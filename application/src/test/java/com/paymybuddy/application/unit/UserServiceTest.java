package com.paymybuddy.application.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.application.exception.AccountCodeAlreadyInUse;
import com.paymybuddy.application.exception.UserAlreadyExistException;
import com.paymybuddy.application.exception.UserNotFoundException;
import com.paymybuddy.application.model.Account;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.AccountRepository;
import com.paymybuddy.application.repository.UserRepository;
import com.paymybuddy.application.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

        @Mock
        private UserRepository userRepository;
        @Mock
        private AccountRepository accountRepository;

        @Captor
        ArgumentCaptor<User> userCaptor;

        private UserService userService;

        User defaultUser;
        User updatedUser;
        User friendUser;
        Account defaultAccount;
        Account existingAccount;

        @BeforeEach
        public void setup() {
                userService = new UserService(userRepository, accountRepository);
        }

        @Test
        public void getUser() throws UserNotFoundException {
                defaultUser = User.builder().id(1).build();

                when(userRepository.findById(defaultUser.getId())).thenReturn(Optional.of(defaultUser));
                userService.getUserById(defaultUser.getId());

                verify(userRepository).findById(defaultUser.getId());
        }

        @Test
        public void getUserNotFound() throws UserNotFoundException {
                defaultUser = User.builder().id(1).build();

                when(userRepository.findById(defaultUser.getId())).thenReturn(Optional.empty());

                assertThrows(UserNotFoundException.class, () -> userService.getUserById(defaultUser.getId()));
        }

        @Test
        public void testCreateNewUser() throws UserAlreadyExistException, AccountCodeAlreadyInUse {
                defaultAccount = Account.builder().code("code").build();
                defaultUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").account(defaultAccount)
                                .build();

                when(userRepository.findByEmail(defaultUser.getEmail())).thenReturn(Optional.empty());
                userService.createUser(defaultUser);

                verify(userRepository).save(userCaptor.capture());

                assertTrue(new ReflectionEquals(defaultUser, "account").matches(userCaptor.getValue()));
                assertTrue(new ReflectionEquals(defaultUser.getAccount().toBuilder().amount(0.).build())
                                .matches(userCaptor.getValue().getAccount()));
        }

        @Test
        public void testNewUserAlreadyExist() throws UserAlreadyExistException {
                defaultUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();

                when(userRepository.findByEmail(defaultUser.getEmail())).thenReturn(Optional.of(defaultUser));

                assertThrows(UserAlreadyExistException.class, () -> userService.createUser(defaultUser));
        }

        @Test
        public void testNewUserCodeAlreadyExist() throws AccountCodeAlreadyInUse {
                defaultAccount = Account.builder().code("code").build();
                defaultUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").account(defaultAccount)
                                .build();

                when(userRepository.findByEmail(defaultUser.getEmail())).thenReturn(Optional.empty());
                when(accountRepository.findByCode(defaultAccount.getCode())).thenReturn(Optional.of(defaultAccount));

                assertThrows(AccountCodeAlreadyInUse.class, () -> userService.createUser(defaultUser));
        }

        @Test
        public void testDeleteUser() throws UserNotFoundException {
                defaultUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();

                when(userRepository.findById(defaultUser.getId())).thenReturn(Optional.of(defaultUser));
                userService.deleteUserById(defaultUser);

                verify(userRepository).delete(defaultUser);
        }

        @Test
        public void testDeleteUserNotFound() throws UserNotFoundException {
                defaultUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();

                when(userRepository.findById(defaultUser.getId())).thenReturn(Optional.empty());

                assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(defaultUser));
        }

        @Test
        public void testUpdateUser() throws UserNotFoundException, UserAlreadyExistException, AccountCodeAlreadyInUse {
                defaultAccount = Account.builder().id(1).code("code").amount(0.).build();
                defaultUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").account(defaultAccount).password("defaultPassword")
                                .build();
                updatedUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").account(defaultAccount).password("newPassword").build();

                when(userRepository.findById(defaultUser.getId()))
                                .thenReturn(Optional.of(defaultUser))
                                .thenReturn(Optional.empty());
                userService.updateUser(updatedUser);

                verify(userRepository).save(updatedUser);
        }

        @Test
        public void testUpdateUserNotFound() throws UserNotFoundException {
                defaultUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();
                updatedUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("newPassword").build();

                when(userRepository.findById(defaultUser.getId())).thenReturn(Optional.empty());

                assertThrows(UserNotFoundException.class, () -> userService.updateUser(defaultUser));
        }

        @Test
        public void testUpdateUserCodeAlreadyExist() throws AccountCodeAlreadyInUse {
                defaultAccount = Account.builder().id(1).code("code").amount(0.).build();
                existingAccount = defaultAccount.toBuilder().id(2).build();
                defaultUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").account(defaultAccount).password("defaultPassword")
                                .build();
                updatedUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").account(defaultAccount).password("newPassword").build();

                when(userRepository.findById(defaultUser.getId()))
                                .thenReturn(Optional.of(defaultUser))
                                .thenReturn(Optional.empty());
                when(accountRepository.findByCode(defaultAccount.getCode())).thenReturn(Optional.of(existingAccount));

                assertThrows(AccountCodeAlreadyInUse.class, () -> userService.updateUser(defaultUser));
        }

        @Test
        public void testAddToFriendlist() throws UserNotFoundException, AccountCodeAlreadyInUse {
                defaultAccount = Account.builder().id(1).code("code").amount(0.).build();
                defaultUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").account(defaultAccount).password("defaultPassword")
                                .friends(new HashSet<>())
                                .build();
                friendUser = User.builder().id(2).email("friendEmail").firstName("friendFirstName")
                                .lastName("friendLastName").account(defaultAccount).password("friendPassword")
                                .friends(new HashSet<>()).build();

                when(userRepository.findById(defaultUser.getId())).thenReturn(Optional.of(defaultUser));
                when(userRepository.findByEmail("friendEmail")).thenReturn(Optional.of(friendUser));
                userService.addUserToFriendlist(defaultUser, "friendEmail");

                verify(userRepository).save(defaultUser);
        }

        @Test
        public void testAddToFriendlistNoUser() throws UserNotFoundException {
                defaultUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").friends(new HashSet<>())
                                .build();
                friendUser = User.builder().id(2).email("friendEmail").firstName("friendFirstName")
                                .lastName("friendLastName").password("friendPassword").friends(new HashSet<>()).build();

                assertThrows(UserNotFoundException.class, () -> userService.addUserToFriendlist(defaultUser,
                                "friendEmail"));
        }

        @Test
        public void testAddToFriendlistNoFriend() throws UserNotFoundException {
                defaultAccount = Account.builder().id(1).code("code").amount(0.).build();
                defaultUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").account(defaultAccount).password("defaultPassword")
                                .friends(new HashSet<>())
                                .build();
                friendUser = User.builder().id(2).email("friendEmail").firstName("friendFirstName")
                                .lastName("friendLastName").account(defaultAccount).password("friendPassword")
                                .friends(new HashSet<>()).build();

                assertThrows(UserNotFoundException.class,
                                () -> userService.addUserToFriendlist(defaultUser, "friendEmail"));
        }
}
