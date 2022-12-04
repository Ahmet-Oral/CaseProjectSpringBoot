package com.ahmetoral.inventorymanagement.repo;

import com.ahmetoral.inventorymanagement.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class UserRepoTest {

    @Autowired
    private UserRepo testRepoUser;

    @Test
    void shouldReturnUserByGivenUsername() {
        //given
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        testRepoUser.save(user);
        //when
        Boolean exists = testRepoUser.findByUsername("username").isPresent();
        //then
        assertThat(exists).isTrue();
    }
}

