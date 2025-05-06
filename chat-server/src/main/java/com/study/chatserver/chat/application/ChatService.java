package com.study.chatserver.chat.application;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.chatserver.chat.api.dto.MessageDto;
import com.study.chatserver.chat.api.dto.response.GroupRoomInfoResDto;
import com.study.chatserver.chat.api.dto.response.MyRoomInfoResDto;
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
import com.study.chatserver.notification.api.dto.response.UnreadCountResDto;
import com.study.chatserver.notification.application.SseEmitterManager;

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
	private final SseEmitterManager emitterManager;

	@Transactional
	public void saveMessage(Long roomId, MessageDto messageDto) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));

		Member sender = memberRepository.findByEmail(messageDto.sender())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		ChatMessage chatMessage = chatMessageRepository.save(ChatMessage.builder()
			.chatRoom(chatRoom)
			.member(sender)
			.content(messageDto.message())
			.build());

		chatParticipantRepository.findByChatRoom(chatRoom)
			.forEach(c -> {
				Member receiver = c.getMember();
				boolean isSender = sender.equals(receiver);

				readStatusRepository.save(ReadStatus.builder()
					.chatRoom(chatRoom)
					.member(receiver)
					.chatMessage(chatMessage)
					.isRead(isSender)
					.build());

				if (!isSender) {
					Long count = readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(chatRoom, receiver);
					emitterManager.send(receiver.getId(), receiver.getEmail(), List.of(UnreadCountResDto.of(chatRoom.getId(), count)));
				}
			});
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

		if ("N".equals(chatRoom.getIsGroupChat())) {
			throw new IllegalArgumentException("그룹 채팅방이 아닙니다.");
		}

		if (chatParticipantRepository.findByChatRoomAndMember(chatRoom, member).isEmpty()) {
			registerParticipant(chatRoom, member);
		}
	}

	private void registerParticipant(ChatRoom chatRoom, Member member) {
		chatParticipantRepository.save(ChatParticipant.builder()
			.chatRoom(chatRoom)
			.member(member)
			.build());
	}

	public List<MessageDto> getChatHistory(Long roomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));

		Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		chatParticipantRepository.findByChatRoomAndMember(chatRoom, member)
			.orElseThrow(() -> new IllegalArgumentException("본인이 속하지 않은 채팅방입니다."));

		return chatMessageRepository.findByChatRoomOrderByCreatedAtAsc(chatRoom).stream()
			.map(c -> MessageDto.of(c.getContent(), c.getMember().getEmail()))
			.toList();
	}

	public boolean isRoomParticipant(String email, Long roomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		return chatParticipantRepository.findByChatRoomAndMember(chatRoom, member).isPresent();
	}

	@Transactional
	public void readMessage(Long roomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));

		Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		readStatusRepository.findByChatRoomAndMemberAndIsReadFalse(chatRoom, member)
			.forEach(r -> r.updateIsRead(true));
	}

	public List<MyRoomInfoResDto> getMyChatRooms() {
		Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		return chatParticipantRepository.findAllByMember(member).stream()
			.map(c -> {
				ChatRoom chatRoom = c.getChatRoom();
				Long count = readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(chatRoom, member);
				return MyRoomInfoResDto.of(chatRoom.getId(), chatRoom.getName(), chatRoom.getIsGroupChat(), count);
			})
			.toList();
	}

	@Transactional
	public void leaveGroupChatRoom(Long roomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));

		if ("N".equals(chatRoom.getIsGroupChat())) {
			throw new IllegalArgumentException("그룹 채팅방이 아닙니다.");
		}

		Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		ChatParticipant participant = chatParticipantRepository.findByChatRoomAndMember(chatRoom, member)
			.orElseThrow(() -> new EntityNotFoundException("참여자를 찾을 수 없습니다."));

		chatParticipantRepository.delete(participant);

		if (chatParticipantRepository.findByChatRoom(chatRoom).isEmpty()) {
			chatRoomRepository.delete(chatRoom);
		}
	}

	@Transactional
	public Long getOrCreatePrivateChatRoom(Long otherMemberId) {
		Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		Member otherMember = memberRepository.findById(otherMemberId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		return chatParticipantRepository.findExistingPrivateRoom(member.getId(), otherMember.getId())
			.map(ChatRoom::getId)
			.orElseGet(() -> {
				ChatRoom privateRoom = chatRoomRepository.save(ChatRoom.builder()
					.name(member.getName() + "-" + otherMember.getName())
					.isGroupChat("N")
					.build());

				registerParticipant(privateRoom, member);
				registerParticipant(privateRoom, otherMember);

				return privateRoom.getId();
			});
	}
}
