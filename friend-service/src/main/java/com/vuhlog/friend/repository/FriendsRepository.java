package com.vuhlog.friend.repository;

import com.vuhlog.friend.entity.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<Friends, String> {
    boolean existsByUserIdAndFriendId(String userId, String friendsId);

    Friends findByUserIdAndFriendId(String userId, String friendsId);
}
