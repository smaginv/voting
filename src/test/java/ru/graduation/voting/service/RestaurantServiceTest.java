package ru.graduation.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.graduation.voting.data.DishTestData;
import ru.graduation.voting.model.Restaurant;
import ru.graduation.voting.to.RestaurantTo;
import ru.graduation.voting.util.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.graduation.voting.data.RestaurantTestData.*;
import static ru.graduation.voting.data.TestData.*;
import static ru.graduation.voting.util.RestaurantUtil.createTo;
import static ru.graduation.voting.util.RestaurantUtil.createTos;

class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService service;

    @Test
    void getTo() {
        RestaurantTo actual = service.getTo(FIVE_RESTAURANT_ID);
        RESTAURANT_TO_MATCHER.assertMatch(actual, createTo(restaurant5));
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.getTo(NOT_FOUND));
    }

    @Test
    void getAll() {
        RESTAURANT_TO_MATCHER.assertMatch(service.getAll(), createTos(ALL_RESTAURANTS));
    }

    @Test
    void delete() {
        service.delete(FOUR_RESTAURANT_ID);
        assertThrows(NotFoundException.class, () -> service.getTo(FOUR_RESTAURANT_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void getWithMenuOnDate() {
        restaurant2.setDishes(DishTestData.DISHES_ON_EXISTS_DATE_TWO);
        RESTAURANT_WITH_DISHES_MATCHER.assertMatch(service.getWithMenuOnDate(
                TWO_RESTAURANT_ID, EXISTING_DATE_DATABASE), restaurant2);
    }

    @Test
    void getWithMenuOnDateNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMenuOnDate(
                NOT_FOUND, NON_EXISTING_DATE_DATABASE));
    }

    @Test
    void getWithMenuToday() {
        restaurant4.setDishes(DishTestData.DISHES_TODAY_FOUR);
        RESTAURANT_WITH_DISHES_MATCHER.assertMatch(service.getWithMenuToday(FOUR_RESTAURANT_ID), restaurant4);
    }

    @Test
    void getWithMenuTodayNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMenuToday(NOT_FOUND));
    }

    @Test
    void getAllWithMenuOnDate() {
        RESTAURANT_MATCHER.assertMatch(service.getAllWithMenuOnDate(EXISTING_DATE_DATABASE),
                List.of(restaurant2, restaurant1));
    }

    @Test
    void getAllWithMenuOnDateEmptyList() {
        RESTAURANT_MATCHER.assertMatch(service.getAllWithMenuOnDate(NON_EXISTING_DATE_DATABASE),
                Collections.emptyList());
    }

    @Test
    void getAllWithMenuToday() {
        RESTAURANT_MATCHER.assertMatch(service.getAllWithMenuToday(), List.of(restaurant5, restaurant4));
    }

    @Test
    void getAllWithMenuTodayNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMenuToday(NOT_FOUND));
    }

    @Test
    void create() {
        Restaurant created = service.save(createTo(getNew()));
        int newId = created.id();
        Restaurant newRestaurant = getNew();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_TO_MATCHER.assertMatch(service.getTo(newId), createTo(newRestaurant));
    }

    @Test
    void update() {
        service.update(createTo(getUpdated()));
        RESTAURANT_TO_MATCHER.assertMatch(service.getTo(ONE_RESTAURANT_ID), createTo(getUpdated()));
    }
}