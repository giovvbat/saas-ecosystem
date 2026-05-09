package com.giovanna.saas_users.service;

import com.giovanna.saas_users.dto.UserDto;
import com.giovanna.saas_users.exception.InvalidOperationException;
import com.giovanna.saas_users.exception.ResourceNotFoundException;
import com.giovanna.saas_users.model.User;
import com.giovanna.saas_users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto("Giovanna", "giovvbatista@gmail.com");
        user = new User("123", "Giovanna", "giovvbatista@gmail.com");
    }

    @Test
    void save_Success() {
        when(repository.findByEmail(userDto.email())).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenReturn(user);

        User result = service.save(userDto);

        assertNotNull(result);

        assertEquals(userDto.name(), result.getName());
        assertEquals(userDto.email(), result.getEmail());

        verify(repository, times(1)).findByEmail(userDto.email());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void save_EmailAlreadyInUse() {
        when(repository.findByEmail(userDto.email())).thenReturn(Optional.of(user));

        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> service.save(userDto));
        assertEquals("provided email address is already in use", exception.getMessage());

        verify(repository, times(1)).findByEmail(userDto.email());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void retrieve_Success() {
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        User result = service.retrieve(user.getId());

        assertNotNull(result);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());

        verify(repository, times(1)).findById(user.getId());
    }

    @Test
    void retrieve_NotFound() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.retrieve("999"));
        assertEquals(exception.getMessage(), "user with id 999 not found");

        verify(repository, times(1)).findById("999");
    }
}