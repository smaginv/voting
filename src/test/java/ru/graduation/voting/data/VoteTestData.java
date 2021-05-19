package ru.graduation.voting.data;

import ru.graduation.voting.TestMatcher;
import ru.graduation.voting.model.Vote;

import java.time.LocalDate;

import static ru.graduation.voting.data.TestData.VOTE_ID;

public class VoteTestData {

    public static final TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingEqualsComparator(Vote.class);

    public static final Vote vote1 = new Vote(VOTE_ID, LocalDate.of(2021, 4, 11));
    public static final Vote vote2 = new Vote(VOTE_ID + 1, LocalDate.of(2021, 4, 11));
    public static final Vote vote3 = new Vote(VOTE_ID + 2, LocalDate.of(2021, 4, 12));
    public static final Vote vote4 = new Vote(VOTE_ID + 3, LocalDate.of(2021, 4, 12));
    public static final Vote ADMIN_VOTE_TODAY = new Vote(VOTE_ID + 4, LocalDate.now());
    public static final Vote USER_VOTE_TODAY = new Vote(VOTE_ID + 5, LocalDate.now());

    public static Vote getNew() {
        return new Vote(null, LocalDate.now());
    }

    public static Vote getUpdated() {
        return new Vote(VOTE_ID, LocalDate.of(2021, 4, 15));
    }
}
