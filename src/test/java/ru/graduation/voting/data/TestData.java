package ru.graduation.voting.data;

import java.time.LocalDate;

public class TestData {
    public static final LocalDate EXISTING_DATE_DATABASE = LocalDate.of(2021, 4, 11);
    public static final LocalDate NON_EXISTING_DATE_DATABASE = LocalDate.of(2000, 1, 1);

    public static final int NOT_FOUND = 111;
    public static final int START = 1;

    public static final int ADMIN_ID = START;
    public static final int USER_ID = START + 1;

    public static final int ONE_RESTAURANT_ID = START;
    public static final int TWO_RESTAURANT_ID = START + 1;
    public static final int THREE_RESTAURANT_ID = START + 2;
    public static final int FOUR_RESTAURANT_ID = START + 3;
    public static final int FIVE_RESTAURANT_ID = START + 4;

    public static final int DISH_ID = START;

    public static final int VOTE_ID = START;
}
