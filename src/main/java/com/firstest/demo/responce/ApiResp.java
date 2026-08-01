package com.firstest.demo.responce;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResp<T> {

    private boolean success;

    private String message;

    private T info;

    private List<T> items;

    private Object errors;

    private PaginResp pagination;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}