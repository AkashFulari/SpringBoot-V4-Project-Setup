package com.firstest.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.firstest.demo.responce.ApiResp;
import com.firstest.demo.responce.PaginResp;

public class Resp {

    public static <T> ResponseEntity<ApiResp<T>> sucess(String msg, T d) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResp.<T>builder().success(true).message(msg).info(d).build());
    }

    public static <T> ResponseEntity<ApiResp<T>> sucess(String msg, T d, HttpStatus statusCode) {
        return ResponseEntity.status(statusCode)
                .body(ApiResp.<T>builder().success(true).message(msg).info(d).build());
    }

    public static <T> ResponseEntity<ApiResp<T>> sucess(String msg, List<T> items) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResp.<T>builder().success(true).message(msg).items(items).build());
    }

    public static <T> ResponseEntity<ApiResp<T>> sucess(String msg, Page<T> page) {
        PaginResp pagin = PaginResp.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResp.<T>builder().success(true).message(msg).items(page.getContent()).pagination(pagin).build());
    }

    public static <T> ResponseEntity<ApiResp<T>> sucess(String msg, List<T> items, HttpStatus statusCode) {
        return ResponseEntity.status(statusCode)
                .body(ApiResp.<T>builder().success(true).message(msg).items(items).build());
    }

    public static <T> ResponseEntity<ApiResp<T>> sucess(String msg, Page<T> page, HttpStatus statusCode) {
        PaginResp pagin = PaginResp.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
        return ResponseEntity.status(statusCode)
                .body(ApiResp.<T>builder().success(true).message(msg).items(page.getContent()).pagination(pagin).build());
    }

    public static <T> ResponseEntity<ApiResp<T>> sucess(String msg) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResp.<T>builder().success(true).message(msg).build());
    }

    public static <T> ResponseEntity<ApiResp<T>> sucess(String msg, HttpStatus statusCode) {
        return ResponseEntity.status(statusCode)
                .body(ApiResp.<T>builder().success(true).message(msg).build());
    }

    public static <T> ResponseEntity<ApiResp<T>> error(String msg) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(ApiResp.<T>builder().success(false).message(msg).build());
    }

    public static <T> ResponseEntity<ApiResp<T>> error(String msg, HttpStatus statusCode) {
        return ResponseEntity.status(statusCode)
                .body(ApiResp.<T>builder().success(false).message(msg).build());
    }
}
