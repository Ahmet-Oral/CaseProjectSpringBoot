package com.ahmetoral.caseproject.requests;


import lombok.Data;

@Data
public class UserRequest {
    private String id;
    private String username;
    private String password;
    private String role;
    private Boolean locked;


}
