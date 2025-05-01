package com.study.chatserver.chat.domain;

import com.study.chatserver.global.entity.BaseEntity;
import com.study.chatserver.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadStatus extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "read_status_id")
	private Long id;

	@Column(nullable = false)
	private Boolean isRead;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoom chatRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_message_id", nullable = false)
	private ChatMessage chatMessage;

	@Builder
	private ReadStatus(Boolean isRead, ChatRoom chatRoom, Member member, ChatMessage chatMessage) {
		this.isRead = isRead;
		this.chatRoom = chatRoom;
		this.member = member;
		this.chatMessage = chatMessage;
	}
}
