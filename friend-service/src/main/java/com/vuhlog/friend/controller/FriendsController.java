package com.vuhlog.friend.controller;

import com.vuhlog.friend.dto.ApiResponse;
import com.vuhlog.friend.dto.request.FriendRequestUpdate;
import com.vuhlog.friend.dto.request.FriendRequestsDTO;
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
    public ApiResponse<FriendsStatusResponse> getRequestBySenderIdAndReceiverId(@PathVariable String receiverId){
        return ApiResponse.<FriendsStatusResponse>builder()
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

    @PutMapping("/cancel")
    public ApiResponse<FriendsStatusResponse> cancelFriendRequest(@RequestBody FriendRequestUpdate request){
        return ApiResponse.<FriendsStatusResponse>builder()
                .result(friendService.cancelFriendRequest(request))
                .build();
    }
}
