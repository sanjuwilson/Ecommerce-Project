package com.app.rewards_service.service_gift_cards;

import com.app.rewards_service.service_gift_cards.code.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class GiftCardService {
    private final GiftCardRepository giftCardRepository;

    public Integer saveGiftCard(GiftCardPurchaseRequest giftCardRequest) {
        var record=GiftCardPurchaseRecord.builder().purchaserUserId(giftCardRequest.userId())
                .amount(giftCardRequest.amount())
                .recipientEmail(giftCardRequest.email()).build();
        var giftCard=GiftCard.builder()
                .code(CodeGenerator.generateSecureCode(20))
                .expiryDate(LocalDateTime.now().plusYears(2))
                .initialAmount(giftCardRequest.amount())
                .remainingBalance(giftCardRequest.amount())
                .status(GiftCardStatus.ACTIVE)
                .giftCardPurchaseRecord(record)
                .build();
        record.setGiftCard(giftCard);
        return giftCardRepository.save(giftCard).getId();
    }
    public void sendGiftCardViaMail(){
        //TO DO
    }

    public GiftCard checkForExistingGiftCardsWithCode(String s) {
        return giftCardRepository.findByCode(s);

    }
    public Integer save(GiftCard giftCard) {
        return giftCardRepository.save(giftCard).getId();
    }
}
