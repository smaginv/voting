package ru.graduation.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.graduation.voting.model.Vote;
import ru.graduation.voting.repository.RestaurantRepository;
import ru.graduation.voting.repository.UserRepository;
import ru.graduation.voting.repository.VoteRepository;
import ru.graduation.voting.to.VoteTo;
import ru.graduation.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.graduation.voting.util.DateTimeUtil.*;
import static ru.graduation.voting.util.ValidationUtil.checkVoteTime;
import static ru.graduation.voting.util.ValidationUtil.checkNotFoundWithId;
import static ru.graduation.voting.util.VoteUtil.*;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository, UserRepository userRepository,
                       RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public VoteTo getTo(int id) {
        return createToWithoutRestaurant(get(id));
    }

    private Vote get(int id) {
        return voteRepository.get(id).orElseThrow(
                () -> new NotFoundException("Not found vote with id: " + id)
        );
    }

    public VoteTo getForUserOnDate(int userId, @Nullable LocalDate date) {
        Vote vote = voteRepository.getForUserOnDate(userId, getStartOfDay(date), getEndOfDay(date)).orElseThrow(
                () -> new NotFoundException(String.format("Not found vote for user %d on date %s", userId, date))
        );
        return createToWithoutUser(vote);
    }

    public VoteTo getForUserToday(int userId) {
        Vote vote = getOptional(userId).orElseThrow(
                () -> new NotFoundException(String.format("Not found vote for user %d today", userId))
        );
        return createToWithoutUser(vote);
    }

    public List<VoteTo> getAllForUser(int userId) {
        return createTosWithoutUser(voteRepository.getAllForUser(userId).orElse(Collections.emptyList()));
    }

    @Cacheable("votesOnDate")
    public List<VoteTo> getAllOnDate(@Nullable LocalDate date) {
        return createTosFull(voteRepository.getAllOnDate(getStartOfDay(date), getEndOfDay(date))
                .orElse(Collections.emptyList()));
    }

    @Cacheable("votes")
    public List<VoteTo> getAllToday() {
        return createTosFull(voteRepository.getAllOnDate(getStartOfDay(LocalDate.now()), getEndOfDay(LocalDate.now())).
                orElse(Collections.emptyList()));
    }

    @CacheEvict(value = "votes", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(voteRepository.delete(id) != 0, id);
    }

    @Transactional
    @CacheEvict(value = "votes", allEntries = true)
    public Vote save(int userId, int restaurantId) {
        Vote vote = getOptional(userId).orElse(new Vote());
        checkVoteTime(vote);
        if (vote.isNew()) {
            vote.setUser(userRepository.get(userId)
                    .orElseThrow(() -> new NotFoundException("Not found user with id: " + userId)));
        }
        vote.setRestaurant(restaurantRepository.get(restaurantId)
                .orElseThrow(() -> new NotFoundException("Not found restaurant with id: " + restaurantId)));

        return voteRepository.save(vote);
    }

    private Optional<Vote> getOptional(int userId) {
        return voteRepository.getForUserOnDate(userId, getStartOfDay(LocalDate.now()),
                getEndOfDay(LocalDate.now()));
    }
}
