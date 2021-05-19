package ru.graduation.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.graduation.voting.model.Vote;
import ru.graduation.voting.service.VoteService;

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
import static ru.graduation.voting.TestUtil.userHttpBasic;
import static ru.graduation.voting.data.TestData.*;
import static ru.graduation.voting.data.UserTestData.admin;
import static ru.graduation.voting.data.UserTestData.user;
import static ru.graduation.voting.data.VoteTestData.*;
import static ru.graduation.voting.util.DateTimeUtil.setBeforeEndOfVote;
import static ru.graduation.voting.util.json.JsonUtil.writeValue;
import static ru.graduation.voting.web.VoteController.REST_URL;

class VoteControllerTest extends AbstractControllerTest {

    private static final String URL = REST_URL + '/';

    @Autowired
    private VoteService voteService;

    @Test
    void getAllOnDate() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "on-date")
                .param("date", String.valueOf(EXISTING_DATE_DATABASE))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(List.of(vote2, vote1)));
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
                .andExpect(VOTE_MATCHER.contentJson(Collections.emptyList()));
    }

    @Test
    void getAllToday() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "today")
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(List.of(USER_VOTE_TODAY, ADMIN_VOTE_TODAY)));
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
    void update() throws Exception {
        setBeforeEndOfVote(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", String.valueOf(FIVE_RESTAURANT_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(writeValue(updated)))
                .andExpect(status().isNoContent());

        VOTE_MATCHER.assertMatch(voteService.get(VOTE_ID), updated);
    }

    @Test
    void updateUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(FIVE_RESTAURANT_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateAfterEndOfVote() throws Exception {
        setBeforeEndOfVote(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", String.valueOf(FIVE_RESTAURANT_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(writeValue(updated)))
                .andExpect(status().is5xxServerError())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof DateTimeException));
    }

    @Test
    void createUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(FIVE_RESTAURANT_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}