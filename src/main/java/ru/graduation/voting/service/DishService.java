package ru.graduation.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.graduation.voting.model.Dish;
import ru.graduation.voting.repository.DishRepository;
import ru.graduation.voting.repository.RestaurantRepository;
import ru.graduation.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static ru.graduation.voting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService {

    private final DishRepository dishRepository;

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public DishService(DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Dish get(int id, int restaurantId) {
        return dishRepository.get(id, restaurantId).orElseThrow(
                () -> new NotFoundException("Not found dish with id: " + id)
        );
    }

    @Cacheable("dishes")
    public List<Dish> getAll(int restaurantId) {
        return dishRepository.getAll(restaurantId);
    }

    @CacheEvict(value = {"dishes", "dishesToday"}, allEntries = true)
    public void delete(int id, int restaurantId) {
        checkNotFoundWithId(dishRepository.delete(id, restaurantId) != 0, id);
    }

    public List<Dish> getAllOnDate(int restaurantId, LocalDate date) {
        return dishRepository.getAllOnDate(restaurantId, date);
    }

    @Cacheable("dishesToday")
    public List<Dish> getAllToday(int restaurantId) {
        return dishRepository.getAllToday(restaurantId);
    }

    @Transactional
    @CacheEvict(value = {"dishes", "dishesToday"}, allEntries = true)
    public Dish save(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        setRestaurant(dish, restaurantId);
        return dishRepository.save(dish);
    }

    @Transactional
    @CacheEvict(value = {"dishes", "dishesToday"}, allEntries = true)
    public Dish update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        if (!dish.isNew() && Objects.isNull(get(dish.id(), restaurantId))) {
            throw new NotFoundException("Not found dish");
        }
        setRestaurant(dish, restaurantId);
        return dishRepository.save(dish);
    }

    private void setRestaurant(Dish dish, int restaurantId) {
        dish.setRestaurant(restaurantRepository.get(restaurantId).orElseThrow(
                () -> new NotFoundException("Not found restaurant with id: " + restaurantId)
        ));
    }
}
