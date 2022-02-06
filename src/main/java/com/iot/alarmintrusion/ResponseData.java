package com.iot.alarmintrusion;

public class ResponseData {
    SecurityDoorState doorState;

    public SecurityDoorState getDoorState() {
        return doorState;
    }

    public void setDoorState(SecurityDoorState doorState) {
        this.doorState = doorState;
    }

    public DangerAlarmState getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(DangerAlarmState alarmState) {
        this.alarmState = alarmState;
    }

    DangerAlarmState alarmState;

    public MotionSensorState getMotionSensorState() {
        return motionSensorState;
    }

    public void setMotionSensorState(MotionSensorState motionSensorState) {
        this.motionSensorState = motionSensorState;
    }

    MotionSensorState motionSensorState;
}
