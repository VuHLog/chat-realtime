package com.vuhlog.chat.service.ServiceImpl;

import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.dto.Response.MessagesResponse;
import com.vuhlog.chat.entity.Conversations;
import com.vuhlog.chat.entity.Messages;
import com.vuhlog.chat.mapper.MessagesMapper;
import com.vuhlog.chat.repository.ConversationsRepository;
import com.vuhlog.chat.repository.MessagesRepository;
import com.vuhlog.chat.repository.httpClients.IdentityClient;
import com.vuhlog.chat.repository.specification.MessageSpecification;
import com.vuhlog.chat.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public MessagesResponse saveMessage(MessagesRequest request) {
        Conversations conversation = conversationsRepository.findById(request.getConversationId()).get();
        Messages messages = messagesMapper.toMessages(request);
        messages.setConversation(conversation);

        messages.setStatus("sent");
        messages = messagesRepository.save(messages);

        //update conversation
        conversation.setLastMessageId(messages.getId());
        conversationsRepository.save(conversation);

        return messagesMapper.toMessagesResponse(messages);
    }

    @Override
    public MessagesResponse getMessageById(String messageId) {
        Messages message = messagesRepository.findById(messageId).orElse(null);
        return messagesMapper.toMessagesResponse(message);
    }

    @Override
    public Page<MessagesResponse> getMessagesByConversationIdOrderByTimeSentDesc(Integer pageNumber, Integer pageSize, String sort, String text, String conversationId) {
        Sort sortable = Sort.by("timeSent").descending();
        if(text !=null && text.length()>1){
            Specification<Messages> specs = Specification.where(null);
            specs = specs.and(MessageSpecification.fromConversation(conversationId).and(MessageSpecification.like(text)));
            Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sortable);
            return messagesRepository.findAll(specs,pageable).map(messagesMapper::toMessagesResponse);
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortable);
        return messagesRepository.findByConversation_IdOrderByTimeSentDesc(conversationId, pageable).map(messagesMapper::toMessagesResponse);
    }

    @Override
    public MessagesResponse MessageResponseWS(String messageId) {
        return getMessageById(messageId);
    }

    @Override
    public void deleteMessageById(String messageId) {
        //assign lastMessageId
        messagesRepository.findById(messageId).flatMap(message -> conversationsRepository.findById(message.getConversation().getId()))
                .ifPresent(
                        conversation -> {
                            messagesRepository.deleteById(messageId);
                            if (messageId.equals(conversation.getLastMessageId())) {
                                Optional<Messages> messagesOptional = messagesRepository.findTop1ByConversation_IdOrderByTimeSentDesc(conversation.getId());
                                conversation.setLastMessageId(messagesOptional.map(Messages::getId).orElse(null));
                                conversationsRepository.save(conversation);
                            }
                        }
                );
    }
}
