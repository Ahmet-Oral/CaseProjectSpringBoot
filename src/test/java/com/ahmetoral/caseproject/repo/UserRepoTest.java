package com.ahmetoral.caseproject.repo;

import com.ahmetoral.caseproject.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {UserRepo.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.ahmetoral.caseproject.model"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
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

