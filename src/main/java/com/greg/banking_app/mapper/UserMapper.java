package com.greg.banking_app.mapper;

import com.greg.banking_app.domain.User;
import com.greg.banking_app.dto.user.UserBaseDto;
import com.greg.banking_app.dto.user.UserCreateDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    public User mapToUser(final UserBaseDto userDto) {
        return new User(
                userDto.getUserId(),
                userDto.getPeselNumber(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getTelephone(),
                userDto.isActive()
        );
    }

    public User mapToUserCreate(final UserCreateDto userDto) {
        return new User(
                userDto.getPeselNumber(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getTelephone()
        );
    }

    public UserBaseDto mapToUserBaseDto(final User user) {
        return new UserBaseDto(
                user.getUserId(),
                user.getPeselNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getTelephone(),
                user.isActive()
        );
    }

    public List<UserBaseDto> mapToUserBaseDtoList(final List<User> users) {
        return users.stream()
                .map(this::mapToUserBaseDto)
                .collect(Collectors.toList());
    }
}
