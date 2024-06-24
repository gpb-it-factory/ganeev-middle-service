package ru.gpb.middle_service.backendMock;

import org.springframework.stereotype.Service;

import java.util.UUID;


public class UtilsService {
    private UtilsService(){};

    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }
}
