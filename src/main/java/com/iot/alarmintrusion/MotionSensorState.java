package com.iot.alarmintrusion;

public class MotionSensorState {

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    private boolean isExist;

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    private boolean on;
}
