package com.simple.sns.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.sns.model.entity.UserEntity;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {

	Optional<UserEntity> findByUserName(String userName);
}
