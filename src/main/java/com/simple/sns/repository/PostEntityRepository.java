package com.simple.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.sns.model.entity.PostEntity;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity,Integer> {
}
