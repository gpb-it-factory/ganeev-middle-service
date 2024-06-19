package ru.gpb.middle_service.backendMock.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserMock {
        private String id;
        private long telegramId;
        private String userName;
}
