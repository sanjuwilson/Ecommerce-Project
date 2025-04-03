package com.app.customer_service.customer;
import com.app.customer_service.address.Address;
import com.app.customer_service.address.AddressMapper;
import com.app.customer_service.address.AddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerMapper {
    private final AddressMapper addressMapper;
    public Customer toCustomer(CustomerDto customerRequest) {
        if (customerRequest == null){
            return null;
        }
        Customer customer = new Customer();
        customer.setId(customerRequest.id());
        customer.setEmail(customerRequest.email());
        customer.setFirstname(customerRequest.firstname());
        customer.setLastname(customerRequest.lastname());
        return customer;
    }


    public CustomerResponse toCustomerResponse (Customer customer){
        return new CustomerResponse(customer.getId(),customer.getFirstname(),customer.getLastname(),customer.getEmail(),customer.getAddresses().stream().map(
                addressMapper::toAddressResponse
        ).collect(Collectors.toList()));
    }


}
