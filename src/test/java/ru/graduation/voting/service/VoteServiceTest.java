package ru.graduation.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.graduation.voting.model.Vote;
import ru.graduation.voting.util.exception.NotFoundException;

import java.time.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.graduation.voting.data.TestData.*;
import static ru.graduation.voting.data.VoteTestData.*;
import static ru.graduation.voting.util.DateTimeUtil.setBeforeEndOfVote;
import static ru.graduation.voting.util.VoteUtil.*;


class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    private VoteService service;

    @Test
    void getTo() {
        VOTE_TO_MATCHER.assertMatch(service.getTo(VOTE_ID), createToWithoutRestaurant(vote1));
    }

    @Test
    void getForUserOnDate() {
        VOTE_TO_MATCHER.assertMatch(service.getForUserOnDate(USER_ID, EXISTING_DATE_DATABASE),
                createToWithoutRestaurant(vote2));
    }

    @Test
    void getForUserOnDateNotFound() {
        assertThrows(NotFoundException.class, () -> service.getForUserOnDate(NOT_FOUND, EXISTING_DATE_DATABASE));
    }

    @Test
    void getForUserToday() {
        VOTE_TO_MATCHER.assertMatch(service.getForUserToday(ADMIN_ID), createToWithoutRestaurant(ADMIN_VOTE_TODAY));
        VOTE_TO_MATCHER.assertMatch(service.getForUserToday(USER_ID), createToWithoutRestaurant(USER_VOTE_TODAY));
    }

    @Test
    void getForUserTodayNotFound() {
        assertThrows(NotFoundException.class, () -> service.getForUserToday(NOT_FOUND));
    }

    @Test
    void getAllForUser() {
        VOTE_TO_MATCHER.assertMatch(service.getAllForUser(ADMIN_ID), createTosWithoutRestaurant(ALL_ADMIN_VOTES));
        VOTE_TO_MATCHER.assertMatch(service.getAllForUser(USER_ID), createTosWithoutRestaurant(ALL_USER_VOTES));
    }

    @Test
    void getAllForUserEmptyList() {
        VOTE_TO_MATCHER.assertMatch(service.getAllForUser(NOT_FOUND), Collections.emptyList());
    }

    @Test
    void getAllOnDate() {
        VOTE_TO_MATCHER.assertMatch(service.getAllOnDate(EXISTING_DATE_DATABASE), createTos(List.of(vote2, vote1)));
    }

    @Test
    void getAllOnDateEmptyList() {
        VOTE_TO_MATCHER.assertMatch(service.getAllOnDate(NON_EXISTING_DATE_DATABASE), Collections.emptyList());
    }

    @Test
    void getAllToday() {
        VOTE_TO_MATCHER.assertMatch(service.getAllToday(), createTos(List.of(USER_VOTE_TODAY, ADMIN_VOTE_TODAY)));
    }

    @Test
    void delete() {
        service.delete(VOTE_ID);
        assertThrows(NotFoundException.class, () -> service.getTo(VOTE_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void create() {
        setBeforeEndOfVote(LocalDateTime.MAX);
        Vote created = service.save(ADMIN_ID, ONE_RESTAURANT_ID);
        int newId = created.id();
        Vote newVote = getNew();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_TO_MATCHER.assertMatch(service.getTo(newId), createTo(newVote));
    }

    @Test
    void createAfterEndOfVote() {
        setBeforeEndOfVote(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
        assertThrows(DateTimeException.class, () -> service.save(USER_ID, TWO_RESTAURANT_ID));
    }
}