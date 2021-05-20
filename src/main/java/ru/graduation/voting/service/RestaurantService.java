package ru.graduation.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.graduation.voting.model.Restaurant;
import ru.graduation.voting.repository.RestaurantRepository;
import ru.graduation.voting.to.RestaurantTo;
import ru.graduation.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ru.graduation.voting.util.RestaurantUtil.*;
import static ru.graduation.voting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantTo getTo(int id) {
        return createTo(get(id));
    }

    private Restaurant get(int id) {
        return getRestaurantFromOptional(restaurantRepository.get(id), id);
    }

    public List<RestaurantTo> getAll() {
        return createTos(restaurantRepository.getAll());
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(restaurantRepository.delete(id) != 0, id);
    }

    public Restaurant getWithMenuOnDate(int id, LocalDate date) {
        return getRestaurantFromOptional(restaurantRepository.getWithMenuOnDate(id, date), id);
    }

    public Restaurant getWithMenuToday(int id) {
        return getRestaurantFromOptional(restaurantRepository.getWithMenuOnDate(id, LocalDate.now()), id);
    }

    public List<Restaurant> getAllWithMenuOnDate(LocalDate date) {
        return restaurantRepository.getAllWithMenuOnDate(date);
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAllWithMenuToday() {
        return restaurantRepository.getAllWithMenuOnDate(LocalDate.now());
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant save(RestaurantTo restaurantTo) {
        Assert.notNull(restaurantTo, "restaurant must not be null");
        return restaurantRepository.save(createNewFromTo(restaurantTo));
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant update(RestaurantTo restaurantTo) {
        Assert.notNull(restaurantTo, "restaurant must not be null");
        Restaurant restaurant = updateFromTo(get(restaurantTo.id()), restaurantTo);
        return checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.id());
    }

    private Restaurant getRestaurantFromOptional(Optional<Restaurant> optional, int id) {
        return optional.orElseThrow(
                () -> new NotFoundException("Not found restaurant with id: " + id)
        );
    }
}
