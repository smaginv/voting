package ru.graduation.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.graduation.voting.model.Dish;
import ru.graduation.voting.service.DishService;
import ru.graduation.voting.util.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.graduation.voting.TestUtil.readFromJson;
import static ru.graduation.voting.TestUtil.userHttpBasic;
import static ru.graduation.voting.data.DishTestData.*;
import static ru.graduation.voting.data.TestData.*;
import static ru.graduation.voting.data.UserTestData.admin;
import static ru.graduation.voting.data.UserTestData.user;
import static ru.graduation.voting.util.json.JsonUtil.writeValue;


class DishControllerTest extends AbstractControllerTest {

    private static final String URL = DishController.REST_URL + '/';

    @Autowired
    private DishService dishService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + DISH_ID, ONE_RESTAURANT_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1));
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + DISH_ID, ONE_RESTAURANT_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + DISH_ID, ONE_RESTAURANT_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + NOT_FOUND, ONE_RESTAURANT_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof NotFoundException));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL, TWO_RESTAURANT_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(DISH_MATCHER.contentJson(DISHES_TWO));
    }

    @Test
    void getAllForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL, TWO_RESTAURANT_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL, TWO_RESTAURANT_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllOnDate() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL + "on-date/?date=" + EXISTING_DATE_DATABASE, ONE_RESTAURANT_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(DISH_MATCHER.contentJson(DISHES_ON_EXISTS_DATE_ONE));
    }

    @Test
    void getAllOnDateForbidden() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL + "on-date/?date=" + EXISTING_DATE_DATABASE, ONE_RESTAURANT_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllOnDateUnAuth() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL + "on-date/?date=" + EXISTING_DATE_DATABASE, ONE_RESTAURANT_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllToday() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "today", FOUR_RESTAURANT_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(DISH_MATCHER.contentJson(DISHES_TODAY_FOUR));
    }

    @Test
    void getAllTodayForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "today", THREE_RESTAURANT_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllTodayUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "today", THREE_RESTAURANT_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + DISH_ID, ONE_RESTAURANT_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> dishService.get(DISH_ID, ONE_RESTAURANT_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + NOT_FOUND, ONE_RESTAURANT_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof NotFoundException));
    }

    @Test
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + NOT_FOUND, ONE_RESTAURANT_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteNotUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + NOT_FOUND, ONE_RESTAURANT_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void create() throws Exception {
        Dish newDish = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(URL, ONE_RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(writeValue(newDish)))
                .andExpect(status().isCreated());

        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishService.get(newId, ONE_RESTAURANT_ID), newDish);
    }

    @Test
    void createForbidden() throws Exception {
        perform(MockMvcRequestBuilders.post(URL, ONE_RESTAURANT_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.post(URL, ONE_RESTAURANT_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(URL + DISH_ID, ONE_RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(dishService.get(DISH_ID, ONE_RESTAURANT_ID), getUpdated());
    }

    @Test
    void updateForbidden() throws Exception {
        perform(MockMvcRequestBuilders.put(URL + DISH_ID, ONE_RESTAURANT_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.put(URL + DISH_ID, ONE_RESTAURANT_ID))
                .andExpect(status().isUnauthorized());
    }
}