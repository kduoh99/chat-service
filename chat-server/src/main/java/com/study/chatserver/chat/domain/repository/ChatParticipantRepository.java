package com.study.chatserver.chat.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.study.chatserver.chat.domain.ChatParticipant;
import com.study.chatserver.chat.domain.ChatRoom;
import com.study.chatserver.member.domain.Member;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

	List<ChatParticipant> findAllByMember(Member member);
	List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);
	Optional<ChatParticipant> findByChatRoomAndMember(ChatRoom chatRoom, Member member);

	@Query("SELECT cp1.chatRoom FROM ChatParticipant cp1 JOIN ChatParticipant cp2 ON cp1.chatRoom.id = cp2.chatRoom.id WHERE cp1.member.id = :memberId AND cp2.member.id = :otherMemberId AND cp1.chatRoom.isGroupChat = 'N'")
	Optional<ChatRoom> findExistingPrivateRoom(@Param("memberId") Long memberId, @Param("otherMemberId") Long otherMemberId);
}
