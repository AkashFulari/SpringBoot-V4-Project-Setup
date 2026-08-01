package com.akashf.springv4.demo.controller;

import com.akashf.springv4.demo.dto.SetUserReq;
import com.akashf.springv4.demo.enums.ReportType;
import com.akashf.springv4.demo.dto.ExportUserReq;
import com.akashf.springv4.demo.dto.ImportUserReq;
import com.akashf.springv4.demo.dto.PaginReq;
import com.akashf.springv4.demo.model.User;
import com.akashf.springv4.demo.responce.ApiResp;
import com.akashf.springv4.demo.service.Resp;
import com.akashf.springv4.demo.service.UserService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResp<User>> create(@Valid @ModelAttribute SetUserReq user) {
        try {
            User i = service.createUser(user);
            return Resp.sucess("Created", i);
        } catch (Exception e) {
            return Resp.error(e.getMessage());
        }
    }

    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResp<User>> update(@PathVariable Long userId, @Valid @ModelAttribute SetUserReq user) {
        try {
            User i = service.updateUser(userId, user);
            return Resp.sucess("Updated", i);
        } catch (Exception e) {
            return Resp.error(e.getMessage());
        }
    }

    @PostMapping("/list")
    public ResponseEntity<ApiResp<User>> getAll(@RequestBody PaginReq req) {
        try {
            Sort sort = req.getSortDirection().equalsIgnoreCase("DESC") ? Sort.by(req.getSortBy()).descending()
                    : Sort.by(req.getSortBy()).ascending();
            Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), sort);
            Page<User> l = service.getAllUsers(pageable);
            return Resp.sucess("Fetched List", l);
        } catch (Exception e) {
            return Resp.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResp<User>> getAll(Pageable pageable) {
        try {
            Page<User> l = service.getAllUsers(pageable);
            return Resp.sucess("Fetched List", l);
        } catch (Exception e) {
            return Resp.error(e.getMessage());
        }
    }

    // Report PDF using the text Content only
    @GetMapping("/{userId}/report")
    public ResponseEntity<?> getReportPDF(@PathVariable Long userId) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user-profile.pdf");
            headers.setContentType(MediaType.APPLICATION_PDF);
            byte[] pdfBytes = service.userRawReportPdf(userId);
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            headers.setContentType(MediaType.APPLICATION_JSON);
            return Resp.error(e.getMessage());
        }
    }

    // Report PDF using the HTML template
    @GetMapping("/{userId}/report2")
    public ResponseEntity<?> getReportTemplatePDF(@PathVariable Long userId) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user-profile.pdf");
            headers.setContentType(MediaType.APPLICATION_PDF);
            byte[] pdfBytes = service.userTemplateReportPdf(userId);
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            headers.setContentType(MediaType.APPLICATION_JSON);
            return Resp.error(e.getMessage());
        }
    }

    @GetMapping("/export/{type}")
    public ResponseEntity<?> getExport(@PathVariable ReportType type, Pageable pageable) {
        HttpHeaders headers = new HttpHeaders();
        try {
            boolean isExcel = type == ReportType.EXCEL;
            String fileName = isExcel ? "users-report.xlsx" : "users-report.csv";
            String fileType = isExcel ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    : "text/csv";

            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.setContentType(MediaType.parseMediaType(fileType));

            Page<User> page = service.getAllUsers(pageable);
            List<User> list = page.getContent();
            byte[] pdfBytes = isExcel ? service.exportUsersExcel(list) : service.exportUsersCsv(list);
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            headers.setContentType(MediaType.APPLICATION_JSON);
            return Resp.error(e.getMessage());
        }
    }

    @PostMapping("/export")
    public ResponseEntity<?> getExportUser(@RequestBody ExportUserReq req) {
        HttpHeaders headers = new HttpHeaders();
        try {
            boolean isExcel = req.getType() == ReportType.EXCEL;
            String fileName = isExcel ? "users-report.xlsx" : "users-report.csv";
            String fileType = isExcel ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    : "text/csv";

            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.setContentType(MediaType.parseMediaType(fileType));

            Sort sort = req.getSortDirection().equalsIgnoreCase("DESC") ? Sort.by(req.getSortBy()).descending()
                    : Sort.by(req.getSortBy()).ascending();
            Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), sort);
            Page<User> page = service.getAllUsers(pageable);
            List<User> list = page.getContent();
            byte[] pdfBytes = isExcel ? service.exportUsersExcel(list) : service.exportUsersCsv(list);
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            headers.setContentType(MediaType.APPLICATION_JSON);
            return Resp.error(e.getMessage());
        }
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResp<Void>> getImportExcel(@ModelAttribute ImportUserReq req) {
        try {
            boolean isExcel = req.getType() == ReportType.EXCEL;
            if (isExcel)
                service.importUsersExcel(req);
            else
                service.importUsersCsv(req);
            return Resp.sucess("Imported successfully!");
        } catch (Exception e) {
            return Resp.error(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResp<User>> getById(@PathVariable Long userId) {
        try {
            User i = service.getUserById(userId);
            return Resp.sucess("Fetched Info", i);
        } catch (Exception e) {
            return Resp.error(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResp<Void>> delete(@PathVariable Long userId) {
        try {
            service.deleteUser(userId);
            return Resp.sucess("Deleted Info");
        } catch (Exception e) {
            return Resp.error(e.getMessage());
        }
    }

}