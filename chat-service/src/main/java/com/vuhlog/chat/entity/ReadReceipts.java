package com.vuhlog.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class ReadReceipts {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Messages message;

    @Column
    private String userId;

    @Column
    private Timestamp readAt;
}
