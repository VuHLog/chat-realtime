package com.vuhlog.friend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendsStatusResponse {
    private String friendRequestId;
    private String status;
    private boolean isFriend;
}
