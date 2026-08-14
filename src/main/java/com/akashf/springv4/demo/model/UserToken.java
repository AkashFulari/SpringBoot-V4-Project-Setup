package com.akashf.springv4.demo.model;

import com.akashf.springv4.demo.enums.DeviceType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private DeviceType device;
}