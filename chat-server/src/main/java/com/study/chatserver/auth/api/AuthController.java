package com.study.chatserver.auth.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.chatserver.auth.api.dto.request.LoginReqDto;
import com.study.chatserver.auth.api.dto.response.LoginResDto;
import com.study.chatserver.auth.application.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto loginReqDto) {
		return ResponseEntity.ok(authService.login(loginReqDto));
	}
}
