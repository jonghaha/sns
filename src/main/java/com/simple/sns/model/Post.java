package com.simple.sns.model;

import java.sql.Timestamp;

import com.simple.sns.model.entity.PostEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Post {

	private Integer id;

	private String title;

	private String body;

	private User user;

	private Timestamp registerAt;

	private Timestamp updatedAt;

	private Timestamp deletedAt;

	public static Post fromEntity(PostEntity entity) {
		return new Post(entity.getId(), entity.getTitle(), entity.getBody(), User.fromEntity(entity.getUser()), entity.getRegisterAt(),
			entity.getUpdatedAt(), entity.getDeletedAt());
	}
}
