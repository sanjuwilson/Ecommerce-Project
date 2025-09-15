package com.app.customer_service.address;

import com.app.customer_service.exceptions.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public List<AddressResponse> getAll(){
        return addressRepository.findAll().stream().map(addressMapper::toAddressResponse).collect(Collectors.toList());
    }
    public Integer save(AddressRequest addressRequest){
        return addressRepository.save(addressMapper.toAddress(addressRequest)).getId();
    }
    public List<AddressResponse> getByCustomerId(Integer id){
        return addressRepository.findAllByCustomerId(id).stream().map(addressMapper::toAddressResponse).collect(Collectors.toList());
    }
    public void update(Integer id,AddressRequest addressRequest){
      Address existingAddress=addressRepository.findById(id).orElseThrow(()->new CustomerNotFoundException("Not found"));
      existingAddress.setStreet(addressRequest.street());
      existingAddress.setZipCode(addressRequest.zipCode());
      existingAddress.setHouseNumber(addressRequest.houseNumber());
      addressRepository.save(existingAddress);

    }
    public void delete(Integer id){
        addressRepository.deleteById(id);
    }
}
