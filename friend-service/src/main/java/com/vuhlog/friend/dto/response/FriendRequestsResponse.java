package com.vuhlog.friend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestsResponse {
    private String id;

    private String senderId;

    private String receiverId;

    private String status;

    private String createdAt;

    private String updatedAt;
}
