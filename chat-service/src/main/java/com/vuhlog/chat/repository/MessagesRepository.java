package com.vuhlog.chat.repository;

import com.vuhlog.chat.entity.Messages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MessagesRepository extends JpaRepository<Messages, String>, JpaSpecificationExecutor<Messages> {
    Page<Messages> findByConversation_IdOrderByTimeSentDesc(String conversationId, Pageable pageable);

    Optional<Messages> findTop1ByConversation_IdOrderByTimeSentDesc(String conversationId);
}
