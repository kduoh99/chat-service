package com.study.chatserver.auth.application;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.chatserver.auth.api.dto.request.LoginReqDto;
import com.study.chatserver.auth.api.dto.response.LoginResDto;
import com.study.chatserver.auth.application.oauth.OAuthService;
import com.study.chatserver.auth.exception.InvalidPasswordException;
import com.study.chatserver.auth.exception.InvalidSocialProviderException;
import com.study.chatserver.global.jwt.TokenProvider;
import com.study.chatserver.global.jwt.dto.TokenDto;
import com.study.chatserver.member.domain.Member;
import com.study.chatserver.member.domain.Role;
import com.study.chatserver.member.domain.SocialType;
import com.study.chatserver.member.domain.repository.MemberRepository;
import com.study.chatserver.member.exception.MemberNotFoundException;

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
			.orElseThrow(MemberNotFoundException::new);

		if (!passwordEncoder.matches(loginReqDto.password(), member.getPassword())) {
			throw new InvalidPasswordException();
		}

		TokenDto tokenDto = tokenProvider.generateToken(member.getEmail(), String.valueOf(member.getRole()));

		return LoginResDto.of(member.getId(), tokenDto.accessToken());
	}

	@Transactional
	public TokenDto signupOrLogin(SocialType provider, String code) {
		OAuthService service = oauthServices.stream()
			.filter(s -> s.support() == provider)
			.findFirst()
			.orElseThrow(InvalidSocialProviderException::new);

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
