package com.simple.sns.repository;

import com.simple.sns.model.entity.AlarmEntity;
import com.simple.sns.model.entity.LikeEntity;
import com.simple.sns.model.entity.PostEntity;
import com.simple.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlarmEntityRepository extends JpaRepository<AlarmEntity,Integer> {
	Page<AlarmEntity> findAllByUserId(Integer userId, Pageable pageable);
}
