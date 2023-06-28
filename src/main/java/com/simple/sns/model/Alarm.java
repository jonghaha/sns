package com.simple.sns.model;

import com.simple.sns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Alarm {
    private Integer id;
    private AlarmType alarmType;
    private AlarmArgs args;
    private Timestamp registerAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Alarm fromEntity(AlarmEntity entity) {
        return new Alarm(entity.getId(), entity.getAlarmType(), entity.getArgs(), entity.getRegisterAt(), entity.getUpdatedAt(), entity.getDeletedAt());
    }
}
