package ru.graduation.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.graduation.voting.model.Dish;
import ru.graduation.voting.util.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.graduation.voting.data.DishTestData.*;
import static ru.graduation.voting.data.TestData.*;

class DishServiceTest extends AbstractServiceTest {

    @Autowired
    private DishService service;

    @Test
    void get() {
        Dish actual = service.get(DISH_ID, ONE_RESTAURANT_ID);
        DISH_MATCHER.assertMatch(actual, dish1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, TWO_RESTAURANT_ID));
    }

    @Test
    void getAll() {
        DISH_MATCHER.assertMatch(service.getAll(ONE_RESTAURANT_ID), DISHES_ONE);
    }

    @Test
    void getAllEmptyList() {
        DISH_MATCHER.assertMatch(service.getAll(NOT_FOUND), Collections.emptyList());
    }

    @Test
    void delete() {
        service.delete(DISH_ID, ONE_RESTAURANT_ID);
        assertThrows(NotFoundException.class, () -> service.get(DISH_ID, ONE_RESTAURANT_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, ONE_RESTAURANT_ID));
    }

    @Test
    void getAllOnDate() {
        DISH_MATCHER.assertMatch(service.getAllOnDate(TWO_RESTAURANT_ID, EXISTING_DATE_DATABASE),
                List.of(dish12, dish11, dish10));
    }

    @Test
    void getAllNonExistentDate() {
        DISH_MATCHER.assertMatch(service.getAllOnDate(TWO_RESTAURANT_ID, NON_EXISTING_DATE_DATABASE),
                Collections.emptyList());
    }

    @Test
    void getAllToday() {
        DISH_MATCHER.assertMatch(service.getAllToday(FOUR_RESTAURANT_ID), DISHES_TODAY_FOUR);
    }

    @Test
    void create() {
        Dish created = service.save(getNew(), ONE_RESTAURANT_ID);
        int newId = created.id();
        Dish newDish = getNew();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(service.get(newId, ONE_RESTAURANT_ID), newDish);
    }

    @Test
    void createNotFound() {
        assertThrows(NotFoundException.class, () -> service.save(getNew(), NOT_FOUND));
    }

    @Test
    void update() {
        Dish updated = getUpdated();
        service.save(updated, ONE_RESTAURANT_ID);
        DISH_MATCHER.assertMatch(service.get(DISH_ID, ONE_RESTAURANT_ID), getUpdated());
    }

    @Test
    void updateNotFound() {
        assertThrows(NotFoundException.class, () -> service.save(getUpdated(), NOT_FOUND));
    }
}