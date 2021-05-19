package ru.graduation.voting.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtil {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME_PATTERN);

    public static LocalDateTime BEFORE_END_OF_VOTE = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0));

    /*only for test*/
    public static void setBeforeEndOfVote(LocalDateTime beforeEndOfVote) {
        BEFORE_END_OF_VOTE = beforeEndOfVote;
    }

    public static boolean isBefore(LocalDateTime dateTime) {
        return dateTime.isBefore(BEFORE_END_OF_VOTE);
    }
}
