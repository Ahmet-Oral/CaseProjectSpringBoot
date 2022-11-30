package com.ahmetoral.inventorymanagement.service;

import com.ahmetoral.inventorymanagement.model.Role;
import com.ahmetoral.inventorymanagement.model.User;
import com.ahmetoral.inventorymanagement.repo.RoleRepo;
import com.ahmetoral.inventorymanagement.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class) // initialize mocks and clear resources after each test
class UserServiceImplTest {

    @Mock private UserRepo userRepoMock;
    @Mock private RoleRepo roleRepoMock;

    private UserServiceImpl underTest;

    @BeforeEach // run before each test
    void setUp() {
        underTest = new UserServiceImpl(userRepoMock, roleRepoMock);
    }

    @Test
    void canGetUsers() {
        // when
        underTest.getUsers();
        // then
        verify(userRepoMock).findAll();
    }
    @Test
    void canSaveUser() {
        // given
        User userToSave = new User(null,"name","username","password", new ArrayList<>());
        // when
        underTest.saveUser(userToSave);
        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepoMock).save(userArgumentCaptor.capture()); // capture saved User
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(userToSave);
    }

    @Test
    void canSaveRole() {
        // given
        Role roleToSave = Role.builder().id(null).name("ROLE_USER").build();
        // when
        roleRepoMock.save(roleToSave);
        // then
        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepoMock).save(roleArgumentCaptor.capture()); // capture saved User
        Role capturedRole = roleArgumentCaptor.getValue();

        assertThat(capturedRole).isEqualTo(roleToSave);
    }
    @Test
    void canAddRoleToUser() {
        // given
        User user = User.builder().id(123L).name("name").password("password")
                .roles(new ArrayList<>()).username("username").build();
        when(userRepoMock.findByUsername("username")).thenReturn(Optional.of(user));
        Role role = Role.builder().id(123L).name("Role Name").build();
        when(roleRepoMock.findByName("Role Name")).thenReturn(Optional.of(role));
        // when
        underTest.addRoleToUser("username", "Role Name");
        // then
        verify(userRepoMock).findByUsername( "username");
        verify(roleRepoMock).findByName("Role Name");
    }
    @Test
    void checkUsernameExists() {
        User user = User.builder().id(123L).name("name").password("password")
                .roles(new ArrayList<>()).username("username").build();
        when(userRepoMock.findByUsername("username")).thenReturn(Optional.of(user));
        assertTrue(underTest.checkUsernameExists("username"));
        verify(userRepoMock).findByUsername("username");
    }
    @Test
    void checkUsernameNotExits() {
        assertFalse(underTest.checkUsernameExists("usernameThatDoesNotExist"));
        verify(userRepoMock).findByUsername("usernameThatDoesNotExist");
    }
    @Test
    void checkRoleExists() {
        Role role = Role.builder().id(123L).name("role").build();
        when(roleRepoMock.findByName("role")).thenReturn( Optional.of(role));
        assertTrue(underTest.checkRoleExists("role"));
        verify(roleRepoMock).findByName("role");
    }
    @Test
    void checkRoleNotExists() {
        assertFalse(underTest.checkRoleExists("roleThatDoesNotExist"));
        verify(roleRepoMock).findByName("roleThatDoesNotExist");
    }




}