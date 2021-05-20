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
    public Vote get(@PathVariable int id) {
        log.info("get vote {} ", id);
        return voteService.get(id);
    }

    @GetMapping("/today")
    @Operation(summary = "Get all today votes", description = "Get all today votes")
    public List<Vote> getAllToday() {
        log.info("get all vote's today");
        return voteService.getAllToday();
    }

    @GetMapping("/on-date")
    @Operation(summary = "Get all votes on date", description = "Get all votes on date")
    public List<Vote> getAllOnDate(@RequestParam @Nullable LocalDate date) {
        log.info("get all vote's on date {}", date);
        return voteService.getAllOnDate(date);
    }

    @PostMapping
    @Operation(summary = "to vote", description = "to vote for restaurant")
    public ResponseEntity<Vote> create(@AuthenticationPrincipal AuthUser authUser,
                                       @RequestParam int restaurantId) {
        log.info("create vote");
        Vote created = voteService.save(authUser.getId(), restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "re-vote", description = "re-vote, only before 11:00")
    public void update(@AuthenticationPrincipal AuthUser authUser,
                       @RequestParam int restaurantId) {
        log.info("update vote for user{}", authUser);
        voteService.update(authUser.getId(), restaurantId);
    }
}
