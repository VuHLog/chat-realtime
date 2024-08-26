package com.vuhlog.chat.dto.Response;

import com.vuhlog.chat.entity.Conversations;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMemberResponse {
    private String id;

    private String userId;

    private String addedAt;

    private Conversations conversation;
}
