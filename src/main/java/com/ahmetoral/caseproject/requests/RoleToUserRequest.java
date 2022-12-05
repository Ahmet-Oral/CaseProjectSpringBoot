package com.ahmetoral.caseproject.requests;


import lombok.Data;

// for method addRoleToUser
@Data
public class RoleToUserRequest {
    private String username;
    private String roleName;
}
