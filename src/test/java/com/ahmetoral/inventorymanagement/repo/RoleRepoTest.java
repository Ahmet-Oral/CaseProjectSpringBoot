package com.ahmetoral.inventorymanagement.repo;

import com.ahmetoral.inventorymanagement.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class RoleRepoTest {

    @Autowired
    private RoleRepo testRepoRole;

    @Test
    void shouldReturnRoleByGivenRoleName() {
        //given
        Role role = Role.builder().id(null).name("ROLE_USER").build();
        testRepoRole.save(role);
        //when
        Boolean exists = testRepoRole.findByName("ROLE_USER").isPresent();
        //then
        assertThat(exists).isTrue();
    }
}