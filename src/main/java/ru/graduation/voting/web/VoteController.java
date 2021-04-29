package ru.graduation.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.graduation.voting.AuthUser;
import ru.graduation.voting.model.Vote;
import ru.graduation.voting.service.VoteService;
import ru.graduation.voting.to.VoteTo;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Vote Controller")
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    static final String REST_URL = "/api/votes";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vote by id", description = "Get vote by id")
    public VoteTo get(@PathVariable int id) {
        log.info("get vote {} ", id);
        return voteService.getTo(id);
    }

    @GetMapping("/users/{userId}/on-date")
    @Operation(summary = "Get user vote on date", description = "Get user vote by user id on date")
    public VoteTo getForUserOnDate(@PathVariable int userId, @RequestParam @Nullable LocalDate date) {
        log.info("get user's {} vote for date {}", userId, date);
        return voteService.getForUserOnDate(userId, date);
    }

    @GetMapping("/users/{userId}/today")
    @Operation(summary = "Get user's vote today", description = "Get user's vote today by user id")
    public VoteTo getForUserToday(@PathVariable int userId) {
        log.info("get user's {} vote today", userId);
        return voteService.getForUserToday(userId);
    }

    @GetMapping("/users/{userId}/all")
    @Operation(summary = "Get all user votes", description = "Get user votes by user id")
    public List<VoteTo> getAllForUser(@PathVariable int userId) {
        log.info("get all vote's for user {}", userId);
        return voteService.getAllForUser(userId);
    }

    @GetMapping("/on-date")
    @Operation(summary = "Get all votes on date", description = "Get all votes on date")
    public List<VoteTo> getAllOnDate(@RequestParam @Nullable LocalDate date) {
        log.info("get all vote's on date {}", date);
        return voteService.getAllOnDate(date);
    }

    @GetMapping("/today")
    @Operation(summary = "Get all today votes", description = "Get all today votes")
    public List<VoteTo> getAllToday() {
        log.info("get all vote's today");
        return voteService.getAllToday();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vote", description = "Delete vote by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete vote {}", id);
        voteService.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "To vote", description = "To vote, only before 11:00")
    public ResponseEntity<Vote> createOrUpdate(@AuthenticationPrincipal AuthUser authUser,
                                               @RequestParam int restaurantId) {
        log.info("create or update vote");
        Vote created = voteService.save(authUser.getId(), restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{restaurantId}/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
