package com.study.chatserver.chat.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
		return ResponseEntity.noContent().build();
	}
}
