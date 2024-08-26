package com.vuhlog.chat.service.ServiceImpl;

import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.dto.Response.MessagesResponse;
import com.vuhlog.chat.entity.Conversations;
import com.vuhlog.chat.entity.Messages;
import com.vuhlog.chat.mapper.MessagesMapper;
import com.vuhlog.chat.repository.ConversationsRepository;
import com.vuhlog.chat.repository.MessagesRepository;
import com.vuhlog.chat.repository.httpClients.IdentityClient;
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

    @Autowired
    private IdentityClient identityClient;

    @Override
    public MessagesResponse sendMessage(MessagesRequest request, String conversationId) {
        Conversations conversation = conversationsRepository.findById(conversationId).get();
        Messages messages = messagesMapper.toMessages(request);
        messages.setConversation(conversation);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String userId = identityClient.getUserByUsername(username).getResult().getId();
        messages.setSenderId(userId);

        messages.setStatus("sent");
        messages = messagesRepository.save(messages);

        //update conversation
        conversation.setLastMessageId(messages.getId());
        conversationsRepository.save(conversation);

        return messagesMapper.toMessagesResponse(messages);
    }

    @Override
    public MessagesResponse getMessageById(String messageId) {
        Messages message = messagesRepository.findById(messageId).get();
        return messagesMapper.toMessagesResponse(message);
    }

    @Override
    public MessagesResponse MessageResponseWS(String messageId) {
        return getMessageById(messageId);
    }
}