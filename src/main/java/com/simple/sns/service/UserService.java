package com.simple.sns.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simple.sns.exception.ErrorCode;
import com.simple.sns.model.entity.UserEntity;
import com.simple.sns.exception.SnsApplicationException;
import com.simple.sns.model.User;
import com.simple.sns.repository.UserEntityRepository;
import com.simple.sns.util.JwtTokenUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserEntityRepository userEntityRepository;
	private final BCryptPasswordEncoder encoder;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.token.expired-time-ms}")
	private Long expiredTimeMs;

	@Transactional
	public User join(String userName, String password) {
		// 회원가입하려는 userName으로 회원가입된 user가 있는지
		userEntityRepository.findByUserName(userName).ifPresent(it -> {
			throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,
				String.format("%s is duplicated", userName));
		});

		// 회원가입 진행 = user를 등록
		UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));

		return User.fromEntity(userEntity);
	}

	// TODO : implement
	public String login(String userName, String password) {
		// 회원가입 여부 체크
		UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

		// 비밀번호 체크
		if (!encoder.matches(password, userEntity.getPassword())) {
		// if (!userEntity.getPassword().equals(password)) {
			throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
		}

		// 토큰 생성
		String token = JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);

		return token;
	}
}
