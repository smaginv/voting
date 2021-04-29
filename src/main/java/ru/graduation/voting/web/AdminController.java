package ru.graduation.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.graduation.voting.model.User;
import ru.graduation.voting.service.UserService;
import ru.graduation.voting.to.UserTo;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.graduation.voting.util.ValidationUtil.assureIdConsistent;
import static ru.graduation.voting.util.ValidationUtil.checkNew;

@RestController
@Tag(name = "Administrator controller")
@RequestMapping(value = AdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    static final String REST_URL = "/api/admin/accounts";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Get user by id")
    public UserTo get(@PathVariable int id) {
        log.info("get user {}", id);
        return userService.getTo(id);
    }

    @GetMapping("/email")
    @Operation(summary = "Get user by email", description = "Get user by email")
    public UserTo getByEmail(@RequestParam String email) {
        log.info("get by email {}", email);
        return userService.getByEmail(email);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Get all users")
    public List<UserTo> getAll() {
        log.info("Get all users");
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user", description = "Delete user")
    public void delete(@PathVariable int id) {
        log.info("delete user {}", id);
        userService.delete(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Create user", description = "Create user")
    public ResponseEntity<User> create(@Valid @RequestBody UserTo userTo) {
        log.info("create {}", userTo);
        checkNew(userTo);
        User created = userService.save(userTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update user", description = "Update user")
    public User update(@PathVariable int id, @Valid @RequestBody UserTo userTo) {
        log.info("update {}", userTo);
        UserTo oldUser = userService.getTo(id);
        assureIdConsistent(userTo, oldUser.id());
        if (userTo.getPassword() == null) {
            userTo.setPassword(oldUser.getPassword());
        }
        return userService.update(userTo);
    }
}
