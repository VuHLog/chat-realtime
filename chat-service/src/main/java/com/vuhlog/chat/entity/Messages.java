package com.vuhlog.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

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
    private String senderId;

    @Column
    private String content;

    @Column
    private String contentType;

    @Column
    private Timestamp timeSent;

    @Column
    private String status;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversations conversation;

    @OneToMany(mappedBy = "message")
    private Set<ReadReceipts> readReceipts;

    @PrePersist
    protected void onCreate() {
        timeSent = Timestamp.valueOf(LocalDateTime.now());
    }
}
