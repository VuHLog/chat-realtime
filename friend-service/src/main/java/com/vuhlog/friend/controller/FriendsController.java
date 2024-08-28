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
@RequestMapping("/requests")
public class FriendsController {
    @Autowired
    private FriendService friendService;

    @GetMapping("/{receiverId}")
    public ApiResponse<FriendRequestsResponse> getRequestBySenderIdAndReceiverId(@PathVariable String receiverId){
        return ApiResponse.<FriendRequestsResponse>builder()
                .result(friendService.findBySenderIdAndReceiverId(receiverId))
                .build();
    }

    @PostMapping("")
    public ApiResponse<FriendsStatusResponse> addFriendRequest(@RequestBody FriendRequestsDTO request){
        return ApiResponse.<FriendsStatusResponse>builder()
                .result(friendService.addFriendRequest(request))
                .build();
    }

    @PutMapping("/accept")
    public ApiResponse<FriendsStatusResponse> acceptFriendRequest(@RequestBody FriendRequestUpdate request){
        return ApiResponse.<FriendsStatusResponse>builder()
                .result(friendService.acceptFriendRequest(request))
                .build();
    }

    @DeleteMapping("/{friendRequestId}")
    public ApiResponse<Void> cancelFriendRequest(@PathVariable String friendRequestId){
        friendService.deleteFriendRequest(friendRequestId);
        return ApiResponse.<Void>builder().build();
    }
}
