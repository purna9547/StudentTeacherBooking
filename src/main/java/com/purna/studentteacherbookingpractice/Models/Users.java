package com.purna.studentteacherbookingpractice.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    private UUID id;

    private String fullname;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String userName;
    private String ph;
    private String password;
    private String role;
//    private String action;

}
