package ru.graduation.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.graduation.voting.service.UserService;
import ru.graduation.voting.to.UserTo;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.graduation.voting.data.TestData.USER_ID;
import static ru.graduation.voting.TestUtil.readFromJson;
import static ru.graduation.voting.TestUtil.userHttpBasic;
import static ru.graduation.voting.data.UserTestData.*;
import static ru.graduation.voting.util.UserUtil.createTo;
import static ru.graduation.voting.web.ProfileController.REST_URL;

class ProfileControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(USER_TO_MATCHER.contentJson(createTo(user)));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isNoContent());
        USER_TO_MATCHER.assertMatch(userService.getAll(), createTo(admin));
    }

    @Test
    void deleteUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register() throws Exception {
        UserTo newUser = createTo(getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, "register")))
                .andDo(print())
                .andExpect(status().isCreated());

        UserTo created = readFromJson(action, UserTo.class);
        int newId = created.getId();
        newUser.setId(newId);
        USER_TO_MATCHER.assertMatch(created, newUser);
        USER_TO_MATCHER.assertMatch(userService.getTo(newId), newUser);
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(jsonWithPassword(createTo(getUpdated()), "updated")))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_TO_MATCHER.assertMatch(userService.getTo(USER_ID), createTo(getUpdated()));
    }

    @Test
    void updateUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL))
                .andExpect(status().isUnauthorized());
    }

}