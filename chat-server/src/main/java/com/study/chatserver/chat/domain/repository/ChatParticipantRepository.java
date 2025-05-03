package com.study.chatserver.chat.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.chatserver.chat.domain.ChatParticipant;
import com.study.chatserver.chat.domain.ChatRoom;
import com.study.chatserver.member.domain.Member;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

	List<ChatParticipant> findAllByMember(Member member);
	List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);
	Optional<ChatParticipant> findByChatRoomAndMember(ChatRoom chatRoom, Member member);
}
