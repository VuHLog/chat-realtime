package com.vuhlog.chat.service.ServiceImpl;

import com.vuhlog.chat.dto.Response.GroupMemberResponse;
import com.vuhlog.chat.entity.GroupMember;
import com.vuhlog.chat.mapper.GroupMemberMapper;
import com.vuhlog.chat.repository.GroupMemberRepository;
import com.vuhlog.chat.service.GroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupMemberServiceImpl implements GroupMemberService {
    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Autowired
    private GroupMemberRepository groupMemberRepository;
}
