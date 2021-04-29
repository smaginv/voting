package ru.graduation.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.graduation.voting.model.Vote;
import ru.graduation.voting.service.VoteService;
import ru.graduation.voting.util.exception.NotFoundException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.graduation.voting.TestUtil.readFromJson;
import static ru.graduation.voting.TestUtil.userHttpBasic;
import static ru.graduation.voting.data.TestData.*;
import static ru.graduation.voting.data.UserTestData.admin;
import static ru.graduation.voting.data.UserTestData.user;
import static ru.graduation.voting.data.VoteTestData.*;
import static ru.graduation.voting.util.DateTimeUtil.setBeforeEndOfVote;
import static ru.graduation.voting.util.VoteUtil.createTo;
import static ru.graduation.voting.util.VoteUtil.createTos;
import static ru.graduation.voting.util.json.JsonUtil.writeValue;
import static ru.graduation.voting.web.VoteController.REST_URL;

class VoteControllerTest extends AbstractControllerTest {

    private static final String URL = REST_URL + '/';
    private static final String URL_USERS = REST_URL + "/users/{userId}/";

    @Autowired
    private VoteService voteService;

    @Test
    void getTo() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + VOTE_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTo(vote1)));
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + VOTE_ID)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + VOTE_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof NotFoundException));
    }

    @Test
    void getForUserOnDate() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USERS + "on-date", USER_ID)
                .param("date", String.valueOf(EXISTING_DATE_DATABASE))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTo(vote2)));
    }

    @Test
    void getForUserOnDateForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USERS + "on-date", USER_ID)
                .param("date", String.valueOf(EXISTING_DATE_DATABASE))
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getForUserOnDateUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USERS + "on-date", USER_ID)
                .param("date", String.valueOf(EXISTING_DATE_DATABASE)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForUserOnDateNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USERS + "on-date", NOT_FOUND)
                .param("date", String.valueOf(NON_EXISTING_DATE_DATABASE))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof NotFoundException));
    }

    @Test
    void getForUserToday() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USERS + "today", ADMIN_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTo(ADMIN_VOTE_TODAY)));
    }

    @Test
    void getForUserTodayForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USERS + "today", ADMIN_ID)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getForUserTodayUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USERS + "today", ADMIN_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForUserTodayNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USERS + "today", NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof NotFoundException));
    }

    @Test
    void getAllForUser() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USERS + "all", ADMIN_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTos(ALL_ADMIN_VOTES)));
    }

    @Test
    void getAllForUserForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USERS + "all", ADMIN_ID)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllForUserUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USERS + "all", ADMIN_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllForUserEmptyList() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USERS + "all", NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(Collections.emptyList()));
    }

    @Test
    void getAllOnDate() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "on-date")
                .param("date", String.valueOf(EXISTING_DATE_DATABASE))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTos(List.of(vote2, vote1))));
    }

    @Test
    void getAllOnDateForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "on-date")
                .param("date", String.valueOf(EXISTING_DATE_DATABASE))
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllOnDateUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "on-date")
                .param("date", String.valueOf(EXISTING_DATE_DATABASE)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllOnDateEmptyList() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "on-date")
                .param("date", String.valueOf(NON_EXISTING_DATE_DATABASE))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(Collections.emptyList()));
    }

    @Test
    void getAllToday() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "today")
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTos(List.of(USER_VOTE_TODAY, ADMIN_VOTE_TODAY))));
    }

    @Test
    void getAllTodayForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "today")
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllTodayUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "today"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + VOTE_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> voteService.getTo(VOTE_ID));
    }

    @Test
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + VOTE_ID)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + VOTE_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createOrUpdate() throws Exception {
        setBeforeEndOfVote(LocalDateTime.MAX);
        Vote newVote = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(FIVE_RESTAURANT_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(writeValue(newVote)))
                .andExpect(status().isCreated());

        Vote created = readFromJson(action, Vote.class);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_TO_MATCHER.assertMatch(voteService.getTo(newId), createTo(newVote));
    }

    @Test
    void createOrUpdateUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(FIVE_RESTAURANT_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createAfterEndOfVote() throws Exception {
        setBeforeEndOfVote(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(FIVE_RESTAURANT_ID))
                .with(userHttpBasic(user)))
                .andExpect(status().is4xxClientError());
        assertThrows(DateTimeException.class, () -> voteService.save(USER_ID, TWO_RESTAURANT_ID));
    }
}