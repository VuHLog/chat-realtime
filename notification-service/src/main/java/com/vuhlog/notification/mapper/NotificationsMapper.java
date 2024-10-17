package com.vuhlog.notification.mapper;

import com.vuhlog.notification.Utils.TimestampUtil;
import com.vuhlog.notification.dto.Request.NotificationsRequest;
import com.vuhlog.notification.dto.Response.NotificationStatusResponse;
import com.vuhlog.notification.dto.Response.NotificationsResponse;
import com.vuhlog.notification.entity.Notifications;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.sql.Timestamp;

@Mapper(componentModel = "spring")
public interface NotificationsMapper {
    Notifications toNotification(NotificationsRequest request);

    @Named("timestampToString")
    @Mapping(target = "timestamp", expression = "java(timestampToString(notifications.getTimestamp()))")
    NotificationsResponse toNotificationResponse(Notifications notifications);

    NotificationStatusResponse toNotificationStatusResponse(Notifications notifications);

    default Timestamp stringToTimestamp(String dateString) {
        return TimestampUtil.stringToTimestamp(dateString);
    }

    default String timestampToString(Timestamp timestamp) {
        return TimestampUtil.timestampToString(timestamp);
    }
}
