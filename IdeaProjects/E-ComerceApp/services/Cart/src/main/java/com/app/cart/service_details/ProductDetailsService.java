package com.app.cart.service_details;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductDetailsService {
    private final ProductDetailsRepository productDetailsRepository;
    private final ProductDetailsMapper mapper;

    public void save (ProductDetails productDetails) {
        productDetailsRepository.save(productDetails);
    }


}
