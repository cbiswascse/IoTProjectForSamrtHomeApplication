package com.iot.alarmintrusion;

public class SecurityDoorRequest {
    private String doorPin;
    private boolean securityNeed;

    public String getDoorPin() {
        return doorPin;
    }
    public void setDoorPin(String doorPin) {
        this.doorPin = doorPin;
    }
    public boolean isSecurityNeed() {
        return securityNeed;
    }
    public void setSecurityNeed(boolean securityNeed) {
        this.securityNeed = securityNeed;
    }
}
