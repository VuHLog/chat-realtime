package com.vuhlog.friend.service;

import com.vuhlog.friend.dto.request.FriendRequestUpdate;
import com.vuhlog.friend.dto.request.FriendRequestsDTO;
import com.vuhlog.friend.dto.response.FriendsStatusResponse;

public interface FriendService {
    FriendsStatusResponse addFriendRequest(FriendRequestsDTO friendRequests);

    FriendsStatusResponse acceptFriendRequest(FriendRequestUpdate friendRequests);

    FriendsStatusResponse rejectFriendRequest(FriendRequestUpdate friendRequests);

    FriendsStatusResponse cancelFriendRequest(FriendRequestUpdate friendRequests);

    Boolean existsByUserIdAndFriendId(String friendId);

    FriendsStatusResponse findBySenderIdAndReceiverId(String receiverId);
}
