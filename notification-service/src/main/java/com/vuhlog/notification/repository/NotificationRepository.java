package com.vuhlog.notification.repository;

import com.vuhlog.notification.entity.Notifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, String> {
    Page<Notifications> findByReceiverId(String receiverId, Pageable pageable);
}
