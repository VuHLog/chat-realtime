package com.vuhlog.chat.Utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class TimestampUtil {
    public static Timestamp stringToTimestamp(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new Timestamp(dateFormat.parse(dateString).getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse date: " + dateString, e);
        }
    }

    public static String timestampToString(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(timestamp);
    }

    public static Timestamp getTimeNow(){
        return Timestamp.valueOf(LocalDateTime.now());
    }
}
