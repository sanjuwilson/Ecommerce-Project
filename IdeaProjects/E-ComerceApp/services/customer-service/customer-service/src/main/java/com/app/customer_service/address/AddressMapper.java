package com.app.customer_service.address;

import com.app.customer_service.customer.Customer;
import org.springframework.stereotype.Service;

@Service
public class AddressMapper {
    public AddressResponse toAddressResponse(Address address) {
        return new AddressResponse(address.getStreet(), address.getHouseNumber(), address.getZipCode());
    }
    public Address toAddress(AddressRequest addressRequest) {
        return Address.builder().houseNumber(addressRequest.houseNumber()).customer(Customer.builder().id(addressRequest.customerId()).build()).street(addressRequest.street()).zipCode(addressRequest.zipCode()).build();
    }

}
