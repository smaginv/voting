package ru.graduation.voting.data;

import ru.graduation.voting.TestMatcher;
import ru.graduation.voting.model.Vote;
import ru.graduation.voting.to.VoteTo;

import java.time.LocalDateTime;
import java.util.List;

import static ru.graduation.voting.data.TestData.VOTE_ID;

public class VoteTestData {

    public static final TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingEqualsComparator(Vote.class);
    public static final TestMatcher<VoteTo> VOTE_TO_MATCHER = TestMatcher.usingEqualsComparator(VoteTo.class);

    public static final Vote vote1 = new Vote(VOTE_ID, LocalDateTime.of(2021, 4, 11, 10, 0, 0));
    public static final Vote vote2 = new Vote(VOTE_ID + 1, LocalDateTime.of(2021, 4, 11, 9, 0, 0));
    public static final Vote vote3 = new Vote(VOTE_ID + 2, LocalDateTime.of(2021, 4, 12, 10, 30, 0));
    public static final Vote vote4 = new Vote(VOTE_ID + 3, LocalDateTime.of(2021, 4, 12, 10, 50, 0));
    public static final Vote ADMIN_VOTE_TODAY = new Vote(VOTE_ID + 4, LocalDateTime.now());
    public static final Vote USER_VOTE_TODAY = new Vote(VOTE_ID + 5, LocalDateTime.now());

    public static final List<Vote> ALL_ADMIN_VOTES = List.of(ADMIN_VOTE_TODAY, vote3, vote1);
    public static final List<Vote> ALL_USER_VOTES = List.of(USER_VOTE_TODAY, vote4, vote2);

    public static Vote getNew() {
        return new Vote(null, LocalDateTime.now());
    }

    public static Vote getUpdated() {
        return new Vote(VOTE_ID, LocalDateTime.of(2021, 4, 15, 7, 50, 0));
    }
}
