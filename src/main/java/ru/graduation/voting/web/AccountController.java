package ru.graduation.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.graduation.voting.AuthUser;
import ru.graduation.voting.model.User;
import ru.graduation.voting.service.UserService;
import ru.graduation.voting.to.UserTo;

import javax.validation.Valid;
import java.net.URI;

import static ru.graduation.voting.util.ValidationUtil.assureIdConsistent;
import static ru.graduation.voting.util.ValidationUtil.checkNew;

@RestController
@Tag(name = "Account controller")
@RequestMapping(value = AccountController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    static final String REST_URL = "/api/account";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get user", description = "Get your details")
    public UserTo get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get user {}", authUser);
        return userService.getTo(authUser.getId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user", description = "Delete your account")
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete {}", authUser);
        userService.delete(authUser.getId());
    }

    @PostMapping(value = "/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Register user", description = "Register user")
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        checkNew(userTo);
        User created = userService.save(userTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update user", description = "Update your details")
    public User update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody UserTo userTo) {
        log.info("update {} to {}", authUser, userTo);
        UserTo oldUser = authUser.getUserTo();
        assureIdConsistent(userTo, oldUser.id());
        if (userTo.getPassword() == null) {
            userTo.setPassword(oldUser.getPassword());
        }
        return userService.update(userTo);
    }
}
