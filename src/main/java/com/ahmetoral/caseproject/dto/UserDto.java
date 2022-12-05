package com.ahmetoral.caseproject.dto;


import com.ahmetoral.caseproject.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class UserDto {
    private String username;
    private String role;
    private Boolean locked;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date dateCreated;

    public UserDto(User user) {
        this.username = user.getUsername();
        if (user.getRoles().stream().findFirst().isPresent()){
            this.role = String.valueOf(user.getRoles().stream().findFirst().get().getName());
        } else {
            this.role ="";
        }
        this.locked = user.getLocked();
        this.dateCreated = user.getDateCreated();
    }
}


