package com.firstest.demo.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.firstest.demo.enums.ReportType;
import com.firstest.demo.enums.TemplateType;
import com.firstest.demo.service.Helper;
import com.firstest.demo.service.Resp;

@RestController
public class GlobalController {

    // Report PDF using the text Content only
    @GetMapping("/download-template/{template}/{type}")
    public ResponseEntity<?> getReportPDF(@PathVariable TemplateType template, @PathVariable ReportType type) {
        HttpHeaders headers = new HttpHeaders();
        try {
            boolean isExcel = type == ReportType.EXCEL;
            String ext = isExcel ? ".xlsx" : ".csv";
            String content = Helper.strPluk("attachment; filename=", template.getName().toLowerCase(), "_template_",
                    ext);
            String fileType = isExcel ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    : "text/csv";

            headers.add(HttpHeaders.CONTENT_DISPOSITION, content);
            headers.setContentType(MediaType.parseMediaType(fileType));
            String templateName = Helper.strPluk("report/templates/", template.getFileName(), ext);
            Resource resource = new ClassPathResource(templateName);

            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (Exception e) {
            headers.setContentType(MediaType.APPLICATION_JSON);
            return Resp.error(e.getMessage());
        }
    }

}