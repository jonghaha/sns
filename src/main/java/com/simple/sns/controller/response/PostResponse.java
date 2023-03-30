package com.simple.sns.controller.response;

import java.sql.Timestamp;

import com.simple.sns.model.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponse {
	private Integer id;

	private String title;

	private String body;

	private UserResponse user;

	private Timestamp registerAt;

	private Timestamp updatedAt;

	private Timestamp deletedAt;

	public static PostResponse fromPost(Post post) {
		return new PostResponse(post.getId(), post.getTitle(), post.getBody(), UserResponse.fromUser(post.getUser()),
			post.getRegisterAt(), post.getUpdatedAt(), post.getDeletedAt());
	}
}
