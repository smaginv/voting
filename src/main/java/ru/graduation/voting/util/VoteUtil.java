package ru.graduation.voting.util;

import ru.graduation.voting.model.Vote;
import ru.graduation.voting.to.VoteTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class VoteUtil {

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getDateTime());
    }

    public static VoteTo createToFull(Vote vote) {
        return new VoteTo(vote.getId(), vote.getDateTime(), UserUtil.createTo(vote.getUser()),
                RestaurantUtil.createTo(vote.getRestaurant()));
    }

    public static VoteTo createToWithoutRestaurant(Vote vote) {
        return new VoteTo(vote.getId(), vote.getDateTime());
    }

    public static VoteTo createToWithoutUser(Vote vote) {
        return new VoteTo(vote.getId(), vote.getDateTime(), RestaurantUtil.createTo(vote.getRestaurant()));
    }

    public static List<VoteTo> createTos(Collection<Vote> votes) {
        return votes.stream().map(VoteUtil::createTo).collect(Collectors.toList());
    }

    public static List<VoteTo> createTosFull(Collection<Vote> votes) {
        return votes.stream().map(VoteUtil::createToFull).collect(Collectors.toList());
    }

    public static List<VoteTo> createTosWithoutRestaurant(Collection<Vote> votes) {
        return votes.stream().map(VoteUtil::createToWithoutRestaurant).collect(Collectors.toList());
    }

    public static List<VoteTo> createTosWithoutUser(Collection<Vote> votes) {
        return votes.stream().map(VoteUtil::createToWithoutUser).collect(Collectors.toList());
    }
}
