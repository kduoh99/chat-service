package com.study.chatserver.chat.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.chatserver.chat.domain.ChatRoom;
import com.study.chatserver.chat.domain.ReadStatus;
import com.study.chatserver.member.domain.Member;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {

	List<ReadStatus> findByChatRoomAndMemberAndIsReadFalse(ChatRoom chatRoom, Member member);
	Long countByChatRoomAndMemberAndIsReadFalse(ChatRoom chatRoom, Member member);
}
