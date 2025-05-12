package com.study.chatserver.chat.application;

import java.util.List;
import java.util.Optional;

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
import com.study.chatserver.chat.exception.ChatRoomAccessDeniedException;
import com.study.chatserver.chat.exception.ChatParticipantNotFoundException;
import com.study.chatserver.chat.exception.ChatRoomNotFoundException;
import com.study.chatserver.chat.exception.InvalidChatRoomTypeException;
import com.study.chatserver.member.domain.Member;
import com.study.chatserver.member.domain.repository.MemberRepository;
import com.study.chatserver.member.exception.MemberNotFoundException;
import com.study.chatserver.notification.api.dto.response.UnreadCountResDto;
import com.study.chatserver.notification.application.pubsub.NotificationPublisher;

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
	private final NotificationPublisher notificationPublisher;

	// 메시지 저장 -> 읽음 처리 -> 알림 전송
	@Transactional
	public void saveMessage(Long roomId, MessageDto messageDto) {
		ChatRoom chatRoom = getChatRoom(roomId);
		Member sender = getMemberByEmail(messageDto.sender());
		ChatMessage chatMessage = saveChatMessage(chatRoom, sender, messageDto.message());
		saveReadStatuses(chatRoom, chatMessage, sender);
		publishUnreadCounts(chatRoom, sender);
	}

	// 그룹 채팅방 생성 -> 사용자 참여 등록
	@Transactional
	public void createGroupChatRoom(String roomName) {
		Member member = getCurrentMember();
		ChatRoom chatRoom = createGroupRoom(roomName);
		registerParticipant(chatRoom, member);
	}

	// 내 채팅방 목록 조회
	public List<MyRoomInfoResDto> getMyChatRooms() {
		Member member = getCurrentMember();
		List<ChatParticipant> participants = getMyParticipants(member);
		return convertToMyRoomInfoList(participants, member);
	}

	// 그룹 채팅방 목록 조회
	public List<GroupRoomInfoResDto> getGroupChatRooms() {
		return chatRoomRepository.findByIsGroupChat("Y").stream()
			.map(c -> GroupRoomInfoResDto.of(c.getId(), c.getName()))
			.toList();
	}

	// 그룹 채팅방 참여
	@Transactional
	public void joinGroupChatRoom(Long roomId) {
		ChatRoom chatRoom = getChatRoom(roomId);
		validateGroupChatRoom(chatRoom);

		Member member = getCurrentMember();

		if (!isParticipant(chatRoom, member)) {
			registerParticipant(chatRoom, member);
		}
	}

	// 채팅방 메시지 목록 조회
	public List<MessageDto> getChatHistory(Long roomId) {
		ChatRoom chatRoom = getChatRoom(roomId);
		Member member = getCurrentMember();
		validateRoomParticipant(chatRoom, member);

		return chatMessageRepository.findByChatRoomOrderByCreatedAtAsc(chatRoom).stream()
			.map(c -> MessageDto.of(c.getContent(), c.getMember().getEmail()))
			.toList();
	}

	// 채팅방 참여자 여부 확인
	public boolean isRoomParticipant(String email, Long roomId) {
		ChatRoom chatRoom = getChatRoom(roomId);
		Member member = getMemberByEmail(email);
		return isParticipant(chatRoom, member);
	}

	// 메시지 읽음 처리
	@Transactional
	public void readMessage(Long roomId) {
		ChatRoom chatRoom = getChatRoom(roomId);
		Member member = getCurrentMember();
		markMessagesAsRead(chatRoom, member);
	}

	// 그룹 채팅방 나가기 -> 참여자 삭제 -> 참여자 없으면 방 삭제
	@Transactional
	public void leaveGroupChatRoom(Long roomId) {
		ChatRoom chatRoom = getChatRoom(roomId);
		validateGroupChatRoom(chatRoom);

		Member member = getCurrentMember();
		ChatParticipant participant = getParticipant(chatRoom, member);

		removeParticipant(participant);
		deleteRoomIfEmpty(chatRoom);
	}

	// 1:1 채팅방 조회 또는 생성 후 ID 반환
	@Transactional
	public Long getOrCreatePrivateChatRoom(Long otherMemberId) {
		Member member = getCurrentMember();
		Member otherMember = getMemberById(otherMemberId);

		return findExistingPrivateRoom(member, otherMember)
			.map(ChatRoom::getId)
			.orElseGet(() -> createPrivateRoom(member, otherMember).getId());
	}

	// 채팅방 조회
	private ChatRoom getChatRoom(Long roomId) {
		return chatRoomRepository.findById(roomId)
			.orElseThrow(ChatRoomNotFoundException::new);
	}

	// 이메일로 사용자 조회
	private Member getMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(MemberNotFoundException::new);
	}

	// 채팅 메시지 저장
	private ChatMessage saveChatMessage(ChatRoom room, Member sender, String content) {
		return chatMessageRepository.save(ChatMessage.builder()
			.chatRoom(room)
			.member(sender)
			.content(content)
			.build());
	}

	// 참여자별 읽음 상태 저장
	private void saveReadStatuses(ChatRoom chatRoom, ChatMessage chatMessage, Member sender) {
		List<ChatParticipant> participants = chatParticipantRepository.findByChatRoom(chatRoom);
		for (ChatParticipant c : participants) {
			Member receiver = c.getMember();
			boolean isSender = receiver.equals(sender);

			readStatusRepository.save(ReadStatus.builder()
				.chatRoom(chatRoom)
				.member(receiver)
				.chatMessage(chatMessage)
				.isRead(isSender)
				.build());
		}
	}

	// 채팅방별 읽지 않은 메시지 수 알림 전송
	private void publishUnreadCounts(ChatRoom chatRoom, Member sender) {
		List<ChatParticipant> participants = chatParticipantRepository.findByChatRoom(chatRoom);

		participants.stream()
			.map(ChatParticipant::getMember)
			.filter(receiver -> !receiver.equals(sender))
			.forEach(receiver -> {
				List<ChatParticipant> joined = chatParticipantRepository.findAllByMember(receiver);
				List<UnreadCountResDto> counts = joined.stream()
					.map(cp -> {
						ChatRoom r = cp.getChatRoom();
						Long count = readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(r, receiver);
						return UnreadCountResDto.of(r.getId(), count);
					})
					.toList();

				notificationPublisher.publish(String.valueOf(receiver.getId()), counts);
			});
	}

	// 현재 로그인한 사용자 조회
	private Member getCurrentMember() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return getMemberByEmail(email);
	}

	// 그룹 채팅방 생성
	private ChatRoom createGroupRoom(String name) {
		return chatRoomRepository.save(ChatRoom.builder()
			.name(name)
			.isGroupChat("Y")
			.build());
	}

	// 채팅방에 참여자 등록
	private void registerParticipant(ChatRoom chatRoom, Member member) {
		chatParticipantRepository.save(ChatParticipant.builder()
			.chatRoom(chatRoom)
			.member(member)
			.build());
	}

	// 그룹 채팅방인지 검증
	private void validateGroupChatRoom(ChatRoom chatRoom) {
		if ("N".equals(chatRoom.getIsGroupChat())) {
			throw new InvalidChatRoomTypeException();
		}
	}

	// 채팅방 참여 여부 확인
	private boolean isParticipant(ChatRoom chatRoom, Member member) {
		return chatParticipantRepository.findByChatRoomAndMember(chatRoom, member).isPresent();
	}

	// 채팅방 참여자인지 검증
	private void validateRoomParticipant(ChatRoom chatRoom, Member member) {
		chatParticipantRepository.findByChatRoomAndMember(chatRoom, member)
			.orElseThrow(ChatRoomAccessDeniedException::new);
	}

	// 읽지 않은 메시지를 모두 읽음 처리
	private void markMessagesAsRead(ChatRoom chatRoom, Member member) {
		readStatusRepository.findByChatRoomAndMemberAndIsReadFalse(chatRoom, member)
			.forEach(r -> r.updateIsRead(true));
	}

	// 사용자가 참여 중인 채팅방 목록 조회
	private List<ChatParticipant> getMyParticipants(Member member) {
		return chatParticipantRepository.findAllByMember(member);
	}

	// 채팅방 목록을 응답 DTO 리스트로 변환
	private List<MyRoomInfoResDto> convertToMyRoomInfoList(List<ChatParticipant> participants, Member member) {
		return participants.stream()
			.map(c -> {
				ChatRoom chatRoom = c.getChatRoom();
				Long count = readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(chatRoom, member);
				return MyRoomInfoResDto.of(chatRoom.getId(), chatRoom.getName(), chatRoom.getIsGroupChat(), count);
			})
			.toList();
	}

	// 채팅방 참여자 조회
	private ChatParticipant getParticipant(ChatRoom chatRoom, Member member) {
		return chatParticipantRepository.findByChatRoomAndMember(chatRoom, member)
			.orElseThrow(ChatParticipantNotFoundException::new);
	}

	// 채팅방에서 참여자 삭제
	private void removeParticipant(ChatParticipant participant) {
		chatParticipantRepository.delete(participant);
	}

	// 참여자가 없는 채팅방 삭제
	private void deleteRoomIfEmpty(ChatRoom chatRoom) {
		if (chatParticipantRepository.findByChatRoom(chatRoom).isEmpty()) {
			chatRoomRepository.delete(chatRoom);
		}
	}

	// ID로 사용자 조회
	private Member getMemberById(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(MemberNotFoundException::new);
	}

	// 기존 1:1 채팅방 존재 여부 확인
	private Optional<ChatRoom> findExistingPrivateRoom(Member member, Member otherMember) {
		return chatParticipantRepository.findExistingPrivateRoom(member.getId(), otherMember.getId());
	}

	// 1:1 채팅방 생성 및 참여자 등록
	private ChatRoom createPrivateRoom(Member member, Member otherMember) {
		ChatRoom privateRoom = chatRoomRepository.save(ChatRoom.builder()
			.name(member.getName() + "-" + otherMember.getName())
			.isGroupChat("N")
			.build());

		registerParticipant(privateRoom, member);
		registerParticipant(privateRoom, otherMember);
		return privateRoom;
	}
}
