package ru.graduation.voting.util;

import ru.graduation.voting.model.Role;
import ru.graduation.voting.model.User;
import ru.graduation.voting.to.UserTo;

import java.util.List;
import java.util.stream.Collectors;

public class UserUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getEmail(), userTo.getPassword(), userTo.getName(), Role.USER);
    }

    public static User cloneFromTo(UserTo userTo) {
        return new User(userTo.getId(), userTo.getEmail(), userTo.getPassword(), userTo.getName(), Role.USER);
    }

    public static UserTo createTo(User user) {
        return new UserTo(user.getId(), user.getEmail(), user.getPassword(), user.getName());
    }

    public static List<UserTo> createTos(List<User> users) {
        return users.stream().map(UserUtil::createTo).collect(Collectors.toList());
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }
}
