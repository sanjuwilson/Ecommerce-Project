package com.app.customer_service.customer;

import com.app.customer_service.address.AddressMapper;
import com.app.customer_service.address.AddressRequest;
import com.app.customer_service.address.AddressService;
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
    private final AddressService addressService;

    public Integer save(CustomerDto customerRequest) {
        Integer id=repo.save(mapper.toCustomer(customerRequest)).getId();
        for(AddressRequest x: customerRequest.addresses()){
            addressService.save(new AddressRequest(x.street()
            ,x.houseNumber(),x.zipCode(),id));
        }
        return id;


    }
    public void updateCustomer(@Valid @RequestBody CustomerDto customerResponse) {
        Customer customer=repo.findById(customerResponse.id()).orElseThrow(()->
            new CustomerNotFoundException("No Customer with the given "+customerResponse.id())
        );
        mergeCustomer(customerResponse,customer);
    }

    private void mergeCustomer(CustomerDto customerRequest, Customer customer) {
        if(StringUtils.isNotBlank(customerRequest.firstname())){
            customer.setFirstname(customerRequest.firstname());
        }
        if(StringUtils.isNotBlank(customerRequest.lastname())){
            customer.setLastname(customerRequest.lastname());
        }
        if(StringUtils.isNotBlank(customerRequest.email())){
            customer.setEmail(customerRequest.email());
        }
        repo.save(customer);
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
