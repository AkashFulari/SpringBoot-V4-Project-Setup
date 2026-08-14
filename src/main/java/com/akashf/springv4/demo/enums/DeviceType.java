package com.akashf.springv4.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeviceType {
    ANDROID("android"),
    IOS("ios"),
    WEB("web");

    private String value;
}
