package ru.graduation.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.graduation.voting.model.Vote;
import ru.graduation.voting.repository.RestaurantRepository;
import ru.graduation.voting.repository.UserRepository;
import ru.graduation.voting.repository.VoteRepository;
import ru.graduation.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        return getVoteFromOptional(voteRepository.get(id));
    }

    public List<Vote> getAllToday() {
        return voteRepository.getAllOnDate(LocalDate.now());
    }

    public List<Vote> getAllOnDate(@Nullable LocalDate date) {
        return voteRepository.getAllOnDate(date);
    }

    @Transactional
    public Vote save(int userId, int restaurantId) {
        Vote vote = voteRepository.getTodayUserVote(userId).orElse(new Vote());
        checkNew(vote);
        vote.setUser(userRepository.getOne(userId));
        vote.setRestaurant(restaurantRepository.getOne(restaurantId));
        vote.setDate(LocalDate.now());
        return voteRepository.save(vote);
    }

    @Transactional
    public Vote update(int userId, int restaurantId) {
        Vote vote = getVoteFromOptional(voteRepository.getTodayUserVote(userId));
        checkVoteTime(vote);
        vote.setRestaurant(restaurantRepository.getOne(restaurantId));
        return voteRepository.save(vote);
    }

    private Vote getVoteFromOptional(Optional<Vote> optional) {
        return optional.orElseThrow(
                () -> new NotFoundException("Not found vote")
        );
    }
}
