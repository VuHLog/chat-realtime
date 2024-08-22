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
public class Conversations {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String type;

    @Column
    private String creator_id;

    @Column
    private Timestamp created_at;

    @Column
    private Timestamp updated_at;

    @Column
    private String lastMessageId;
}
