package com.greg.banking_app.controller;

import com.greg.banking_app.domain.User;
import com.greg.banking_app.dto.user.UserBaseDto;
import com.greg.banking_app.dto.user.UserCreateDto;
import com.greg.banking_app.exception.UserNotFoundException;
import com.greg.banking_app.mapper.UserMapper;
import com.greg.banking_app.service.UserDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserDbService userDbService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserBaseDto>> getUsers() {
        return ResponseEntity.ok(userMapper.mapToUserBaseDtoList(userDbService.getAllUsers()));
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserBaseDto> getUser(@PathVariable Long userId) throws UserNotFoundException {
            return ResponseEntity.ok(userMapper.mapToUserBaseDto(userDbService.getUser(userId)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserBaseDto> createUser(@RequestBody UserCreateDto userCreateDto) {
        User user = userMapper.mapToUserCreate(userCreateDto);
        return ResponseEntity.ok(userMapper.mapToUserBaseDto(userDbService.createUser(user)));
    }

    @DeleteMapping("{userId}")
    public String deactiveUser(@PathVariable Long userId) throws UserNotFoundException {
        userDbService.deActiveUser(userId);
        return "User is deActivated";
    }

    @PutMapping
    public ResponseEntity<UserBaseDto> updateUser(@RequestBody UserBaseDto userBaseDto) throws UserNotFoundException {
        User user = userMapper.mapToUser(userBaseDto);
        return ResponseEntity.ok(userMapper.mapToUserBaseDto(userDbService.updateUser(user)));
    }
}
