package ru.graduation.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.graduation.voting.service.UserService;
import ru.graduation.voting.to.UserTo;
import ru.graduation.voting.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.graduation.voting.TestUtil.readFromJson;
import static ru.graduation.voting.TestUtil.userHttpBasic;
import static ru.graduation.voting.data.TestData.NOT_FOUND;
import static ru.graduation.voting.data.TestData.USER_ID;
import static ru.graduation.voting.data.UserTestData.*;
import static ru.graduation.voting.util.UserUtil.createTo;
import static ru.graduation.voting.util.UserUtil.createTos;

class AdminControllerTest extends AbstractControllerTest {

    private static final String URL = AdminController.REST_URL + '/';

    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + USER_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(USER_TO_MATCHER.contentJson(createTo(user)));
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + USER_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + USER_ID))
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
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "email?email=" + user.getEmail())
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(USER_TO_MATCHER.contentJson(createTo(user)));
    }

    @Test
    void getByEmailForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL)
                .param("email", admin.getEmail())
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getByEmailUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL)
                .param("email", user.getEmail()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(USER_TO_MATCHER.contentJson(createTos(List.of(admin, user))));
    }

    @Test
    void getAllForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + USER_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> userService.getTo(USER_ID));
    }

    @Test
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + USER_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + USER_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof NotFoundException));
    }

    @Test
    void create() throws Exception {
        UserTo newUser = createTo(getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(newUser, "created")))
                .andExpect(status().isCreated());

        UserTo created = readFromJson(action, UserTo.class);
        int newId = created.id();
        newUser.setId(newId);
        USER_TO_MATCHER.assertMatch(created, newUser);
        USER_TO_MATCHER.assertMatch(userService.getTo(newId), newUser);
    }

    @Test
    void createForbidden() throws Exception {
        perform(MockMvcRequestBuilders.post(URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.post(URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update() throws Exception {
        UserTo updated = createTo(getUpdated());
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(updated, "updated")))
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_TO_MATCHER.assertMatch(userService.getTo(USER_ID), createTo(getUpdated()));
    }

    @Test
    void updateForbidden() throws Exception {
        perform(MockMvcRequestBuilders.put(URL + USER_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.put(URL + USER_ID))
                .andExpect(status().isUnauthorized());
    }
}