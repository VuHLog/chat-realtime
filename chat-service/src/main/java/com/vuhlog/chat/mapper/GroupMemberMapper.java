package com.vuhlog.chat.mapper;

import com.vuhlog.chat.Utils.TimestampUtil;
import com.vuhlog.chat.dto.Response.GroupMemberResponse;
import com.vuhlog.chat.entity.GroupMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.sql.Timestamp;

@Mapper(componentModel = "spring")
public interface GroupMemberMapper {

    @Named("timestampToString")
    @Mapping(target = "addedAt", expression = "java(timestampToString(groupMember.getAddedAt()))")
    GroupMemberResponse toGroupMemberResponse(GroupMember groupMember);

    default Timestamp stringToTimestamp(String dateString) {
        return TimestampUtil.stringToTimestamp(dateString);
    }

    default String timestampToString(Timestamp timestamp) {
        return TimestampUtil.timestampToString(timestamp);
    }
}
