package com.simple.sns.model;

import java.sql.Timestamp;

import com.simple.sns.model.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {
	private Integer id;
	private String userName;
	private String password;
	private UserRole userRole;
	private Timestamp registeredAt;
	private Timestamp updatedAt;
	private Timestamp deletedAt;

	public static User fromEntity(UserEntity entity) {
		return new User(
			entity.getId(),
			entity.getUserName(),
			entity.getPassword(),
			entity.getRole(),
			entity.getRegisterAt(),
			entity.getUpdatedAt(),
			entity.getDeletedAt()
		);
	}
}
