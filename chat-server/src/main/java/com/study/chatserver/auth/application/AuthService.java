package com.study.chatserver.auth.application;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.chatserver.auth.api.dto.request.LoginReqDto;
import com.study.chatserver.auth.api.dto.response.LoginResDto;
import com.study.chatserver.auth.application.oauth.OAuthService;
import com.study.chatserver.global.auth.TokenProvider;
import com.study.chatserver.global.auth.dto.TokenDto;
import com.study.chatserver.member.domain.Member;
import com.study.chatserver.member.domain.Role;
import com.study.chatserver.member.domain.SocialType;
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
	private final List<OAuthService> oauthServices;

	public LoginResDto login(LoginReqDto loginReqDto) {
		Member member = memberRepository.findByEmail(loginReqDto.email())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		if (!passwordEncoder.matches(loginReqDto.password(), member.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		TokenDto tokenDto = tokenProvider.generateToken(member.getEmail(), String.valueOf(member.getRole()));

		return LoginResDto.of(member.getId(), tokenDto.accessToken());
	}

	@Transactional
	public TokenDto signupOrLogin(SocialType provider, String code) {
		OAuthService service = oauthServices.stream()
			.filter(s -> s.support() == provider)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 로그인입니다."));

		String accessToken = service.getAccessToken(code);
		String[] memberInfo = service.getMemberInfo(accessToken);

		Member member = memberRepository.findByEmail(memberInfo[1])
			.orElseGet(() -> memberRepository.save(Member.builder()
				.socialId(memberInfo[0])
				.socialType(provider)
				.email(memberInfo[1])
				.name(memberInfo[2])
				.role(Role.USER)
				.build()));

		return tokenProvider.generateToken(member.getEmail(), member.getRole().name());
	}
}
