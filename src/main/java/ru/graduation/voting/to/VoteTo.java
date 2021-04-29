package ru.graduation.voting.to;

import java.time.LocalDateTime;

public class VoteTo extends BaseTo {

    private LocalDateTime dateTime;

    private UserTo user;

    private RestaurantTo restaurant;

    public VoteTo() {
    }

    public VoteTo(Integer id, LocalDateTime dateTime) {
        super(id);
        this.dateTime = dateTime;
    }

    public VoteTo(Integer id, LocalDateTime dateTime, RestaurantTo restaurant) {
        super(id);
        this.dateTime = dateTime;
        this.restaurant = restaurant;
    }

    public VoteTo(Integer id, LocalDateTime dateTime, UserTo user, RestaurantTo restaurant) {
        super(id);
        this.dateTime = dateTime;
        this.user = user;
        this.restaurant = restaurant;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public RestaurantTo getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantTo restaurant) {
        this.restaurant = restaurant;
    }

    public UserTo getUser() {
        return user;
    }

    public void setUser(UserTo user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "VoteTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                '}';
    }
}
