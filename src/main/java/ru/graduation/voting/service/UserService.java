package ru.graduation.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.graduation.voting.model.User;
import ru.graduation.voting.repository.UserRepository;
import ru.graduation.voting.to.UserTo;
import ru.graduation.voting.util.exception.NotFoundException;

import java.util.List;

import static ru.graduation.voting.util.UserUtil.*;
import static ru.graduation.voting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserTo getTo(int id) {
        return createTo(get(id));
    }

    private User get(int id) {
        return userRepository.get(id).orElseThrow(
                () -> new NotFoundException("Not found user with id: " + id)
        );
    }

    public UserTo getByEmail(String email) {
        User user = userRepository.getByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User '" + email + "' was not found")
        );
        return createTo(user);
    }

    @Cacheable("users")
    public List<UserTo> getAll() {
        return createTos(userRepository.getAll());
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(userRepository.delete(id) != 0, id);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User save(UserTo userTo) {
        Assert.notNull(userTo, "user must not be null");
        return userRepository.save(createNewFromTo(userTo));
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User update(UserTo userTo) {
        Assert.notNull(userTo, "user must not be null");
        return userRepository.save(updateFromTo(get(userTo.id()), userTo));
    }
}
