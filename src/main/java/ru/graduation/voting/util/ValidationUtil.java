package ru.graduation.voting.util;

import ru.graduation.voting.HasId;
import ru.graduation.voting.model.Vote;
import ru.graduation.voting.util.exception.NotFoundException;

import java.time.DateTimeException;
import java.time.LocalDateTime;

import static ru.graduation.voting.util.DateTimeUtil.BEFORE_END_OF_VOTE;
import static ru.graduation.voting.util.DateTimeUtil.isBefore;

public class ValidationUtil {

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(HasId entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }

    public static void checkVoteTime(Vote vote) {
        vote.setDateTime(LocalDateTime.now());
        if (!isBefore(vote.getDateTime())) {
            throw new DateTimeException(String.format("voting ended in %s", BEFORE_END_OF_VOTE));
        }
    }
}
