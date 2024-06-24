package ru.gpb.middle_service.dto;

import lombok.Data;

@Data
public class CreateUserRequestV2 {
    private long userId;
    private String userName;
}
