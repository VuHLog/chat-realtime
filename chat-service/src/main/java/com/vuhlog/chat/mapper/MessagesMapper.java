package com.vuhlog.chat.mapper;

import com.vuhlog.chat.Utils.TimestampUtil;
import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.dto.Response.MessagesResponse;
import com.vuhlog.chat.entity.Messages;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Mapper(componentModel = "spring")
public interface MessagesMapper {
    Messages toMessages(MessagesRequest request);

    @Named("timestampToString")
    @Mapping(target = "timeSent", expression = "java(timestampToString(messages.getTimeSent()))")
    MessagesResponse toMessagesResponse(Messages messages);

    default Timestamp stringToTimestamp(String dateString) {
            return TimestampUtil.stringToTimestamp(dateString);
    }

    default String timestampToString(Timestamp timestamp) {
        return TimestampUtil.timestampToString(timestamp);
    }
}
