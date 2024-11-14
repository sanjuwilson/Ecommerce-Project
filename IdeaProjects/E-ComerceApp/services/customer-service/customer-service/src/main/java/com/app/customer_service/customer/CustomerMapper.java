package com.app.customer_service.customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer toCustomer(CustomerDto customerResponse) {
        if (customerResponse == null){
            return null;
        }
        Customer customer = new Customer();
        customer.setId(customerResponse.id());
        customer.setEmail(customerResponse.email());
        customer.setAddress(toAddress(customerResponse.address()));
        customer.setFirstname(customerResponse.firstname());
        customer.setLastname(customerResponse.lastname());
        return customer;
    }

    public Address toAddress(AddressDto addressDto) {
        if (addressDto == null){
            return null;
        }
        Address address = new Address();
        address.setStreet(addressDto.street());
        address.setHouseNumber(addressDto.houseNumber());
        address.setZipCode(addressDto.zipCode());
        return address;
    }

    public CustomerResponse toCustomerResponse (Customer customer){
        return new CustomerResponse(customer.getId(),customer.getFirstname(),customer.getLastname(),customer.getEmail(),toAddressResponse(customer.getAddress()));
    }

    private AddressResponse toAddressResponse(Address address) {
        if (address == null){
            return null;
        }
        return new AddressResponse(address.getStreet(),address.getHouseNumber(),address.getZipCode());
    }
}
