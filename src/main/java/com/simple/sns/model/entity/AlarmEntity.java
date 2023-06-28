package com.simple.sns.model.entity;

import com.simple.sns.model.AlarmArgs;
import com.simple.sns.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "\"alarm\"", indexes = {
	@Index(name = "user_id_idx", columnList = "user_id")
})
@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@SQLDelete(sql = "UPDATE \"alarm\" SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class AlarmEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// 알람을 받은 사람
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@Enumerated(EnumType.STRING)
	private AlarmType alarmType;

	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	private AlarmArgs args;

	@Column(name = "register_at")
	private Timestamp registerAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "deleted_at")
	private Timestamp deletedAt;

	@PrePersist
	void registerdAt() {
		this.registerAt = Timestamp.from(Instant.now());
	}

	@PreUpdate
	void updatedAt() {
		this.updatedAt = Timestamp.from(Instant.now());
	}

	public static AlarmEntity of(UserEntity userEntity, AlarmType alarmType, AlarmArgs args) {
		AlarmEntity entity = new AlarmEntity();
		entity.setUser(userEntity);
		entity.setAlarmType(alarmType);
		entity.setArgs(args);
		return entity;
	}
}
