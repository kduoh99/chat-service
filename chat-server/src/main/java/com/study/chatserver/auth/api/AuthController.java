package com.study.chatserver.auth.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.chatserver.auth.api.dto.request.LoginReqDto;
import com.study.chatserver.auth.api.dto.response.LoginResDto;
import com.study.chatserver.auth.application.AuthService;
import com.study.chatserver.global.auth.dto.TokenDto;
import com.study.chatserver.member.domain.SocialType;

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

	@PostMapping("/callback/{provider}")
	public ResponseEntity<TokenDto> socialCallback(@PathVariable SocialType provider, @RequestParam String code) {
		return ResponseEntity.ok(authService.signupOrLogin(provider, code));
	}
}
