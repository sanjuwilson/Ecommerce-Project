package com.app.rewards_service.service_reward;

import com.app.rewards_service.exceptions.*;
import com.app.rewards_service.producer.KafkaProducer;
import com.app.rewards_service.securtity.TokenGenerator;
import com.app.rewards_service.service_details.ProductDetails;
import com.app.rewards_service.service_gift_cards.GiftCard;
import com.app.rewards_service.service_gift_cards.GiftCardService;
import com.app.rewards_service.service_gift_cards.GiftCardStatus;
import com.app.rewards_service.service_gift_cards.giftcard_transactioninfo.GiftCardTransactionDetails;
import com.app.rewards_service.service_order.OrderClient;
import com.app.rewards_service.service_points.PointsPayment;
import com.app.rewards_service.service_points.PointsService;
import com.ecom.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final GiftCardService giftCardService;
    private final KafkaProducer producer;
    private final PointsService pointsService;
    private final OrderClient orderClient;
    private static final double CASH_PER_POINT= 0.2;
    private final KafkaProducer kafkaProducer;
    private final Set<TransactionMethod>transactionMethods=new HashSet<>();
    private final Map<TransactionMethod,BigDecimal>map=new HashMap<>();
    private final TokenGenerator tokenGenerator;


    public Integer saveTransaction(TransactionRequest transactionRequest) {
        if(checkOrderExistence(transactionRequest.orderReference())){
            throw new DuplicateOrderException("An Order with with the given reference already exists");
        }
        else{
            boolean giftCardIsUsable = false;

            AtomicInteger totalPointsEarnedNow = new AtomicInteger(0);
            List<ProductDetails> productDetails = new ArrayList<>();
            try{
                List<ProductPointsRecord>pointsRecords=new ArrayList<>();
                String token = "Bearer " + tokenGenerator.generateToken();
                System.out.println(token);
                TransactionResponse transactionResponse = orderClient.getTransactionDetails(transactionRequest.orderReference(),token);

                Transaction transaction = Transaction.builder()
                        .orderReference(transactionRequest.orderReference())
                        .userId(transactionResponse.customerId())
                        .build();

                transactionResponse.request().forEach(product -> {
                    int points = switch (product.category()) {
                        case "Electronics" -> 10;
                        case "Automotive" -> 15;
                        default -> 4;
                    };
                    totalPointsEarnedNow.addAndGet(points);



                    productDetails.add(ProductDetails.builder()
                            .productId(product.productId())
                            .quantity(product.quantity())
                            .point(points)
                            .cashPerPoint(CASH_PER_POINT)
                            .transaction(transaction)
                            .build());
                    pointsRecords.add(new ProductPointsRecord(product.name(),product.quantity(),points,CASH_PER_POINT));
                });

                PointsPayment latestPoints = pointsService.getPointsDetailsByUserId(transactionResponse.customerId())
                        .orElse(null);

                int previousPoints = latestPoints != null ? latestPoints.getCurrentPointsAfterTransaction() : 0;
                int pointsUsed;
                if(transactionRequest.pointsUsed()!=null && transactionRequest.pointsUsed()!=0){
                    pointsUsed = transactionRequest.pointsUsed();
                    transactionMethods.add(TransactionMethod.POINTS);
                }
                else{
                    pointsUsed = 0;
                }
                if (pointsUsed > previousPoints) {
                    throw new InsufficientPointsException("Insufficient Points");
                }

                int updatedPoints = (previousPoints + totalPointsEarnedNow.get()) - pointsUsed;

                PointsPayment newPointsRecord = PointsPayment.builder()
                        .currentPointsAfterTransaction(updatedPoints)
                        .pointsEarned(totalPointsEarnedNow.get())
                        .pointsUsed(pointsUsed)
                        .cashEarned(totalPointsEarnedNow.get() * CASH_PER_POINT)
                        .totalCashAfterTransaction(updatedPoints * CASH_PER_POINT)
                        .transaction(transaction)
                        .build();
                GiftCard giftCard=null;
                if (transactionRequest.amountFromGiftCard() != null &&transactionRequest.amountFromGiftCard().compareTo(BigDecimal.ZERO) > 0) {
                    giftCard=giftCardService.checkForExistingGiftCardsWithCode(transactionRequest.giftCardCode());
                    if(giftCard!=null){
                        if(giftCard.getStatus().equals(GiftCardStatus.ACTIVE)){
                            BigDecimal amountInGiftCard=giftCard.getRemainingBalance();
                            if (transactionRequest.amountFromGiftCard().compareTo(amountInGiftCard) > 0) {
                                throw new IllegalArgumentException("Requested amount exceeds available gift card balance.");
                            }
                            else{
                                BigDecimal newBalance=amountInGiftCard.subtract(transactionRequest.amountFromGiftCard());
                                giftCard.setRemainingBalance(newBalance);
                                giftCardIsUsable=true;
                                if(newBalance.compareTo(BigDecimal.ZERO)==0){
                                    giftCard.setStatus(GiftCardStatus.USED);
                                }


                            }
                        }
                        else{
                            throw new GiftCardNotActiveException("Gift Card Not Active.");
                        }

                    }
                    if(giftCard==null && transactionRequest.giftCardCode()!=null){
                        throw new NonExistentGiftCardException("Non existent gift card.");
                    }


                }

                double cashFromPoints=pointsUsed*CASH_PER_POINT;
                transaction.setDetails(productDetails);
                transaction.setPointsPayment(newPointsRecord);
                BigDecimal cashFromPointsBD = BigDecimal.valueOf(cashFromPoints);
                BigDecimal totalAmount = transactionResponse.amount(); // total order amount
                BigDecimal amountAfterPointDeduction = totalAmount.subtract(cashFromPointsBD);
                BigDecimal amountAfterGiftCardDeduction = amountAfterPointDeduction;
                if(transactionMethods.contains(TransactionMethod.POINTS)){
                    map.put(TransactionMethod.POINTS, cashFromPointsBD);
                }

                if (giftCardIsUsable) {
                    amountAfterGiftCardDeduction = amountAfterPointDeduction.subtract(transactionRequest.amountFromGiftCard());
                    transactionMethods.add(TransactionMethod.GIFT_CARD);
                    map.put(TransactionMethod.GIFT_CARD, transactionRequest.amountFromGiftCard());
                }


                boolean isCredit=manageCard(transactionRequest,amountAfterGiftCardDeduction,transactionResponse);
                transaction.setTotalPayableAmount(amountAfterGiftCardDeduction);


                List<GiftCardTransactionDetails>details=new ArrayList<>();
                GiftCardTransactionDetails giftCardTransactionDetail=null;
                if(giftCardIsUsable){
                    details=giftCard.getGiftCardTransactions();
                    giftCardTransactionDetail= GiftCardTransactionDetails.builder()
                            .amountUsed(transactionRequest.amountFromGiftCard())
                            .giftCard(giftCard)
                            .transaction(transaction)
                            .build();
                    details.add(giftCardTransactionDetail);
                }
                if(giftCardTransactionDetail!=null){
                    transaction.setGiftCardTransactionDetails(giftCardTransactionDetail);
                }
                String fullName=transactionResponse.customerResponse().firstname()+" "+transactionResponse.customerResponse().lastname();

                kafkaProducer.sendPointsMessage(new PointDetailsConfirmationRecord(updatedPoints,totalPointsEarnedNow.get(),totalPointsEarnedNow.get()*CASH_PER_POINT,fullName,transactionResponse.customerResponse().email(),transactionRequest.orderReference(),pointsRecords));
                BigDecimal amountFromCredit=BigDecimal.ZERO;
                BigDecimal amountFromDebit=BigDecimal.ZERO;
                if(isCredit){
                    amountFromCredit=transactionRequest.amountFromCredit();

                }
                else{
                    amountFromDebit=transactionRequest.amountFromDebit();

                }

                PaymentMethodDetails paymentMethodDetails=PaymentMethodDetails.builder()
                        .amountFromPoints(cashFromPointsBD)
                        .amountFromGiftCard(transactionRequest.amountFromGiftCard())
                        .paymentMethod(transactionMethods)
                        .amountFromCreditCard(amountFromCredit)
                        .amountFromDebitCard(amountFromDebit)
                        .transaction(transaction).build() ;
                transaction.setPaymentMethodDetails(paymentMethodDetails);
                Integer id=transactionRepository.save(transaction).getTransactionId();
                if(giftCard!=null){
                    giftCard.setGiftCardTransactions(details);
                    giftCardService.save(giftCard);
                }
                producer.sendCartStatusUpdate(new CartUpdater(transactionResponse.cartReference(),LocalDateTime.now(), CartStatus.CHECKED_OUT));
                producer.sendOrderStatusUpdate(new OrderUpdater(transactionRequest.orderReference(), LocalDateTime.now(), OrderStatus.COMPLETED));
                map.clear();
                transactionMethods.clear();


                return id;
            }catch(OrderNotFoundException e){
                System.out.println(e.getMessage());

            }

        }



        return null;
    }

    private boolean checkOrderExistence(String orderReference) {
        return transactionRepository.existsByOrderReference(orderReference);
    }

    private boolean manageCard(TransactionRequest transactionRequest,BigDecimal amountAfterGiftCardDeduction,TransactionResponse transactionResponse){
        boolean isCreditCard=false;
        boolean isCreditCardActive=false;
        boolean isDebitCardActive=false;
        BigDecimal remaining = amountAfterGiftCardDeduction;
        if(transactionRequest.amountFromCredit().compareTo(BigDecimal.ZERO)>0){
            isCreditCardActive=true;
            remaining=amountAfterGiftCardDeduction.subtract(transactionRequest.amountFromCredit());
            transactionMethods.add(TransactionMethod.CREDIT_CARD);
            map.put(TransactionMethod.CREDIT_CARD, transactionRequest.amountFromCredit());
            isCreditCard=true;
        }
        if(transactionRequest.amountFromDebit().compareTo(BigDecimal.ZERO)>0){
            isDebitCardActive=true;
            remaining=amountAfterGiftCardDeduction.subtract(transactionRequest.amountFromDebit());
            transactionMethods.add(TransactionMethod.DEBIT_CARD);
            map.put(TransactionMethod.DEBIT_CARD, transactionRequest.amountFromDebit());
            isCreditCard=false;
        }
        if(isDebitCardActive && isCreditCardActive){
            throw new IllegalArgumentException("A Credit and a debit cannot be used at the same time");
        }
        if(remaining.compareTo(BigDecimal.ZERO)!=0){
            throw new TransactionFailedException("Amount Mismatch.Transaction Failed");
        }else{
            List<ProductRecord>productRecordList=transactionResponse.request().stream().map(transaction-> new ProductRecord(transaction.name(),transaction.price(),transaction.category(),transaction.quantity())).toList();
            List<TransactionMethod> paymentMethodList= new ArrayList<>(transactionMethods);

            producer.sendOrderMessage(new OrderConfirmationRecord(transactionRequest.orderReference(),
                    transactionResponse.amount(),new CustomerRecord(transactionResponse.customerResponse().firstname(),transactionResponse.customerResponse().lastname(),transactionResponse.customerResponse().email()),
                    productRecordList,paymentMethodList
                    ));
            producer.sendPaymentMessage(new PaymentConfirmationRecord(transactionRequest.orderReference(),transactionResponse.amount(),
                    map,transactionResponse.customerResponse().firstname(),transactionResponse.customerResponse().lastname(),
                    transactionResponse.customerResponse().email()
            ));
        }
        return isCreditCard;




    }


}
