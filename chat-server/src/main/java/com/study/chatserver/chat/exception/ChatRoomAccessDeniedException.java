package com.study.chatserver.chat.exception;

import com.study.chatserver.global.error.exception.AccessDeniedGroupException;

public class ChatRoomAccessDeniedException extends AccessDeniedGroupException {
	public ChatRoomAccessDeniedException(String message) {
		super(message);
	}

	public ChatRoomAccessDeniedException() {
		this("본인이 속하지 않은 채팅방입니다.");
	}
}
