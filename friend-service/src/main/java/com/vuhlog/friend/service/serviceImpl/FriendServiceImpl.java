package com.vuhlog.friend.service.serviceImpl;

import com.vuhlog.friend.constants.FriendRequestsStatus;
import com.vuhlog.friend.dto.request.FriendRequestUpdate;
import com.vuhlog.friend.dto.request.FriendRequestsDTO;
import com.vuhlog.friend.dto.response.FriendsStatusResponse;
import com.vuhlog.friend.entity.FriendRequests;
import com.vuhlog.friend.entity.Friends;
import com.vuhlog.friend.exception.AppException;
import com.vuhlog.friend.exception.ErrorCode;
import com.vuhlog.friend.repository.FriendRequestsRepository;
import com.vuhlog.friend.repository.FriendsRepository;
import com.vuhlog.friend.repository.httpClients.IdentityClient;
import com.vuhlog.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendRequestsRepository friendRequestsRepository;

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private IdentityClient identityClient;

    @Override
    public FriendsStatusResponse addFriendRequest(FriendRequestsDTO request) {
        String userId = getUserId();
        if (existsByUserIdAndFriendId(request.getReceiverId())
        ) throw new AppException(ErrorCode.FRIEND_EXISTED);

        FriendRequests friendRequest = FriendRequests.builder()
                .senderId(userId)
                .receiverId(request.getReceiverId())
                .status(FriendRequestsStatus.PENDING.getStatus())
                .build();
        friendRequestsRepository.save(friendRequest);

        return FriendsStatusResponse.builder()
                .status(FriendRequestsStatus.PENDING.getStatus())
                .isFriend(false)
                .build();
    }

    @Override
    public FriendsStatusResponse acceptFriendRequest(FriendRequestUpdate request) {
        //chua ton tai loi moi ket ban
        Optional<FriendRequests> optionalFriendRequests = friendRequestsRepository.findById(request.getId());
        if (
                optionalFriendRequests.isEmpty()
        ) throw new AppException(ErrorCode.FRIEND_REQUEST_NOT_EXISTED);

        FriendRequests friendRequest = optionalFriendRequests.get();
        friendRequest.setStatus(FriendRequestsStatus.ACCEPTED.getStatus());
        friendRequestsRepository.save(friendRequest);

        Friends friend1 = Friends.builder()
                .userId(friendRequest.getReceiverId())
                .friendId(friendRequest.getSenderId())
                .build();
        friendsRepository.save(friend1);

        Friends friend2 = Friends.builder()
                .userId(friendRequest.getSenderId())
                .friendId(friendRequest.getReceiverId())
                .build();
        friendsRepository.save(friend2);
        return FriendsStatusResponse.builder()
                .friendRequestId(friendRequest.getId())
                .status(FriendRequestsStatus.ACCEPTED.getStatus())
                .isFriend(true)
                .build();
    }

    @Override
    public FriendsStatusResponse rejectFriendRequest(FriendRequestUpdate request) {
        //chua ton tai loi moi ket ban
        Optional<FriendRequests> optionalFriendRequests = friendRequestsRepository.findById(request.getId());
        if (
                optionalFriendRequests.isEmpty()
        ) throw new AppException(ErrorCode.FRIEND_REQUEST_NOT_EXISTED);

        FriendRequests friendRequest = optionalFriendRequests.get();
        friendRequest.setStatus(FriendRequestsStatus.REJECTED.getStatus());
        friendRequestsRepository.save(friendRequest);

        return FriendsStatusResponse.builder()
                .friendRequestId(friendRequest.getId())
                .status(FriendRequestsStatus.REJECTED.getStatus())
                .isFriend(false)
                .build();
    }

    @Override
    public FriendsStatusResponse cancelFriendRequest(FriendRequestUpdate request) {
        //chua ton tai loi moi ket ban
        Optional<FriendRequests> optionalFriendRequests = friendRequestsRepository.findById(request.getId());
        if (
                optionalFriendRequests.isEmpty()
        ) throw new AppException(ErrorCode.FRIEND_REQUEST_NOT_EXISTED);

        FriendRequests friendRequest = optionalFriendRequests.get();
        friendRequest.setStatus(FriendRequestsStatus.CANCELLED.getStatus());
        friendRequestsRepository.save(friendRequest);

        return FriendsStatusResponse.builder()
                .friendRequestId(friendRequest.getId())
                .status(FriendRequestsStatus.CANCELLED.getStatus())
                .isFriend(false)
                .build();
    }

    @Override
    public Boolean existsByUserIdAndFriendId(String friendId) {
        String userId = getUserId();
        return friendsRepository.existsByUserIdAndFriendId(userId, friendId);
    }

    @Override
    public FriendsStatusResponse findBySenderIdAndReceiverId(String receiverId) {
        String senderId = getUserId();
        Optional<FriendRequests> OptionalFriendRequests = friendRequestsRepository.findTop1BySenderIdAndReceiverIdOrderByUpdatedAtDesc(senderId, receiverId);
        if (OptionalFriendRequests.isEmpty()) throw new AppException(ErrorCode.FRIEND_REQUEST_NOT_EXISTED);
        FriendRequests friendRequest = OptionalFriendRequests.get();
        return FriendsStatusResponse.builder()
                .friendRequestId(friendRequest.getId())
                .status(friendRequest.getStatus())
                .isFriend(friendRequest.getStatus().equals(FriendRequestsStatus.ACCEPTED.getStatus()))
                .build();
    }

    public String getUserId(){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return identityClient.getUserByUsername(username).getResult().getId();
    }
}
