package ru.graduation.voting.data;

import ru.graduation.voting.TestMatcher;
import ru.graduation.voting.model.Dish;

import java.time.LocalDate;
import java.util.List;

import static ru.graduation.voting.data.TestData.DISH_ID;

public class DishTestData {
    public static final TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Dish.class, "restaurant");

    public static final Dish dish1 = new Dish(DISH_ID, "Baked potato", 1, LocalDate.of(2021, 4, 10));
    public static final Dish dish2 = new Dish(DISH_ID + 1, "Burger", 2, LocalDate.of(2021, 4, 10));
    public static final Dish dish3 = new Dish(DISH_ID + 2, "Casserole", 3, LocalDate.of(2021, 4, 10));
    public static final Dish dish4 = new Dish(DISH_ID + 3, "Chicken salad", 4, LocalDate.of(2021, 4, 11));
    public static final Dish dish5 = new Dish(DISH_ID + 4, "Crumble", 5, LocalDate.of(2021, 4, 11));
    public static final Dish dish6 = new Dish(DISH_ID + 5, "Curry", 6, LocalDate.of(2021, 4, 11));

    public static final Dish dish7 = new Dish(DISH_ID + 6, "Onion Rings", 1, LocalDate.of(2021, 4, 10));
    public static final Dish dish8 = new Dish(DISH_ID + 7, "Mozzarella Sticks", 2, LocalDate.of(2021, 4, 10));
    public static final Dish dish9 = new Dish(DISH_ID + 8, "Reuben", 3, LocalDate.of(2021, 4, 10));
    public static final Dish dish10 = new Dish(DISH_ID + 9, "Shrimp", 4, LocalDate.of(2021, 4, 11));
    public static final Dish dish11 = new Dish(DISH_ID + 10, "Calamari", 5, LocalDate.of(2021, 4, 11));
    public static final Dish dish12 = new Dish(DISH_ID + 11, "Arancini", 6, LocalDate.of(2021, 4, 11));

    public static final Dish dish13 = new Dish(DISH_ID + 12, "Greek Gyro", 1, LocalDate.now());
    public static final Dish dish14 = new Dish(DISH_ID + 13, "Buffalo Chicken", 2, LocalDate.now());
    public static final Dish dish15 = new Dish(DISH_ID + 14, "Roast Beef", 3, LocalDate.now());
    public static final Dish dish16 = new Dish(DISH_ID + 15, "Ham Slider", 3, LocalDate.now());
    public static final Dish dish17 = new Dish(DISH_ID + 16, "Pizza Slider", 2, LocalDate.now());
    public static final Dish dish18 = new Dish(DISH_ID + 17, "Potato Cakes", 1, LocalDate.now());


    public static final List<Dish> DISHES_ONE = List.of(dish6, dish5, dish4, dish3, dish2, dish1);
    public static final List<Dish> DISHES_TWO = List.of(dish12, dish11, dish10, dish9, dish8, dish7);
    public static final List<Dish> DISHES_TODAY_FOUR = List.of(dish15, dish14, dish13);
    public static final List<Dish> DISHES_TODAY_FIVE = List.of(dish18, dish17, dish16);
    public static final List<Dish> DISHES_ON_EXISTS_DATE_ONE = List.of(dish6, dish5, dish4);
    public static final List<Dish> DISHES_ON_EXISTS_DATE_TWO = List.of(dish12, dish11, dish10);

    public static Dish getNew() {
        return new Dish(null, "New dish", 55, LocalDate.now());
    }

    public static Dish getUpdated() {
        return new Dish(DISH_ID, "Updated dish", 111, LocalDate.now());
    }
}
