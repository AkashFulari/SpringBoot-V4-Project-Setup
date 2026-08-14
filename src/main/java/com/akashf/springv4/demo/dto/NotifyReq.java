package com.akashf.springv4.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifyReq {
    private String targetToken; // Client's FCM token
    private String title;
    private String body;
}
