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
import java.util.Collections;
import java.util.List;

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
        return restaurantRepository.get(id).orElseThrow(
                () -> new NotFoundException("Not found restaurant with id: " + id)
        );
    }

    @Cacheable("restaurants")
    public List<RestaurantTo> getAll() {
        return createTos(restaurantRepository.getAll().orElse(Collections.emptyList()));
    }

    @CacheEvict(value = {"restaurants", "restaurantsWithMenu"}, allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(restaurantRepository.delete(id) != 0, id);
    }

    public Restaurant getWithMenuOnDate(int id, LocalDate date) {
        return restaurantRepository.getWithMenuOnDate(id, date).orElseThrow(
                () -> new NotFoundException("Not found restaurant with id: " + id)
        );
    }

    public Restaurant getWithMenuToday(int id) {
        return restaurantRepository.getWithMenuOnDate(id, LocalDate.now()).orElseThrow(
                () -> new NotFoundException("Not found restaurant with id: " + id)
        );
    }

    public List<Restaurant> getAllWithMenuOnDate(LocalDate date) {
        return restaurantRepository.getAllWithMenuOnDate(date).orElse(Collections.emptyList());
    }

    @Cacheable("restaurantsWithMenu")
    public List<Restaurant> getAllWithMenuToday() {
        return restaurantRepository.getAllWithMenuOnDate(LocalDate.now()).orElse(Collections.emptyList());
    }

    @Transactional
    @CacheEvict(value = {"restaurants", "restaurantsWithMenu"}, allEntries = true)
    public Restaurant save(RestaurantTo restaurantTo) {
        Assert.notNull(restaurantTo, "restaurant must not be null");
        return restaurantRepository.save(createNewFromTo(restaurantTo));
    }

    @Transactional
    @CacheEvict(value = {"restaurants", "restaurantsWithMenu"}, allEntries = true)
    public Restaurant update(RestaurantTo restaurantTo) {
        Assert.notNull(restaurantTo, "restaurant must not be null");
        Restaurant restaurant = updateFromTo(get(restaurantTo.id()), restaurantTo);
        return checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.id());
    }
}
