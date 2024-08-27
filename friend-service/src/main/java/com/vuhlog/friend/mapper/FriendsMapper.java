package com.vuhlog.friend.mapper;

import com.vuhlog.friend.Utils.TimestampUtil;
import com.vuhlog.friend.dto.response.FriendsResponse;
import com.vuhlog.friend.entity.Friends;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.sql.Timestamp;

@Mapper(componentModel = "spring")
public interface FriendsMapper {
    @Named("timestampToString")
    @Mapping(target = "createdAt", expression = "java(timestampToString(friends.getCreatedAt()))")
    FriendsResponse toFriendsResponse(Friends friends);

    default Timestamp stringToTimestamp(String dateString) {
        return TimestampUtil.stringToTimestamp(dateString);
    }

    default String timestampToString(Timestamp timestamp) {
        return TimestampUtil.timestampToString(timestamp);
    }
}
