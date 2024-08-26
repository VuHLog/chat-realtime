package com.vuhlog.chat.mapper;

import com.vuhlog.chat.Utils.TimestampUtil;
import com.vuhlog.chat.dto.Request.ConversationsRequest;
import com.vuhlog.chat.dto.Response.ConversationsResponse;
import com.vuhlog.chat.entity.Conversations;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.sql.Timestamp;

@Mapper(componentModel = "spring")
public interface ConversationsMapper {
    @Mapping(target = "groupMembers", ignore = true)
    Conversations toConversations(ConversationsRequest request);

    @Named("timestampToString")
    @Mapping(target = "createdAt", expression = "java(timestampToString(conversations.getCreatedAt()))")
    @Mapping(target = "updatedAt", expression = "java(timestampToString(conversations.getUpdatedAt()))")
    ConversationsResponse toConversationsResponse(Conversations conversations);

    default Timestamp stringToTimestamp(String dateString) {
        return TimestampUtil.stringToTimestamp(dateString);
    }

    default String timestampToString(Timestamp timestamp) {
        return TimestampUtil.timestampToString(timestamp);
    }
}
