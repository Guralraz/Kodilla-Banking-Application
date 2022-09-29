package com.greg.banking_app.mapper;

import com.greg.banking_app.domain.User;
import com.greg.banking_app.domain.UserAddress;
import com.greg.banking_app.dto.useraddress.UserAddressDto;
import com.greg.banking_app.exception.UserNotFoundException;
import com.greg.banking_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAddressMapper {

    @Autowired
    private UserRepository userRepository;

    public UserAddress mapToUserAddress(final UserAddressDto userAddressDto) throws UserNotFoundException {
            User currentUser = userRepository.findById(userAddressDto.getUserId()).orElseThrow(UserNotFoundException::new);
            return new UserAddress(
                    userAddressDto.getAddressId(),
                    userAddressDto.getAddressLine(),
                    userAddressDto.getZipCode(),
                    userAddressDto.getCity(),
                    userAddressDto.isCorrespondence(),
                    currentUser
            );
    }

    public UserAddressDto mapToUserAddressDto(final UserAddress userAddress) {
        return new UserAddressDto(
                userAddress.getAddressId(),
                userAddress.getAddressLine(),
                userAddress.getZipCode(),
                userAddress.getCity(),
                userAddress.isCorrespondence(),
                userAddress.getUser().getUserId()
        );
    }

    public List<UserAddressDto> mapToUserAddressDtoList(final List<UserAddress> userAddresses) {
        return userAddresses.stream()
                .map(this::mapToUserAddressDto)
                .collect(Collectors.toList());
    }
}
