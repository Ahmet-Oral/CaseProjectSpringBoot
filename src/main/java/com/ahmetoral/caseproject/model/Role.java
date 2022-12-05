package com.ahmetoral.caseproject.model;


import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "\"role\"")
public class Role {
    @Id
    @Type(type = "uuid-char")
    private UUID id = UUID.randomUUID();
    private String name;


}

