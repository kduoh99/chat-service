package com.study.chatserver.chat.domain;

import java.util.ArrayList;
import java.util.List;

import com.study.chatserver.global.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_id")
	private Long id;

	@Column(nullable = false)
	private String name;

	private String isGroupChat;

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
	private List<ChatParticipant> chatParticipants = new ArrayList<>();

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<ChatMessage> chatMessages = new ArrayList<>();

	@Builder
	private ChatRoom(String name, String isGroupChat) {
		this.name = name;
		this.isGroupChat = isGroupChat;
	}
}
