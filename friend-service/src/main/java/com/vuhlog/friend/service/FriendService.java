package com.vuhlog.friend.service;

import com.vuhlog.friend.dto.request.FriendRequestUpdate;
import com.vuhlog.friend.dto.request.FriendRequestsDTO;
import com.vuhlog.friend.dto.response.FriendRequestsResponse;
import com.vuhlog.friend.dto.response.FriendsStatusResponse;

public interface FriendService {
    FriendsStatusResponse addFriendRequest(FriendRequestsDTO friendRequests);

    FriendsStatusResponse acceptFriendRequest(FriendRequestUpdate friendRequests);

    void deleteFriendRequest(String friendRequestId);

    Boolean existsByUserIdAndFriendId(String friendId);

    FriendRequestsResponse findBySenderIdAndReceiverId(String receiverId);
}
