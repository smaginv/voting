package ru.graduation.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.graduation.voting.data.DishTestData;
import ru.graduation.voting.service.RestaurantService;
import ru.graduation.voting.to.RestaurantTo;
import ru.graduation.voting.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.graduation.voting.TestUtil.readFromJson;
import static ru.graduation.voting.TestUtil.userHttpBasic;
import static ru.graduation.voting.data.RestaurantTestData.*;
import static ru.graduation.voting.data.TestData.*;
import static ru.graduation.voting.data.UserTestData.admin;
import static ru.graduation.voting.data.UserTestData.user;
import static ru.graduation.voting.util.RestaurantUtil.createTo;
import static ru.graduation.voting.util.RestaurantUtil.createTos;
import static ru.graduation.voting.util.json.JsonUtil.writeValue;

class RestaurantControllerTest extends AbstractControllerTest {

    private static final String URL = RestaurantController.REST_URL + '/';

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + FIVE_RESTAURANT_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(createTo(restaurant5)));
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + FIVE_RESTAURANT_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + THREE_RESTAURANT_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof NotFoundException));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(createTos(ALL_RESTAURANTS)));
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
    void getWithMenuOnDate() throws Exception {
        restaurant2.setDishes(DishTestData.DISHES_ON_EXISTS_DATE_TWO);
        perform(MockMvcRequestBuilders.get(URL + TWO_RESTAURANT_ID + "/menu-on-date")
                .param("date", String.valueOf(EXISTING_DATE_DATABASE))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(RESTAURANT_WITH_DISHES_MATCHER.contentJson(restaurant2));
    }

    @Test
    void getWithMenuOnDateForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + TWO_RESTAURANT_ID + "/menu-on-date")
                .param("date", String.valueOf(EXISTING_DATE_DATABASE))
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getWithMenuOnDateUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + TWO_RESTAURANT_ID + "/menu-on-date")
                .param("date", String.valueOf(EXISTING_DATE_DATABASE)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getWithMenuToday() throws Exception {
        restaurant4.setDishes(DishTestData.DISHES_TODAY_FOUR);
        perform(MockMvcRequestBuilders.get(URL + FOUR_RESTAURANT_ID + "/menu-today")
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(RESTAURANT_WITH_DISHES_MATCHER.contentJson(restaurant4));
    }

    @Test
    void getWithMenuTodayForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + THREE_RESTAURANT_ID + "/menu-today")
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getWithMenuTodayUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + THREE_RESTAURANT_ID + "/menu-today"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllWithMenuOnDate() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "menu-on-date")
                .param("date", String.valueOf(EXISTING_DATE_DATABASE))
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(RESTAURANT_MATCHER.contentJson(List.of(restaurant2, restaurant1)));

    }

    @Test
    void getAllWithMenuOnDateUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "menu-on-date")
                .param("date", String.valueOf(EXISTING_DATE_DATABASE)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    void getAllWithMenuToday() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "menu-today")
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(RESTAURANT_MATCHER.contentJson(List.of(restaurant5, restaurant4)));
    }

    @Test
    void getAllWithMenuUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "menu-today"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + ONE_RESTAURANT_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restaurantService.getTo(ONE_RESTAURANT_ID));
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
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + THREE_RESTAURANT_ID)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + FIVE_RESTAURANT_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void create() throws Exception {
        RestaurantTo newRestaurant = createTo(getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(writeValue(newRestaurant)))
                .andExpect(status().isCreated());

        RestaurantTo created = readFromJson(action, RestaurantTo.class);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_TO_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_TO_MATCHER.assertMatch(restaurantService.getTo(newId), newRestaurant);
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
        RestaurantTo updated = createTo(getUpdated());
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(URL + ONE_RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        RESTAURANT_TO_MATCHER.assertMatch(restaurantService.getTo(ONE_RESTAURANT_ID), createTo(getUpdated()));
    }

    @Test
    void updateForbidden() throws Exception {
        perform(MockMvcRequestBuilders.put(URL + ONE_RESTAURANT_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.put(URL + ONE_RESTAURANT_ID))
                .andExpect(status().isUnauthorized());
    }
}