package com.akashf.springv4.demo.dto;

import com.akashf.springv4.demo.enums.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveTokenReq {
    private String token;
    private DeviceType device;
}
