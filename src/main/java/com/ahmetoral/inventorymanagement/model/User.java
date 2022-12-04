package com.ahmetoral.inventorymanagement.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;


@Entity
@Getter
@Setter
@ToString

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "\"user\"")
public class User {

    @Id
    @Type(type = "uuid-char")
    private UUID id = UUID.randomUUID();

    @Column(unique = true)
    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER) // When fetching user, load roles at the same time
    private Collection<Role> roles = new ArrayList<>();

    private Boolean locked = false;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    @CreationTimestamp
    @Column(updatable = false)
    private Date dateCreated;


}
