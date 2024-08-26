package com.vuhlog.chat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String userId;

    @Column
    private Timestamp addedAt;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    @JsonBackReference
    private Conversations conversation;

    @PrePersist
    public void onCreate() {
        addedAt = Timestamp.valueOf(LocalDateTime.now());
    }
}
