package ru.gpb.middle_service.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorV2 {
    private String message;
    private String type;
    private String code;
    private String traceId;
}
