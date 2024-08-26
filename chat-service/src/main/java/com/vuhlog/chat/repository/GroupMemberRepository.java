package com.vuhlog.chat.repository;

import com.vuhlog.chat.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, String> {
}
