package com.paymybuddy.application.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.application.exception.UserAlreadyExistException;
import com.paymybuddy.application.exception.UserNotFoundException;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.UserRepository;
import com.paymybuddy.application.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

        @Mock
        private UserRepository userRepository;

        private UserService userService;

        User defaultUser;
        User updatedUser;

        @BeforeEach
        public void clear() {
                userService = new UserService(userRepository);
        }

        @Test
        public void getUser() throws UserNotFoundException {
                defaultUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();

                when(userRepository.findByEmail("defaultEmail")).thenReturn(Optional.of(defaultUser));
                userService.getUserByEmail(defaultUser);

                verify(userRepository).findByEmail("defaultEmail");
        }

        @Test
        public void getUserNotFound() throws UserNotFoundException {
                defaultUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();

                when(userRepository.findByEmail("defaultEmail")).thenReturn(Optional.empty());

                assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(defaultUser));
        }

        @SuppressWarnings("null")
        @Test
        public void testCreateNewUser() throws UserAlreadyExistException {
                defaultUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();

                when(userRepository.findByEmail("defaultEmail")).thenReturn(Optional.empty());
                userService.createUser(defaultUser);

                verify(userRepository).save(defaultUser);
        }

        @Test
        public void testNewUserAlreadyExist() throws UserAlreadyExistException {
                defaultUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();

                when(userRepository.findByEmail("defaultEmail")).thenReturn(Optional.of(defaultUser));

                assertThrows(UserAlreadyExistException.class, () -> userService.createUser(defaultUser));
        }

        @SuppressWarnings("null")
        @Test
        public void testDeleteUser() throws UserNotFoundException {
                defaultUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();

                when(userRepository.findByEmail("defaultEmail")).thenReturn(Optional.of(defaultUser));
                userService.deleteUserByEmail(defaultUser);

                verify(userRepository).delete(defaultUser);
        }

        @Test
        public void testDeleteUserNotFound() throws UserNotFoundException {
                defaultUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();

                when(userRepository.findByEmail("defaultEmail")).thenReturn(Optional.empty());

                assertThrows(UserNotFoundException.class, () -> userService.deleteUserByEmail(defaultUser));
        }

        @SuppressWarnings("null")
        @Test
        public void testUpdateUser() throws UserNotFoundException, UserAlreadyExistException {
                defaultUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();
                updatedUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("newPassword").build();

                when(userRepository.findByEmail("defaultEmail"))
                                .thenReturn(Optional.of(defaultUser))
                                .thenReturn(Optional.empty());
                userService.updateUser(updatedUser);

                verify(userRepository).save(updatedUser);
        }

        @Test
        public void testUpdateUserNotFound() throws UserNotFoundException {
                defaultUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();
                updatedUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("newPassword").build();

                when(userRepository.findByEmail("defaultEmail")).thenReturn(Optional.empty());

                assertThrows(UserNotFoundException.class, () -> userService.updateUser(defaultUser));
        }

}
