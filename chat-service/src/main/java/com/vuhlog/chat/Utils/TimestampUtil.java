package com.vuhlog.chat.Utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class TimestampUtil {
//    Map<Long, Function<Timestamp, String>> strategyMap = new LinkedHashMap<>();

//    public TimestampUtil() {
//        strategyMap.put(60L, this::formatInSeconds); // time < 1 minute
//        strategyMap.put(3600L, this::formatInMinutes); // time < 1 hour
//        strategyMap.put(86400L, this::formatInHours); //time < 1 day
//        strategyMap.put(Long.MAX_VALUE, this::formatInDate);
//    }

    public static Timestamp stringToTimestamp(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new Timestamp(dateFormat.parse(dateString).getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse date: " + dateString, e);
        }
    }

    private String formatInSeconds(Timestamp timestamp) {
        long elapseSeconds = ChronoUnit.SECONDS.between(timestamp.toInstant(), Instant.now());
        return elapseSeconds + " giây trước";
    }

    private String formatInMinutes(Timestamp timestamp) {
        long elapseMinutes = ChronoUnit.MINUTES.between(timestamp.toInstant(), Instant.now());
        return elapseMinutes + " phút trước";
    }

    private String formatInHours(Timestamp timestamp) {
        long elapseHours = ChronoUnit.HOURS.between(timestamp.toInstant(), Instant.now());
        return elapseHours + " giờ trước";
    }

    private String formatInDate(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(timestamp);
    }

    public static String timestampToString(Timestamp timestamp) {
        long elapseSeconds = ChronoUnit.SECONDS.between(timestamp.toInstant(), Instant.now());
//
//        var strategy = strategyMap.entrySet().stream()
//                .filter(longFunctionEntry -> elapseSeconds < longFunctionEntry.getKey()).findFirst().get();
//
//        return strategy.getValue().apply(timestamp);


        if (elapseSeconds < 60) {
            return elapseSeconds + " giây trước";
        } else if (elapseSeconds < 3600) {
            long elapseMinutes = ChronoUnit.MINUTES.between(timestamp.toInstant(), Instant.now());
            return elapseMinutes + " phút trước";
        } else if (elapseSeconds < 86400) {
            long elapseHours = ChronoUnit.HOURS.between(timestamp.toInstant(), Instant.now());
            return elapseHours + " giờ trước";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(timestamp);
    }

    public static Timestamp getTimeNow() {
        return Timestamp.valueOf(LocalDateTime.now());
    }
}
