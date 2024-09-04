package com.vuhlog.chat.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessagesRequest {
    private String content;

    private String contentType;

    private String url;

    private String conversationId;

//    private Set<ReadReceipts> readReceipts;
}
