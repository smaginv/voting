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
import ru.graduation.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static ru.graduation.voting.util.ValidationUtil.*;

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

    public Vote get(int id) {
        return voteRepository.get(id).orElseThrow(
                () -> new NotFoundException("Not found vote with id: " + id)
        );
    }

    @Cacheable("votes")
    public List<Vote> getAllToday() {
        return voteRepository.getAllOnDate(LocalDate.now()).orElse(Collections.emptyList());
    }

    @Cacheable("votesOnDate")
    public List<Vote> getAllOnDate(@Nullable LocalDate date) {
        return voteRepository.getAllOnDate(date).orElse(Collections.emptyList());
    }

    @Transactional
    @CacheEvict(value = "votes", allEntries = true)
    public Vote save(int userId, int restaurantId) {
        Vote vote = voteRepository.getTodayUserVote(userId).orElse(new Vote());
        checkNew(vote);
        vote.setUser(userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + userId)));
        vote.setRestaurant(restaurantRepository.get(restaurantId)
                .orElseThrow(() -> new NotFoundException("Not found restaurant with id: " + restaurantId)));
        vote.setDate(LocalDate.now());
        return voteRepository.save(vote);
    }

    @Transactional
    @CacheEvict(value = "votes", allEntries = true)
    public Vote update(int userId, int restaurantId) {
        Vote vote = voteRepository.getTodayUserVote(userId).orElseThrow(
                () -> new NotFoundException("Not found vote")
        );
        checkVoteTime(vote);
        vote.setRestaurant(restaurantRepository.get(restaurantId)
                .orElseThrow(() -> new NotFoundException("Not found restaurant with id: " + restaurantId)));
        return voteRepository.save(vote);
    }
}
