package com.akashf.springv4.demo.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.akashf.springv4.demo.dto.NotifyReq;
import com.akashf.springv4.demo.dto.SaveTokenReq;
import com.akashf.springv4.demo.enums.ReportType;
import com.akashf.springv4.demo.enums.TemplateType;
import com.akashf.springv4.demo.service.Helper;
import com.akashf.springv4.demo.service.Resp;
import com.akashf.springv4.demo.service.UserTokenService;

@RestController
public class GlobalController {

    private final UserTokenService userTokenService;

    public GlobalController(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

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

    // Report PDF using the text Content only
    @PostMapping("/test-fcm")
    public ResponseEntity<?> testFcm(@RequestBody NotifyReq req) {
        try {
            Helper.notifyFCM(req);
            return Resp.sucess("Notification sent successfully");
        } catch (Exception e) {
            return Resp.error(e.getMessage());
        }
    }

    /**
     * Save a new notification token to the database.
     * Called when user generates a new token.
     */
    @PostMapping("/token/save")
    public ResponseEntity<?> saveToken(@RequestBody SaveTokenReq req) {
        try {
            var response = userTokenService.saveToken(req);
            return Resp.sucess("Token saved successfully", response);
        } catch (IllegalArgumentException e) {
            return Resp.error("Invalid token request: " + e.getMessage());
        } catch (Exception e) {
            return Resp.error("Failed to save token: " + e.getMessage());
        }
    }

    /**
     * Fetch the latest notification token from the database.
     * Called on page load or when user needs the current token.
     */
    @GetMapping("/token/latest")
    public ResponseEntity<?> getLatestToken() {
        try {
            var response = userTokenService.getLatestToken();
            return Resp.sucess("Latest token fetched successfully", response);
        } catch (RuntimeException e) {
            // No token found in database - this is expected on first visit
            return Resp.error("No token found");
        } catch (Exception e) {
            return Resp.error("Failed to fetch token: " + e.getMessage());
        }
    }

}