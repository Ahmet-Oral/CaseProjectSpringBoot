package com.ahmetoral.inventorymanagement.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FailedLoginAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer numberOfAttempts = 0;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

}
