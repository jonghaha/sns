package com.simple.sns.controller.response;

import com.simple.sns.model.Alarm;
import com.simple.sns.model.AlarmArgs;
import com.simple.sns.model.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class AlarmResponse {
    private Integer id;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private String text;
    private Timestamp registerAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmResponse fromEntity(Alarm alarm) {
        return new AlarmResponse(alarm.getId(), alarm.getAlarmType(), alarm.getArgs(), alarm.getAlarmType().getAlarmText(), alarm.getRegisterAt(), alarm.getUpdatedAt(), alarm.getDeletedAt());
    }
}
