package com.vuhlog.chat.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessagesResponse {
    private String id;

    private String senderId;

    private String content;

    private String contentType;

    private String timeSent;

    private String status;

//    private Conversations conversation;

//    private Set<ReadReceipts> readReceipts;
}
