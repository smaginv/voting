package ru.graduation.voting.data;

import ru.graduation.voting.TestMatcher;
import ru.graduation.voting.model.Role;
import ru.graduation.voting.model.User;
import ru.graduation.voting.to.UserTo;
import ru.graduation.voting.util.json.JsonUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static ru.graduation.voting.data.TestData.*;

public class UserTestData {
    public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "registered", "password");
    public static final TestMatcher<UserTo> USER_TO_MATCHER = TestMatcher.usingIgnoringFieldsComparator(UserTo.class, "password");

    public static final User admin = new User(ADMIN_ID, "admin@mail.ru", "admin", "admin", new Date(), Collections.singleton(Role.ADMIN));
    public static final User user = new User(USER_ID, "user@mail.ru", "user", "user", new Date(), Collections.singleton(Role.USER));

    public static final List<User> ALL_USERS = List.of(admin, user);

    public static User getNew() {
        return new User(null, "new@mail.ru", "register", "newUser", new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(user);
        updated.setEmail("update@mail.ru");
        updated.setPassword("update");
        updated.setName("update");
        updated.setRoles(Collections.singleton(Role.USER));
        return updated;
    }

    public static String jsonWithPassword(UserTo userTo, String passw) {
        return JsonUtil.writeAdditionProps(userTo, "password", passw);
    }
}
