package com.ahmetoral.inventorymanagement.service;

import com.ahmetoral.inventorymanagement.model.User;
import com.ahmetoral.inventorymanagement.repo.RoleRepo;
import com.ahmetoral.inventorymanagement.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class) // initialize mocks and clear resources after each test
class UserServiceImplTest {

    @Mock private UserRepo userRepo;
    @Mock private RoleRepo roleRepo;

    private UserServiceImpl underTest;

    @BeforeEach // run before each test
    void setUp() {
        underTest = new UserServiceImpl(userRepo, roleRepo);
    }


    @Test
    void canGetUsers() {
        // when
        underTest.getUsers();
        // then
        verify(userRepo).findAll();
    }

    @Test
    void canSaveUser() {
        // given
        User userToSave = new User(null,"name","username","password", new ArrayList<>());
        // when
        underTest.saveUser(userToSave);
        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userArgumentCaptor.capture()); // capture saved User
        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(userToSave);
    }

    @Test
    @Disabled
    void saveRole() {
    }

    @Test
    @Disabled
    void addRoleToUser() {
    }

    @Test
    @Disabled
    void getUserByUsername() {

    }


}