package com.study.chatserver.chat.exception;

import com.study.chatserver.global.error.exception.InvalidGroupException;

public class InvalidChatRoomTypeException extends InvalidGroupException {
	public InvalidChatRoomTypeException(String message) {
		super(message);
	}

	public InvalidChatRoomTypeException() {
		this("그룹 채팅방이 아닙니다.");
	}
}
