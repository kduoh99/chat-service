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

import com.study.chatserver.chat.api.dto.response.GroupRoomInfoResDto;
import com.study.chatserver.chat.application.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

	private final ChatService chatService;

	@PostMapping("/group/room")
	public ResponseEntity<Void> createGroupRoom(@RequestParam String roomName) {
		chatService.createGroupRoom(roomName);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/group/room/list")
	public ResponseEntity<List<GroupRoomInfoResDto>> getGroupRooms() {
		return ResponseEntity.ok(chatService.getGroupRooms());
	}

	@PostMapping("/group/room/{roomId}/join")
	public ResponseEntity<Void> joinGroupRoom(@PathVariable Long roomId) {
		chatService.joinGroupRoom(roomId);
		return ResponseEntity.noContent().build();
	}
}
