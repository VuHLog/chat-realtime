package com.vuhlog.chat.repository;

import com.vuhlog.chat.entity.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<Messages, String> {
}
