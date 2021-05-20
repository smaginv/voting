package ru.graduation.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.graduation.voting.model.User;
import ru.graduation.voting.to.UserTo;
import ru.graduation.voting.util.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.graduation.voting.data.TestData.*;
import static ru.graduation.voting.data.UserTestData.*;
import static ru.graduation.voting.util.UserUtil.*;

class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private UserService service;

    @Test
    void getTo() {
        USER_TO_MATCHER.assertMatch(service.getTo(USER_ID), createTo(user));
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.getTo(NOT_FOUND));
    }

    @Test
    void getByEmail() {
        USER_TO_MATCHER.assertMatch(service.getByEmail("user@mail.ru"), createTo(user));
    }

    @Test
    void getByEmailNotFound() {
        assertThrows(UsernameNotFoundException.class, () -> service.getByEmail("unknown@mail.ru"));
    }

    @Test
    void getAll() {
        USER_TO_MATCHER.assertMatch(service.getAll(), createTos(ALL_USERS));
    }

    @Test
    void delete() {
        service.delete(ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.getTo(ADMIN_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void create() {
        UserTo created = service.save(createTo(getNew()));
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(cloneFromTo(created), newUser);
        USER_TO_MATCHER.assertMatch(service.getTo(newId), createTo(newUser));
    }

    @Test
    void update() {
        service.update(createTo(getUpdated()));
        USER_TO_MATCHER.assertMatch(service.getTo(USER_ID), createTo(getUpdated()));
    }
}