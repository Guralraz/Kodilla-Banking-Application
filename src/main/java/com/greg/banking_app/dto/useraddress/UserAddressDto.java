package com.greg.banking_app.dto.useraddress;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAddressDto {

    private Long addressId;
    private String addressLine;
    private String zipCode;
    private String city;
    private boolean correspondence;
    private Long userId;
}
