package com.vuhlog.chat.service.ServiceImpl;

import com.vuhlog.chat.dto.Request.ConversationsRequest;
import com.vuhlog.chat.dto.Response.ConversationsResponse;
import com.vuhlog.chat.dto.Response.LatestConversationResponse;
import com.vuhlog.chat.entity.Conversations;
import com.vuhlog.chat.entity.GroupMember;
import com.vuhlog.chat.exception.AppException;
import com.vuhlog.chat.exception.ErrorCode;
import com.vuhlog.chat.mapper.ConversationsMapper;
import com.vuhlog.chat.repository.ConversationsRepository;
import com.vuhlog.chat.repository.GroupMemberRepository;
import com.vuhlog.chat.repository.httpClients.IdentityClient;
import com.vuhlog.chat.service.ConversationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

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
        String userId = getMyUserId();

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
    public void deleteConversation(String conversationId) {
        conversationsRepository.findById(conversationId).ifPresent(conversation -> conversationsRepository.deleteById(conversationId));
    }

    @Override
    public ConversationsResponse getConversationById(String conversationId) {
        Conversations conversation = conversationsRepository.findById(conversationId).orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_EXISTED));
        String userId = getMyUserId();
        List<GroupMember> groupMemberList = new ArrayList<>(conversation.getGroupMembers());
        List<GroupMember> groupMembersCheck = groupMemberList.stream().filter(gm -> gm.getUserId().equals(userId)).toList();
        if(groupMembersCheck.isEmpty()){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return conversationsMapper.toConversationsResponse(conversation);
    }

    @Override
    public Page<ConversationsResponse> getMyConversations(Pageable pageable) {
        String userId = getMyUserId();

        return conversationsRepository.findByGroupMembers_UserId(userId, pageable).map(conversationsMapper::toConversationsResponse);
    }

    @Override
    public LatestConversationResponse getLatestConversation() {
        String userId = getMyUserId();
        return conversationsMapper.toLatestConversationResponse(conversationsRepository.findTop1ByGroupMembers_UserIdOrderByUpdatedAtDesc(userId));
    }

    @Override
    public ConversationsResponse getPrivateMessageByMemberIdAndType(String userId, String receiverId, int type) {
        Conversations conversation = conversationsRepository.findPrivateMessageByMembersIdAndType(userId, receiverId, type);
        return conversationsMapper.toConversationsResponse(conversation);
    }

    private String getMyUserId(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return identityClient.getUserByUsername(username).getResult().getId();
    }

    @Override
    public boolean isInConversation(String conversationId) {
        Conversations conversation = conversationsRepository.findById(conversationId).orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_EXISTED));
        String userId = getMyUserId();
        List<GroupMember> groupMemberList = new ArrayList<>(conversation.getGroupMembers());
        List<GroupMember> groupMembersCheck = groupMemberList.stream().filter(gm -> gm.getUserId().equals(userId)).toList();
        return !groupMembersCheck.isEmpty();
    }
}
