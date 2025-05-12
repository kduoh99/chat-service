package com.study.chatserver.chat.exception;

import com.study.chatserver.global.error.exception.NotFoundGroupException;

public class ChatRoomNotFoundException extends NotFoundGroupException {
	public ChatRoomNotFoundException(String message) {
		super(message);
	}

	public ChatRoomNotFoundException() {
		this("존재하지 않는 채팅방입니다.");
	}
}
