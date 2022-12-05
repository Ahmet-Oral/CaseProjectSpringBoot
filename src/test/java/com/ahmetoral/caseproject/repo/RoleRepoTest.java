package com.ahmetoral.caseproject.repo;

import com.ahmetoral.caseproject.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@ContextConfiguration(classes = {RoleRepo.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.ahmetoral.caseproject.model"})
@DataJpaTest
class RoleRepoTest {


    @Autowired
    private RoleRepo testRepoRole;

    @Test
    void shouldReturnRoleByGivenRoleName() {
        //given
        Role role = Role.builder().id(UUID.randomUUID()).name("ROLE_USERR").build();
        testRepoRole.save(role);
        //when
        Boolean exists = testRepoRole.findByName("ROLE_USERR").isPresent();
        //then
        assertThat(exists).isTrue();
    }


}