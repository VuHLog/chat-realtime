package com.vuhlog.chat.repository;

import com.vuhlog.chat.entity.Conversations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConversationsRepository extends JpaRepository<Conversations, String> {
    @Query(value = "SELECT c.*\n" +
            "FROM group_member AS gm1\n" +
            "INNER JOIN (\n" +
            "\tSELECT gm.conversation_id, gm.user_id AS receiver_id FROM group_member AS gm\n" +
            "\tWHERE gm.user_id = :receiverId\n" +
            ") AS gm2\tON gm1.conversation_id = gm2.conversation_id\n" +
            "INNER JOIN conversations AS c ON c.id = gm1.conversation_id \n" +
            "WHERE gm1.user_id = :userId AND c.type = :type", nativeQuery = true)
    Conversations findPrivateMessageByMembersIdAndType(
            @Param("userId") String userId, @Param("receiverId") String receiverId, @Param("type") int type
    );

    Page<Conversations> findByGroupMembers_UserId(@Param("userId") String userId, Pageable pageable);
}
