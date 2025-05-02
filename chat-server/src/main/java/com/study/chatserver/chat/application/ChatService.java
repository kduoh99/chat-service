package com.study.chatserver.chat.application;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.chatserver.chat.api.dto.request.MessageReqDto;
import com.study.chatserver.chat.api.dto.response.GroupRoomInfoResDto;
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

	@Transactional
	public void saveMessage(Long roomId, MessageReqDto messageReqDto) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));

		Member sender = memberRepository.findByEmail(messageReqDto.sender())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		ChatMessage chatMessage = chatMessageRepository.save(ChatMessage.builder()
			.chatRoom(chatRoom)
			.member(sender)
			.content(messageReqDto.message())
			.build());

		chatParticipantRepository.findByChatRoom(chatRoom)
			.forEach(c ->
				readStatusRepository.save(ReadStatus.builder()
					.chatRoom(chatRoom)
					.member(c.getMember())
					.chatMessage(chatMessage)
					.isRead(c.getMember().equals(sender))
					.build()));
	}

	@Transactional
	public void createGroupChatRoom(String roomName) {
		Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		chatParticipantRepository.save(ChatParticipant.builder()
			.chatRoom(chatRoomRepository.save(ChatRoom.builder()
				.name(roomName)
				.isGroupChat("Y")
				.build()))
			.member(member)
			.build());
	}

	public List<GroupRoomInfoResDto> getGroupChatRooms() {
		return chatRoomRepository.findByIsGroupChat("Y").stream()
			.map(c -> GroupRoomInfoResDto.of(c.getId(), c.getName()))
			.toList();
	}

	@Transactional
	public void joinGroupChatRoom(Long roomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));

		Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		chatParticipantRepository.findByChatRoomAndMember(chatRoom, member)
			.or(() -> Optional.of(registerParticipant(chatRoom, member)));
	}

	private ChatParticipant registerParticipant(ChatRoom chatRoom, Member member) {
		return chatParticipantRepository.save(ChatParticipant.builder()
			.chatRoom(chatRoom)
			.member(member)
			.build());
	}
}
