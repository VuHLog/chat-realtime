package com.vuhlog.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class Conversations {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String type;

    @Column
    private String creatorId;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;

    @Column
    private String lastMessageId;

    @OneToMany(mappedBy = "conversation")
    private Set<Messages> messages;

    @PrePersist
    protected void onCreate() {
        createdAt = Timestamp.valueOf(LocalDateTime.now());
        updatedAt = Timestamp.valueOf(LocalDateTime.now());
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Timestamp.valueOf(LocalDateTime.now());
    }
}
