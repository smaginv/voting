package ru.graduation.voting;

import org.springframework.lang.NonNull;
import ru.graduation.voting.model.User;
import ru.graduation.voting.to.UserTo;
import ru.graduation.voting.util.UserUtil;

public class AuthUser extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 1L;

    private UserTo userTo;

    public AuthUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        setTo(UserUtil.createTo(user));
    }

    public int getId() {
        return userTo.id();
    }

    public void setTo(UserTo newTo) {
        newTo.setPassword(null);
        userTo = newTo;
    }

    public UserTo getUserTo() {
        return userTo;
    }

    @Override
    public String toString() {
        return userTo.toString();
    }
}