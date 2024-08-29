package com.vuhlog.friend.controller;

import com.vuhlog.friend.dto.ApiResponse;
import com.vuhlog.friend.dto.request.FriendRequestUpdate;
import com.vuhlog.friend.dto.request.FriendRequestsDTO;
import com.vuhlog.friend.dto.response.FriendRequestsResponse;
import com.vuhlog.friend.dto.response.FriendsStatusResponse;
import com.vuhlog.friend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class FriendsController {
    @Autowired
    private FriendService friendService;

    @GetMapping("/requests/{receiverId}")
    public ApiResponse<FriendRequestsResponse> getRequestBySenderIdAndReceiverId(@PathVariable String receiverId){
        return ApiResponse.<FriendRequestsResponse>builder()
                .result(friendService.findBySenderIdAndReceiverId(receiverId))
                .build();
    }

    @PostMapping("/requests")
    public ApiResponse<FriendsStatusResponse> addFriendRequest(@RequestBody FriendRequestsDTO request){
        return ApiResponse.<FriendsStatusResponse>builder()
                .result(friendService.addFriendRequest(request))
                .build();
    }

    @PutMapping("/requests/accept")
    public ApiResponse<FriendsStatusResponse> acceptFriendRequest(@RequestBody FriendRequestUpdate request){
        return ApiResponse.<FriendsStatusResponse>builder()
                .result(friendService.acceptFriendRequest(request))
                .build();
    }

    @DeleteMapping("/requests/{friendRequestId}")
    public ApiResponse<Void> cancelFriendRequest(@PathVariable String friendRequestId){
        friendService.deleteFriendRequest(friendRequestId);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/{friendId}")
    public ApiResponse<Void> unFriend(@PathVariable String friendId){
        friendService.unfriend(friendId);
        return ApiResponse.<Void>builder().build();
    }
}
