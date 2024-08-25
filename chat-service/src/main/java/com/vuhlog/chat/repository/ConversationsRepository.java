package com.vuhlog.chat.repository;

import com.vuhlog.chat.entity.Conversations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationsRepository extends JpaRepository<Conversations, String> {
}
