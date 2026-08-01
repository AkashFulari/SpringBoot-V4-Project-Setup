package com.akashf.springv4.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportType {
    EXCEL("excel"),
    CSV("csv");

    private String value;
}
