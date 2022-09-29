package com.greg.banking_app.service;

import com.greg.banking_app.domain.User;
import com.greg.banking_app.exception.UserNotFoundException;
import com.greg.banking_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDbService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(final Long userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public User createUser(final User user) {
        return userRepository.save(user);
    }

    public void deActiveUser(final Long userId) throws UserNotFoundException {
        if(userRepository.existsById(userId)) {
            User currentUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
            currentUser.setActive(false);
            userRepository.save(currentUser);
        } else {
            throw new UserNotFoundException();
        }
    }

    public User updateUser(final User user) throws UserNotFoundException {
        if(userRepository.existsById(user.getUserId())) {
            return userRepository.save(user);
        } else {
            throw new UserNotFoundException();
        }
    }
}
