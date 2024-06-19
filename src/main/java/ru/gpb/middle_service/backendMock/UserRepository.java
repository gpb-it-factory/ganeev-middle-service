package ru.gpb.middle_service.backendMock;

import org.springframework.stereotype.Repository;
import ru.gpb.middle_service.backendMock.entity.UserMock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class UserRepository {
    private final List<UserMock> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }
    public void save(UserMock userMock){
        users.add(userMock);
    }

    public Optional<UserMock> findByTelegramId(long telegramId){
        return users.stream()
                .filter(user->user.getTelegramId()==telegramId)
                .findAny();
    };

    public Optional<UserMock> findByUserName(String userName){
        return users.stream()
                .filter(user-> userName.equals(user.getUserName()))
                .findAny();
    };

}
