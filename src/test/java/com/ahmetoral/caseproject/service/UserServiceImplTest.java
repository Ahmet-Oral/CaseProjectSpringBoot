package com.ahmetoral.caseproject.service;

import com.ahmetoral.caseproject.model.Role;
import com.ahmetoral.caseproject.model.User;
import com.ahmetoral.caseproject.repo.RoleRepo;
import com.ahmetoral.caseproject.repo.UserRepo;
import com.ahmetoral.caseproject.requests.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(MockitoExtension.class) // initialize mocks and clear resources after each test
class UserServiceImplTest {


    @Mock
    private UserRepo userRepoMock;
    @Mock
    private RoleRepo roleRepoMock;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl underTest;

    @BeforeEach
    void setUp() {        // run before each test
        underTest = new UserServiceImpl(userRepoMock, roleRepoMock, passwordEncoder);
    }

    @Test
    void canGetUsers() {
        // when
        underTest.getUsers();
        // then
        verify(userRepoMock).findAll();
    }

    @Test
    @Disabled
    void canSaveUser() {
        // given
        UserRequest userToSave = new UserRequest();
        userToSave.setUsername("username");
        userToSave.setPassword("password");
        userToSave.setRole("ROLE_TEST");
        // when
        underTest.saveUser(userToSave);
        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepoMock).save(userArgumentCaptor.capture()); // capture saved User
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getUsername()).isEqualTo(userToSave.getUsername());
        assertThat(capturedUser.getPassword()).isEqualTo(userToSave.getPassword());
    }


    @Test
    void canSaveRole() {
        // given
        Role roleToSave = Role.builder().id(null).name("ROLE_TEST").build();
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
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        when(userRepoMock.findByUsername("username")).thenReturn(Optional.of(user));
        Role role = new Role();
        role.setName("ROLE_TEST");
        when(roleRepoMock.findByName("ROLE_TEST")).thenReturn(Optional.of(role));
        // when
        underTest.setUserRole("username", "ROLE_TEST");
        // then
        verify(userRepoMock).findByUsername("username");
        assertNotNull(userRepoMock.findByUsername("username").get().getRoles());
    }

    @Test
    void checkUsernameExists() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
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
        Role role = new Role();
        role.setName("role");
        when(roleRepoMock.findByName("role")).thenReturn(Optional.of(role));
        assertTrue(underTest.checkRoleExists("role"));
        verify(roleRepoMock).findByName("role");
    }

    @Test
    void checkRoleNotExists() {
        assertFalse(underTest.checkRoleExists("roleThatDoesNotExist"));
        verify(roleRepoMock).findByName("roleThatDoesNotExist");
    }


}