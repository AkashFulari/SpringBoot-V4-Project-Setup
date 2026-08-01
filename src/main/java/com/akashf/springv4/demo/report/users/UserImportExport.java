package com.akashf.springv4.demo.report.users;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.akashf.springv4.demo.model.User;
import com.akashf.springv4.demo.repository.UserRepo;
import com.akashf.springv4.demo.service.Helper;

@Service
public class UserImportExport {
    private UserRepo repo;

    public UserImportExport(UserRepo repo) {
        this.repo = repo;
    }

    public byte[] exportExcel(List<User> users) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Users");

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Font
            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            // Alignment
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            Row header = sheet.createRow(0);
            Cell c0 = header.createCell(0);
            c0.setCellValue("Id");
            c0.setCellStyle(headerStyle);
            Cell c1 = header.createCell(1);
            c1.setCellValue("Name");
            c1.setCellStyle(headerStyle);
            Cell c2 = header.createCell(2);
            c2.setCellValue("Email");
            c2.setCellStyle(headerStyle);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setAlignment(HorizontalAlignment.LEFT);

            int rowNumber = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNumber++);
                Cell dc0 = row.createCell(0, CellType.NUMERIC);
                dc0.setCellValue(user.getId());
                dc0.setCellStyle(dataStyle);
                Cell dc1 = row.createCell(1, CellType.STRING);
                dc1.setCellValue(user.getName());
                dc1.setCellStyle(dataStyle);
                Cell dc2 = row.createCell(2, CellType.STRING);
                dc2.setCellValue(user.getEmail());
                dc2.setCellStyle(dataStyle);
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public void importExcel(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream());) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row == null)
                    continue;
                User user = new User();
                user.setName(Helper.getCellValue(row.getCell(0)));
                user.setEmail(Helper.getCellValue(row.getCell(1)));
                repo.save(user);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] exportCsv(List<User> users) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVPrinter csv = new CSVPrinter(new OutputStreamWriter(out, StandardCharsets.UTF_8),
                    CSVFormat.DEFAULT.builder().setHeader("Id", "Name", "Email").build());

            for (User u : users)
                csv.printRecord(u.getId(), u.getName(), u.getEmail());

            csv.flush();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void importCsv(MultipartFile file) {
        try {
            CSVParser parser = CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

            for (CSVRecord row : parser) {
                User user = new User();
                user.setName(row.get("Name"));
                user.setEmail(row.get("Email"));

                repo.save(user);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}