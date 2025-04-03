package com.app.product_service.services;

import com.app.product_service.Exceptions.OutOfStockException;
import com.app.product_service.Exceptions.ProductNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo repo;
    private final ProductMapper mapper;

    public Integer save(ProductDto dto) {
        return repo.save(mapper.toProduct(dto)).getId();
    }
    public List<ProductResponse> getAllProducts(){
        return repo.findAll().stream().map(mapper:: toProductResponse).collect(Collectors.toList());
    }
   @Transactional
    public List<ProductResponse> requestPurchase(List<PurchaseRequest> request){
        List<Integer>ids=request.stream().map(PurchaseRequest::id).toList();
        List<Product> products=repo.findAllByIdInOrderById(ids);
        if(products.size() !=ids.size()){
            throw new ProductNotFoundException("One or More Products you requested is not on our database");
        }
        else {
            List<PurchaseRequest> requestSorted = request.stream().sorted
                    (Comparator.comparing(PurchaseRequest::id)).toList();
            List<ProductResponse> response = new ArrayList<>();
            for (int i = 0; i < products.size(); i++) {
                Product pro = products.get(i);
                PurchaseRequest req = requestSorted.get(i);
                if (pro.getAvailableQuantity() < req.quantity()) {
                    throw new OutOfStockException("Product Out Of Stock");
                } else {
                    pro.setAvailableQuantity(pro.getAvailableQuantity() - req.quantity());
                    repo.save(pro);
                    response.add(mapper.toProductResponse(pro));
                }

            }
            return response;
        }
    }
    public ProductResponse getProductsById(Integer id) {
        return repo.findById(id).map(mapper:: toProductResponse).orElseThrow(()->
                new EntityNotFoundException("Product with id "+id+" not found"));

    }
}
