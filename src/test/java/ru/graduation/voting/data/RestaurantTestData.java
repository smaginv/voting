package ru.graduation.voting.data;

import ru.graduation.voting.TestMatcher;
import ru.graduation.voting.model.Restaurant;
import ru.graduation.voting.to.RestaurantTo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.graduation.voting.data.TestData.*;

public class RestaurantTestData {

    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingEqualsComparator(Restaurant.class);
    public static final TestMatcher<RestaurantTo> RESTAURANT_TO_MATCHER = TestMatcher.usingEqualsComparator(RestaurantTo.class);
    public static TestMatcher<Restaurant> RESTAURANT_WITH_DISHES_MATCHER =
            TestMatcher.usingAssertions(Restaurant.class,
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("dishes.restaurant").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static final Restaurant restaurant1 = new Restaurant(ONE_RESTAURANT_ID, "One restaurant");
    public static final Restaurant restaurant2 = new Restaurant(TWO_RESTAURANT_ID, "Two restaurant");
    public static final Restaurant restaurant3 = new Restaurant(THREE_RESTAURANT_ID, "Three restaurant");
    public static final Restaurant restaurant4 = new Restaurant(FOUR_RESTAURANT_ID, "Four restaurant");
    public static final Restaurant restaurant5 = new Restaurant(FIVE_RESTAURANT_ID, "Five restaurant");

    public static final List<Restaurant> ALL_RESTAURANTS =
            List.of(restaurant5, restaurant4, restaurant3, restaurant2, restaurant1);

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(ONE_RESTAURANT_ID, "Updated Restaurant");
    }
}
