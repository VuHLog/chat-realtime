package com.vuhlog.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Builder
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String senderId;

    @Column
    private String receiverId;

    @Column
    private String content;

    @Column
    private String notificationType;

    @Column
    private String href;

    @Column
    private Timestamp timestamp;

    @Column
    private String status;

    @PrePersist
    protected void onCreate() {
        timestamp = Timestamp.valueOf(LocalDateTime.now());
    }
}
