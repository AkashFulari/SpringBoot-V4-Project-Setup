package com.firstest.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String avatar;

    public User(String email, String name, String avatar) {
        this.email = email;
        this.name = name;
        this.avatar = avatar;
    };
}