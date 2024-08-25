package com.vuhlog.chat.service.ServiceImpl;

import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.dto.Response.MessagesResponse;
import com.vuhlog.chat.entity.Conversations;
import com.vuhlog.chat.entity.Messages;
import com.vuhlog.chat.mapper.MessagesMapper;
import com.vuhlog.chat.repository.ConversationsRepository;
import com.vuhlog.chat.repository.MessagesRepository;
import com.vuhlog.chat.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessagesServiceImpl implements MessagesService {
    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private ConversationsRepository conversationsRepository;

    @Autowired
    private MessagesMapper messagesMapper;

    @Override
    public MessagesResponse sendMessage(MessagesRequest request, String conversationId) {
        Messages messages = messagesMapper.toMessages(request);
        messages.setConversation(conversationsRepository.findById(conversationId).get());
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        messages.setSenderId("username");
        messages.setStatus("sent");
        return messagesMapper.toMessagesResponse(messagesRepository.save(messages));
    }
}
