package com.vuhlog.chat.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessagesRequest {
    private String id;

    private String senderId;

    private String content;

    private String contentType;

    private String timeSent;

    private String status;

    private String url;

    private String conversationId;

//    private Set<ReadReceipts> readReceipts;
}
