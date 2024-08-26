package com.vuhlog.chat.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessagesWSRequest {
    private String conversationId;

    private String messagesId;
}
