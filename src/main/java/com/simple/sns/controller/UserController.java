package com.simple.sns.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
