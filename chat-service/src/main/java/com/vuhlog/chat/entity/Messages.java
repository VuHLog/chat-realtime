package com.vuhlog.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String conversationId;

    @Column
    private String senderId;

    @Column
    private String content;

    @Column
    private String contentType;

    @Column
    private Timestamp timeSent;

    @Column
    private String status;
}
