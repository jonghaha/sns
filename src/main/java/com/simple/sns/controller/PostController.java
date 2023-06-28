package com.simple.sns.controller;

import com.simple.sns.controller.request.PostCommentRequest;
import com.simple.sns.controller.request.PostCreateRequest;
import com.simple.sns.controller.request.PostModifyRequest;
import com.simple.sns.controller.response.CommentResponse;
import com.simple.sns.controller.response.PostResponse;
import com.simple.sns.controller.response.Response;
import com.simple.sns.model.Post;
import com.simple.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping
	public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {
		postService.create(request.getTitle(), request.getBody(), authentication.getName());
		return Response.sucess();
	}

	@PutMapping("/{postId}")
	public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request,
										 Authentication authentication) {
		Post post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);
		return Response.sucess(PostResponse.fromPost(post));
	}

	@DeleteMapping("/{postId}")
	public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
		postService.delete(authentication.getName(), postId);
		return Response.sucess();
	}

	@GetMapping
	public Response<Page<PostResponse>> list(Pageable pageable, Authentication authentication) {
		return Response.sucess(postService.list(pageable).map(PostResponse::fromPost));
	}

	@GetMapping("/my")
	public Response<Page<PostResponse>> my(Pageable pageable, Authentication authentication) {
		return Response.sucess(postService.my(authentication.getName(), pageable).map(PostResponse::fromPost));
	}

	@PostMapping("/{postId}/likes")
	public Response<Void> like(@PathVariable Integer postId, Authentication authentication) {
		postService.like(postId, authentication.getName());
		return Response.sucess();
	}

	@GetMapping("/{postId}/likes")
	public Response<Long> likeCount(@PathVariable Integer postId, Authentication authentication) {
		return Response.sucess(postService.likeCount(postId));
	}

	@PostMapping("/{postId}/comments")
	public Response<Void> comment(@PathVariable Integer postId, @RequestBody PostCommentRequest request, Authentication authentication) {
		postService.comment(postId, authentication.getName(), request.getComment());
		return Response.sucess();
	}
	@GetMapping("/{postId}/comments")
	public Response<Page<CommentResponse>> comment(@PathVariable Integer postId, Pageable pageable, Authentication authentication) {
		return Response.sucess(postService.getComments(postId, pageable).map(CommentResponse::fromComment));
	}
}
