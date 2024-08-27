package com.vuhlog.friend.constants;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public enum FriendRequestsStatus {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected"),
    CANCELLED("cancelled");
    FriendRequestsStatus(String status) {
        this.status = status;
    }

    private final String status;
}
