package com.iot.alarmintrusion;

public class MotionSensorRequest {
    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    private boolean on;
}
