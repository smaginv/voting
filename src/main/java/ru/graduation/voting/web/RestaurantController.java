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
import ru.graduation.voting.model.Restaurant;
import ru.graduation.voting.service.RestaurantService;
import ru.graduation.voting.to.RestaurantTo;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.graduation.voting.util.ValidationUtil.assureIdConsistent;
import static ru.graduation.voting.util.ValidationUtil.checkNew;

@RestController
@Tag(name = "Restaurant Controller")
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by id", description = "Get restaurant by id")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return restaurantService.getTo(id);
    }

    @GetMapping
    @Operation(summary = "Get all restaurants", description = "Get all restaurants")
    public List<RestaurantTo> getAll() {
        log.info("Get all restaurants");
        return restaurantService.getAll();
    }

    @GetMapping("/{id}/menu-on-date")
    @Operation(summary = "Get restaurant with menu", description = "Get restaurant by id with menu on date")
    public Restaurant getWithMenuOnDate(@PathVariable int id, @RequestParam LocalDate date) {
        log.info("Get restaurant {} with menu on date {}", id, date);
        return restaurantService.getWithMenuOnDate(id, date);
    }

    @GetMapping("/{id}/menu-today")
    @Operation(summary = "Get restaurant with menu", description = "Get restaurant by id with today menu")
    public Restaurant getWithMenuToday(@PathVariable int id) {
        log.info("Get restaurant {} with today menu", id);
        return restaurantService.getWithMenuToday(id);
    }

    @GetMapping("/menu-on-date")
    @Operation(summary = "Get all restaurants with menu", description = "Get all restaurants with menu on date")
    public List<Restaurant> getAllWithMenuOnDate(@RequestParam LocalDate date) {
        log.info("Get all restaurants with menu on date {}", date);
        return restaurantService.getAllWithMenuOnDate(date);
    }

    @GetMapping("/menu-today")
    @Operation(summary = "Get all restaurants with menu", description = "Get all restaurants with today menu")
    public List<Restaurant> getAllWithMenuToday() {
        log.info("Get all restaurants with today menu");
        return restaurantService.getAllWithMenuToday();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete restaurant", description = "Delete restaurant by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        restaurantService.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create restaurant", description = "Create restaurant")
    public ResponseEntity<Restaurant> create(@Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("create {}", restaurantTo);
        checkNew(restaurantTo);
        Restaurant created = restaurantService.save(restaurantTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update restaurant", description = "Update restaurant by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody RestaurantTo restaurantTo, @PathVariable int id) {
        log.info("update {}", restaurantTo);
        assureIdConsistent(restaurantTo, id);
        restaurantService.update(restaurantTo);
    }
}
