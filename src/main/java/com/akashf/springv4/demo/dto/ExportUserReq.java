package com.akashf.springv4.demo.dto;

import com.akashf.springv4.demo.enums.ReportType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportUserReq extends PaginReq {
    private ReportType type;
}