package com.greg.banking_app.service;

import com.greg.banking_app.domain.UserAddress;
import com.greg.banking_app.exception.UserAddressNotFoundException;
import com.greg.banking_app.exception.UserNotFoundException;
import com.greg.banking_app.repository.UserAddressRepository;
import com.greg.banking_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressDbService {

    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;

    public List<UserAddress> getUserAddresses(final Long userId) throws UserNotFoundException {
        if(userRepository.existsById(userId)) {
            return userAddressRepository.findByUser_UserId(userId);
        } else {
            throw new UserNotFoundException();
        }
    }

    public UserAddress getUserAddress(final Long userId, final Long addressId) throws UserNotFoundException, UserAddressNotFoundException {
        if(userRepository.existsById(userId)) {
            if(userAddressRepository.existsById(addressId)) {
                return userAddressRepository.findByUser_UserIdAndAddressId(userId, addressId);
            } else {
                throw new UserAddressNotFoundException();
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    public UserAddress createUserAddress(final UserAddress userAddress) {
        return userAddressRepository.save(userAddress);
    }

    public UserAddress updateUserAddress(final UserAddress userAddress) throws UserAddressNotFoundException {
        if(userAddressRepository.existsById(userAddress.getAddressId())) {
            return userAddressRepository.save(userAddress);
        } else {
            throw new UserAddressNotFoundException();
        }
    }

    public void deleteUserAddress(final Long addressId) throws UserAddressNotFoundException {
        if(userAddressRepository.existsById(addressId)) {
            userAddressRepository.deleteById(addressId);
        } else {
            throw new UserAddressNotFoundException();
        }
    }
}
