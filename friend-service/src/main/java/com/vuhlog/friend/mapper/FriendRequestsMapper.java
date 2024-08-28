package com.vuhlog.friend.mapper;

import com.vuhlog.friend.Utils.TimestampUtil;
import com.vuhlog.friend.dto.response.FriendRequestsResponse;
import com.vuhlog.friend.entity.FriendRequests;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.sql.Timestamp;

@Mapper(componentModel = "spring")
public interface FriendRequestsMapper {
    @Named("timestampToString")
    @Mapping(target = "createdAt", expression = "java(timestampToString(friendRequest.getCreatedAt()))")
    @Mapping(target = "updatedAt", expression = "java(timestampToString(friendRequest.getUpdatedAt()))")
    FriendRequestsResponse toFriendRequestResponse(FriendRequests friendRequest);

    default Timestamp stringToTimestamp(String dateString) {
        return TimestampUtil.stringToTimestamp(dateString);
    }

    default String timestampToString(Timestamp timestamp) {
        return TimestampUtil.timestampToString(timestamp);
    }
}
