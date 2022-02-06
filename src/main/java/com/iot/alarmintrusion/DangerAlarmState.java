package com.iot.alarmintrusion;

public class DangerAlarmState {

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    private boolean alarm;
}
