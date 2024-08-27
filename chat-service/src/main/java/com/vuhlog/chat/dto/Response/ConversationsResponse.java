package com.vuhlog.chat.dto.Response;

import com.vuhlog.chat.entity.GroupMember;
import com.vuhlog.chat.entity.Messages;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationsResponse {
    private String id;

    private int type;

    private String creatorId;

    private String createdAt;

    private String updatedAt;

    private String lastMessageId;

    private Set<GroupMember> groupMembers;
}
