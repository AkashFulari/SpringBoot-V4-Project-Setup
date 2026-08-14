package com.akashf.springv4.demo.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.akashf.springv4.demo.dto.NotifyReq;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.web.multipart.MultipartFile;

public class Helper {

    /**
     * Print Constole Data.
     **/
    public static <T> void o(T... params) {
        for (T a : params) {
            System.out.print(a);
        }
    }

    /**
     * Print Constole Data.
     **/
    public static <T> void on(T... params) {
        for (T a : params) {
            System.out.println(a);
        }
    }

    /**
     * Print Constole Data.
     **/
    public static String strPluk(String... params) {
        return Arrays.stream(params).collect(Collectors.joining());
    }

    /**
     * This method validates the is not null.
     **/
    public static <T> boolean isNN(T obj) {
        return obj != null;
    }

    public static <T> boolean isValid(T obj) {
        if (obj instanceof String) {
            return isNN(obj) && !((String) obj).isEmpty();
        }
        return isNN(obj);
    }

    public static <T> void throwIf(boolean isValid, T msg) throws Exception {
        if (isValid) {
            String message = "";
            if (msg instanceof Throwable)
                message = ((Throwable) msg).getMessage();
            else if (msg instanceof String)
                message = (String) msg;
            else
                message = msg.toString();
            throw new Exception(message);
        }
    }

    public static String GetURL(String filePath) throws Exception {
        return GetURL(filePath, false);
    }

    public static String GetURL(String filePath, boolean canThrowError) throws Exception {
        if (isValid(filePath)) {
            File f = new File(filePath);
            throwIf(!f.exists() && canThrowError, "INVALID FILE PATH");
            return ServletUriComponentsBuilder.fromCurrentContextPath().path(filePath).toUriString();
        }
        return "";
    }

    public static String UploadTo(MultipartFile f, String dest) throws Exception {
        String filePath = dest + f.getOriginalFilename();
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.write(path, f.getBytes());
        return filePath;
    }

    public static void DelTo(String dest) throws Exception {
        File file = new File(dest);
        if (!file.exists())
            return;
        file.delete();
    }

    public static String getCellValue(Cell cell) {
        if (!isNN(cell)) {
            return "";
        }

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();

            case NUMERIC -> String.valueOf(cell.getNumericCellValue());

            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    public static String notifyFCM(NotifyReq req) {
        // Build the basic visual alert
        Notification notification = Notification.builder()
                .setTitle(req.getTitle())
                .setBody(req.getBody())
                .build();
        // Build the message targeting a specific device token
        Message message = Message.builder()
                .setToken(req.getTargetToken())
                .setNotification(notification)
                .build();
        try {
            // Send the message synchronously
            return FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            return "Error sending notification: " + e.getMessage();
        }
    }
}
