package com.study.chatserver.global.error.dto;

public record ErrorResponse(
        int statusCode,
        String message
) {
}
