package com.firstest.demo.dto;

import lombok.Data;

@Data
public class PaginReq {
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String sortDirection = "ASC";
    private String search;
    private Long userId;
}