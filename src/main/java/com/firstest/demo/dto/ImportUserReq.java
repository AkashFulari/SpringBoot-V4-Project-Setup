package com.firstest.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import com.firstest.demo.enums.ReportType;

import lombok.Data;

@Data
public class ImportUserReq {
    private MultipartFile file;
    private ReportType type;
}