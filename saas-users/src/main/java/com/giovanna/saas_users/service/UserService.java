package com.giovanna.saas_users.service;

import com.giovanna.saas_users.dto.UserDto;
import com.giovanna.saas_users.exception.InvalidOperationException;
import com.giovanna.saas_users.exception.ResourceNotFoundException;
import com.giovanna.saas_users.model.User;
import com.giovanna.saas_users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    @Transactional
    public User save(UserDto userDto) {
        if (repository.findByEmail(userDto.email()).isPresent()) {
            throw new InvalidOperationException("provided email address is already in use");
        }

        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        return repository.save(user);
    }

    public User retrieve(String id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", id));
    }
}
