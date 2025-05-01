package com.study.chatserver.chat.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.chatserver.chat.domain.ChatParticipant;
import com.study.chatserver.chat.domain.ChatRoom;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

	List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);
}
