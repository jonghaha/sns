package com.simple.sns.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.sns.controller.request.PostCreateRequest;
import com.simple.sns.controller.response.Response;
import com.simple.sns.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping
	public Response<Void> create(@RequestBody PostCreateRequest request) {
		postService.create(request.getTitle(), request.getBody(), "");
		return Response.sucess();
	}
}
