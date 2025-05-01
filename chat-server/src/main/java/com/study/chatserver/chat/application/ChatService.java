package com.study.chatserver.chat.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.chatserver.chat.api.dto.request.MessageReqDto;
import com.study.chatserver.chat.domain.ChatMessage;
import com.study.chatserver.chat.domain.ChatParticipant;
import com.study.chatserver.chat.domain.ChatRoom;
import com.study.chatserver.chat.domain.ReadStatus;
import com.study.chatserver.chat.domain.repository.ChatMessageRepository;
import com.study.chatserver.chat.domain.repository.ChatParticipantRepository;
import com.study.chatserver.chat.domain.repository.ChatRoomRepository;
import com.study.chatserver.chat.domain.repository.ReadStatusRepository;
import com.study.chatserver.member.domain.Member;
import com.study.chatserver.member.domain.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatParticipantRepository chatParticipantRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final ReadStatusRepository readStatusRepository;
	private final MemberRepository memberRepository;

	public void saveMessage(Long roomId, MessageReqDto messageReqDto) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));

		Member sender = memberRepository.findByEmail(messageReqDto.sender())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		ChatMessage chatMessage = ChatMessage.builder()
			.chatRoom(chatRoom)
			.member(sender)
			.content(messageReqDto.message())
			.build();
		chatMessageRepository.save(chatMessage);

		List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
		for (ChatParticipant c : chatParticipants) {
			ReadStatus readStatus = ReadStatus.builder()
				.chatRoom(chatRoom)
				.member(c.getMember())
				.chatMessage(chatMessage)
				.isRead(c.getMember().equals(sender))
				.build();
			readStatusRepository.save(readStatus);
		}
	}
}
