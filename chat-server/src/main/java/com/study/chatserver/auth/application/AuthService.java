package com.study.chatserver.auth.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.chatserver.auth.api.dto.request.LoginReqDto;
import com.study.chatserver.auth.api.dto.response.LoginResDto;
import com.study.chatserver.global.auth.TokenProvider;
import com.study.chatserver.global.auth.dto.TokenDto;
import com.study.chatserver.member.domain.Member;
import com.study.chatserver.member.domain.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;

	public LoginResDto login(LoginReqDto loginReqDto) {
		Member member = memberRepository.findByEmail(loginReqDto.email())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		if (!passwordEncoder.matches(loginReqDto.password(), member.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		TokenDto tokenDto = tokenProvider.generateToken(member.getEmail(), String.valueOf(member.getRole()));

		return LoginResDto.of(member.getId(), tokenDto.accessToken());
	}
}
