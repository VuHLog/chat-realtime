package com.vuhlog.chat.repository.specification;

import com.vuhlog.chat.entity.Messages;
import org.springframework.data.jpa.domain.Specification;

public class MessageSpecification {
    public static Specification<Messages> like(String text){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("content"), "%"+text+"%");
    }

    public static Specification<Messages> fromConversation(String conversationId){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("conversation").get("id"), "%"+conversationId+"%");
    }
}
