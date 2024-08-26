package com.vuhlog.chat.dto.Request;

import com.vuhlog.chat.entity.GroupMember;
import com.vuhlog.chat.entity.Messages;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationsRequest {
    private int type;

    private List<String> membersId;
}
