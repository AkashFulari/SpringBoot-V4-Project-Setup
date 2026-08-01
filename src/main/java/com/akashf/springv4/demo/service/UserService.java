package com.akashf.springv4.demo.service;

import com.akashf.springv4.demo.dto.ImportUserReq;
import com.akashf.springv4.demo.dto.SetUserReq;
import com.akashf.springv4.demo.model.User;
import com.akashf.springv4.demo.report.users.UserImportExport;
import com.akashf.springv4.demo.repository.UserRepo;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class UserService {
    private final TemplateEngine templateEngine;
    private final UserImportExport excelService;
    private final UserRepo repo;

    public UserService(UserRepo repo, TemplateEngine templateEngine, UserImportExport excelService) {
        this.repo = repo;
        this.excelService = excelService;
        this.templateEngine = templateEngine;
    }

    public User createUser(SetUserReq user) throws Exception {
        return setUser(user, new User());
    }

    public User updateUser(Long id, SetUserReq user) throws Exception {
        User u = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return setUser(user, u);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public User getUserById(Long id) throws Exception {
        User u = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        String avt = u.getAvatar();
        if (Helper.isValid(avt)) {
            System.out.println(avt);
            u.setAvatar(Helper.GetURL(avt));
        }
        return u;
    }

    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    private User setUser(SetUserReq user, User u) throws Exception {
        u.setName(user.getName());
        u.setEmail(user.getEmail());
        MultipartFile file = user.getAvatar();
        if (Helper.isNN(file)) {
            String oldPath = u.getAvatar();

            String uploadDir = "asstes/users/profile/";
            String filePath = Helper.UploadTo(file, uploadDir);
            u.setAvatar(filePath);

            if (Helper.isValid(oldPath)) {
                Helper.DelTo(oldPath);
            }
        }

        return repo.save(u);
    }

    public byte[] userRawReportPdf(Long userId) {
        try {
            User u = getUserById(userId);
            Helper.throwIf(!Helper.isValid(u), "RECORD NOT FOUND");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("User Profile"));
            document.add(new Paragraph(""));
            document.add(new Paragraph("Employee Id : " + u.getId()));
            document.add(new Paragraph("Name : " + u.getName()));
            document.add(new Paragraph("Email : " + u.getEmail()));
            document.add(new Paragraph("Department : " + u.getClass()));
            document.close();
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] userTemplateReportPdf(Long userId) {
        try {
            User u = getUserById(userId);
            Helper.throwIf(!Helper.isValid(u), "RECORD NOT FOUND");

            Context context = new Context();
            context.setVariable("user", u);
            String html = templateEngine.process("pdf/temp2", context);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] exportUsersExcel(List<User> users) {
        return excelService.exportExcel(users);
    }

    public byte[] exportUsersCsv(List<User> users) {
        return excelService.exportCsv(users);
    }

    public void importUsersExcel(ImportUserReq req) {
        excelService.importExcel(req.getFile());
    }

    public void importUsersCsv(ImportUserReq req) {
        excelService.importCsv(req.getFile());
    }
}