package com.simple.sns.controller;

import com.simple.sns.controller.response.AlarmResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.simple.sns.controller.request.UserJoinRequest;
import com.simple.sns.controller.request.UserLoginRequest;
import com.simple.sns.controller.response.UserLoginResponse;
import com.simple.sns.controller.response.Response;
import com.simple.sns.controller.response.UserJoinResponse;
import com.simple.sns.model.User;
import com.simple.sns.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/join")
	public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
		User user = userService.join(request.getName(), request.getPassword());
		return Response.sucess(UserJoinResponse.fromUser(user));
	}

	@PostMapping("/login")
	public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
		String token = userService.login(request.getName(), request.getPassword());
		return Response.sucess(new UserLoginResponse(token));
	}

	@GetMapping("/alarm")
	public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
		return Response.sucess(userService.alarmList(authentication.getName(), pageable).map(AlarmResponse::fromEntity));
	}
}
