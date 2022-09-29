package com.greg.banking_app.controller;

import com.greg.banking_app.domain.UserAddress;
import com.greg.banking_app.dto.useraddress.UserAddressDto;
import com.greg.banking_app.exception.UserAddressNotFoundException;
import com.greg.banking_app.exception.UserNotFoundException;
import com.greg.banking_app.mapper.UserAddressMapper;
import com.greg.banking_app.service.UserAddressDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/addresses")
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressDbService userAddressDbService;
    private final UserAddressMapper userAddressMapper;

    @GetMapping("{userId}")
    public ResponseEntity<List<UserAddressDto>> getUserAddresses(@PathVariable Long userId) throws UserNotFoundException {
        return ResponseEntity.ok(userAddressMapper.mapToUserAddressDtoList(userAddressDbService.getUserAddresses(userId)));
    }

    @GetMapping("{userId}/{addressId}")
    public ResponseEntity<UserAddressDto> getUserAddress(@PathVariable Long userId, @PathVariable Long addressId) throws UserNotFoundException, UserAddressNotFoundException {
        return ResponseEntity.ok(userAddressMapper.mapToUserAddressDto(userAddressDbService.getUserAddress(userId, addressId)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserAddressDto> createAddress(@RequestBody UserAddressDto userAddressDto) throws UserNotFoundException {
        UserAddress userAddress = userAddressMapper.mapToUserAddress(userAddressDto);
        return ResponseEntity.ok(userAddressMapper.mapToUserAddressDto(userAddressDbService.createUserAddress(userAddress)));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserAddressDto> updateAddress(@RequestBody UserAddressDto userAddressDto) throws UserNotFoundException, UserAddressNotFoundException {
        UserAddress userAddress = userAddressMapper.mapToUserAddress(userAddressDto);
        return ResponseEntity.ok(userAddressMapper.mapToUserAddressDto(userAddressDbService.updateUserAddress(userAddress)));
    }

    @DeleteMapping("{addressId}")
    public String deleteAddress(@PathVariable Long addressId) throws UserAddressNotFoundException {
        userAddressDbService.deleteUserAddress(addressId);
        return "Address with id: " + addressId + " deleted";
    }
}
