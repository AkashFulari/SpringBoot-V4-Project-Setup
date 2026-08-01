package com.akashf.springv4.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import com.akashf.springv4.demo.enums.ReportType;

import lombok.Data;

@Data
public class ImportUserReq {
    private MultipartFile file;
    private ReportType type;
}