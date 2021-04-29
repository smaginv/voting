package ru.graduation.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.graduation.voting.model.Dish;
import ru.graduation.voting.service.DishService;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.graduation.voting.util.ValidationUtil.assureIdConsistent;
import static ru.graduation.voting.util.ValidationUtil.checkNew;

@RestController
@Tag(name = "Dish Controller")
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {

    static final String REST_URL = "/api/restaurants/{restaurantId}/dishes";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get dish by id", description = "Get dish by id and restaurant id")
    public Dish get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get dish {} for restaurant {}", id, restaurantId);
        return dishService.get(id, restaurantId);
    }

    @GetMapping
    @Operation(summary = "Get all dishes", description = "Get all dishes by restaurant id")
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("get all dish for restaurant {}", restaurantId);
        return dishService.getAll(restaurantId);
    }

    @GetMapping("/on-date")
    @Operation(summary = "Get all dishes on date", description = "Get all dishes on date by restaurant id")
    public List<Dish> getAllOnDate(@PathVariable int restaurantId, @RequestParam LocalDate date) {
        log.info("get all dish by date {} for restaurant {}", date, restaurantId);
        return dishService.getAllOnDate(restaurantId, date);
    }

    @GetMapping("/today")
    @Operation(summary = "Get all dishes today", description = "Get all dishes today by restaurant id")
    public List<Dish> getAllToday(@PathVariable int restaurantId) {
        log.info("get all dish today for restaurant {}", restaurantId);
        return dishService.getAllToday(restaurantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete dish", description = "Delete dishes by id and restaurant id")
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete dish {} for restaurant {}", id, restaurantId);
        dishService.delete(id, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create dish", description = "Create dish for restaurant")
    public ResponseEntity<Dish> create(@Valid @RequestBody Dish dish, @PathVariable int restaurantId) {
        log.info("create {}", dish);
        checkNew(dish);
        Dish created = dishService.save(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update dish", description = "Update dish for restaurant")
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update {}", dish);
        assureIdConsistent(dish, id);
        dishService.update(dish, restaurantId);
    }
}
