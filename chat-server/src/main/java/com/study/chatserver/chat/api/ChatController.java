package com.study.chatserver.chat.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.chatserver.chat.api.dto.MessageDto;
import com.study.chatserver.chat.api.dto.response.GroupRoomInfoResDto;
import com.study.chatserver.chat.application.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

	private final ChatService chatService;

	@PostMapping("/group/room")
	public ResponseEntity<Void> createGroupChatRoom(@RequestParam String roomName) {
		chatService.createGroupChatRoom(roomName);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/group/room/list")
	public ResponseEntity<List<GroupRoomInfoResDto>> getGroupChatRooms() {
		return ResponseEntity.ok(chatService.getGroupChatRooms());
	}

	@PostMapping("/group/room/{roomId}/join")
	public ResponseEntity<Void> joinGroupChatRoom(@PathVariable Long roomId) {
		chatService.joinGroupChatRoom(roomId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/history/{roomId}")
	public ResponseEntity<List<MessageDto>> getChatHistory(@PathVariable Long roomId) {
		return ResponseEntity.ok(chatService.getChatHistory(roomId));
	}

	@PostMapping("/room/{roomId}/read")
	public ResponseEntity<Void> readMessage(@PathVariable Long roomId) {
		chatService.readMessage(roomId);
		return ResponseEntity.noContent().build();
	}
}
