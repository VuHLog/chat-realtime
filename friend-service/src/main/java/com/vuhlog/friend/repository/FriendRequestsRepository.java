package com.vuhlog.friend.repository;

import com.vuhlog.friend.entity.FriendRequests;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRequestsRepository extends JpaRepository<FriendRequests, String> {
    Optional<FriendRequests> findTop1BySenderIdAndReceiverIdOrderByUpdatedAtDesc(String senderId, String receiverId);
}
