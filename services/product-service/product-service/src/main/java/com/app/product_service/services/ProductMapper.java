package com.app.product_service.services;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public Product toProduct(ProductDto dto){

        return  Product.builder().id(dto.id())
                .name(dto.name())
                .availableQuantity(dto.availableQuantity())
                .category(Category.builder().id(dto.category_id()).build())
                .price(dto.price())
                .build();

    }
    public ProductResponse toProductResponse(Product product){
        Category category=product.getCategory();
        return new ProductResponse(product.getId(), product.getName()
                , product.getAvailableQuantity(), product.getPrice(), category.getId(),category.getDescription());
    }
}
