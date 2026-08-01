package com.firstest.demo.dto;

import com.firstest.demo.enums.ReportType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportUserReq extends PaginReq {
    private ReportType type;
}