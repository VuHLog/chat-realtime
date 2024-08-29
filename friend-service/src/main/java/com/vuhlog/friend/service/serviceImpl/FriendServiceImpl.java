package com.vuhlog.friend.service.serviceImpl;

import com.vuhlog.friend.constants.FriendRequestsStatus;
import com.vuhlog.friend.dto.request.FriendRequestUpdate;
import com.vuhlog.friend.dto.request.FriendRequestsDTO;
import com.vuhlog.friend.dto.response.FriendRequestsResponse;
import com.vuhlog.friend.dto.response.FriendsStatusResponse;
import com.vuhlog.friend.entity.FriendRequests;
import com.vuhlog.friend.entity.Friends;
import com.vuhlog.friend.exception.AppException;
import com.vuhlog.friend.exception.ErrorCode;
import com.vuhlog.friend.mapper.FriendRequestsMapper;
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

    @Autowired
    private FriendRequestsMapper friendRequestsMapper;

    @Override
    public FriendsStatusResponse addFriendRequest(FriendRequestsDTO request) {
        //request da duoc gui
        if (!friendRequestExisted(request.getReceiverId()).isEmpty())
            throw new AppException(ErrorCode.FRIEND_REQUEST_EXISTED);

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
    public void deleteFriendRequest(String friendRequestId) {
        //chua ton tai loi moi ket ban
        Optional<FriendRequests> optionalFriendRequests = friendRequestsRepository.findById(friendRequestId);
        if (
                optionalFriendRequests.isEmpty()
        ) throw new AppException(ErrorCode.FRIEND_REQUEST_NOT_EXISTED);

        friendRequestsRepository.deleteById(friendRequestId);
    }

    @Override
    public void unfriend(String friendId) {
        if(!existsByUserIdAndFriendId(friendId)){
            throw new AppException(ErrorCode.FRIEND_NOT_EXISTED);
        }
        String userId = getUserId();
        Friends friends = friendsRepository.findByUserIdAndFriendId(userId, friendId);
        friendsRepository.deleteById(friends.getId());

        friends = friendsRepository.findByUserIdAndFriendId(friendId, userId);
        friendsRepository.deleteById(friends.getId());

        FriendRequests friendRequest = friendRequestExisted(friendId).orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_EXISTED));
        friendRequestsRepository.deleteById(friendRequest.getId());
    }

    @Override
    public Boolean existsByUserIdAndFriendId(String friendId) {
        String userId = getUserId();
        return friendsRepository.existsByUserIdAndFriendId(userId, friendId);
    }

    @Override
    public FriendRequestsResponse findBySenderIdAndReceiverId(String receiverId) {
        FriendRequests friendRequest = friendRequestExisted(receiverId).orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_EXISTED));
        return friendRequestsMapper.toFriendRequestResponse(friendRequest);
    }

    public Optional<FriendRequests> friendRequestExisted(String receiverId) {
        String senderId = getUserId();
        Optional<FriendRequests> OptionalFriendRequests = friendRequestsRepository.findTop1BySenderIdAndReceiverIdOrderByUpdatedAtDesc(senderId, receiverId);
        if (OptionalFriendRequests.isEmpty()) {
            OptionalFriendRequests = friendRequestsRepository.findTop1BySenderIdAndReceiverIdOrderByUpdatedAtDesc(receiverId, senderId);
        }

        return OptionalFriendRequests;
    }

    public String getUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return identityClient.getUserByUsername(username).getResult().getId();
    }
}
