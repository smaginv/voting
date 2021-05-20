package ru.graduation.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.graduation.voting.data.UserTestData;
import ru.graduation.voting.model.Vote;
import ru.graduation.voting.to.UserTo;
import ru.graduation.voting.util.UserUtil;
import ru.graduation.voting.util.exception.NotFoundException;

import java.time.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.graduation.voting.data.TestData.*;
import static ru.graduation.voting.data.VoteTestData.*;
import static ru.graduation.voting.util.DateTimeUtil.setBeforeEndOfVote;


class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserService userService;

    @Test
    void get() {
        VOTE_MATCHER.assertMatch(voteService.get(VOTE_ID), vote1);
    }

    @Test
    void getAllOnDate() {
        VOTE_MATCHER.assertMatch(voteService.getAllOnDate(EXISTING_DATE_DATABASE), List.of(vote2, vote1));
    }

    @Test
    void getAllOnDateEmptyList() {
        VOTE_MATCHER.assertMatch(voteService.getAllOnDate(NON_EXISTING_DATE_DATABASE), Collections.emptyList());
    }

    @Test
    void getAllToday() {
        VOTE_MATCHER.assertMatch(voteService.getAllToday(), List.of(USER_VOTE_TODAY, ADMIN_VOTE_TODAY));
    }

    @Test
    void create() {
        UserTo user = userService.save(UserUtil.createTo(UserTestData.getNew()));
        Vote created = voteService.save(user.getId(), ONE_RESTAURANT_ID);
        int newId = created.id();
        Vote newVote = getNew();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
    }

    @Test
    void update() {
        setBeforeEndOfVote(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        VOTE_MATCHER.assertMatch(voteService.get(VOTE_ID), getUpdated());
    }

    @Test
    void updateNotFound() {
        assertThrows(NotFoundException.class, () -> voteService.update(NOT_FOUND, NOT_FOUND));
    }

    @Test
    void updateAfterEndOfVote() {
        setBeforeEndOfVote(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        assertThrows(DateTimeException.class, () -> voteService.update(USER_ID, TWO_RESTAURANT_ID));
    }
}