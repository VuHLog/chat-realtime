package com.vuhlog.chat.entity;

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

    @PrePersist
    public void onCreate() {
        addedAt = Timestamp.valueOf(LocalDateTime.now());
    }
}
