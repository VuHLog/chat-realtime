package com.vuhlog.chat.service.ServiceImpl;

import com.vuhlog.chat.dto.Request.ConversationsRequest;
import com.vuhlog.chat.dto.Response.ConversationsResponse;
import com.vuhlog.chat.entity.Conversations;
import com.vuhlog.chat.entity.GroupMember;
import com.vuhlog.chat.mapper.ConversationsMapper;
import com.vuhlog.chat.repository.ConversationsRepository;
import com.vuhlog.chat.repository.GroupMemberRepository;
import com.vuhlog.chat.repository.httpClients.IdentityClient;
import com.vuhlog.chat.service.ConversationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ConversationsServiceImpl implements ConversationsService {
    @Autowired
    private ConversationsRepository conversationsRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private IdentityClient identityClient;

    @Autowired
    private ConversationsMapper conversationsMapper;

    @Override
    public ConversationsResponse addConversation(ConversationsRequest request) {
        String usernameCreator = SecurityContextHolder.getContext().getAuthentication().getName();
        String userId = identityClient.getUserByUsername(usernameCreator).getResult().getId();

        if (request.getType() == 1) {
            ConversationsResponse conversationsResponse =
                    getPrivateMessageByMemberIdAndType(userId, request.getMembersId().get(0), 1);
            if (conversationsResponse != null) {
                return conversationsResponse;
            }
        }

        Conversations conversation = conversationsMapper.toConversations(request);
        conversation.setCreatorId(userId);
        Set<GroupMember> groupMembers = new HashSet<>();
        request.getMembersId().forEach(id -> groupMembers.add(GroupMember.builder()
                .userId(id)
                .conversation(conversation)
                .build()));
        groupMembers.add(GroupMember.builder()
                .userId(userId)
                .conversation(conversation)
                .build());

        conversation.setGroupMembers(groupMembers);
        return conversationsMapper.toConversationsResponse(conversationsRepository.save(conversation));
    }

    @Override
    public ConversationsResponse getConversationById(String conversationId) {
        Conversations conversation = conversationsRepository.findById(conversationId).get();
        return conversationsMapper.toConversationsResponse(conversation);
    }

    @Override
    public ConversationsResponse getPrivateMessageByMemberIdAndType(String userId, String receiverId, int type) {
        Conversations conversation = conversationsRepository.findPrivateMessageByMembersIdAndType(userId, receiverId, type);
        return conversationsMapper.toConversationsResponse(conversation);
    }
}
