package com.app.customer_service.customer;

import com.app.customer_service.customer.*;

import com.app.customer_service.exceptions.CustomerNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepo repo;
    private final CustomerMapper mapper;

    public Customer save(CustomerDto customerResponse) {
        return repo.save(mapper.toCustomer(customerResponse));
    }
    public void updateCustomer(@Valid @RequestBody CustomerDto customerResponse) {
        Customer customer=repo.findById(customerResponse.id()).orElseThrow(()->
            new CustomerNotFoundException("No Customer with the given "+customerResponse.id())
        );
        mergeCustomer(customerResponse,customer);
    }

    private void mergeCustomer(CustomerDto customerResponse, Customer customer) {
        if(StringUtils.isNotBlank(customerResponse.firstname())){
            customer.setFirstname(customerResponse.firstname());
        }
        if(StringUtils.isNotBlank(customerResponse.lastname())){
            customer.setLastname(customerResponse.lastname());
        }
        if(StringUtils.isNotBlank(customerResponse.email())){
            customer.setEmail(customerResponse.email());
        }
        if(customerResponse.address()!=null){
            customer.setAddress(mapper.toAddress(customerResponse.address()));
        }
    }
    public List<CustomerResponse> getAllCustomer() {
        return repo.findAll().stream().map(mapper:: toCustomerResponse).collect(Collectors.toList());
    }
    public boolean isExisting(int id){
        return repo.findById(id).isPresent();
    }

    public CustomerResponse getById(int cusId) {
       return repo.findById(cusId).map(mapper:: toCustomerResponse)
               .orElseThrow(()-> new CustomerNotFoundException("No Customer with the given "+cusId));
    }

    public void deleteCustomer(int cusId) {
        repo.deleteById(cusId);
    }
}
