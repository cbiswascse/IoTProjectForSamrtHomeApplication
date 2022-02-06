package com.iot.alarmintrusion;

public class SecurityDoorState {
    public boolean isSecurityNeed() {
        return securityNeed;
    }

    public void setSecurityNeed(boolean securityNeed) {
        this.securityNeed = securityNeed;
    }

    private boolean securityNeed;

    public boolean isSecurityBreak() {
        return securityBreak;
    }

    public void setSecurityBreak(boolean securityBreak) {
        this.securityBreak = securityBreak;
    }

    private boolean securityBreak;

    public int getSecurityBreakCounter() {
        return securityBreakCounter;
    }

    public void setSecurityBreakCounter(int securityBreakCounter) {
        this.securityBreakCounter = securityBreakCounter;
    }

    private int securityBreakCounter;

    public void setDoorOpen(boolean doorOpen) {
        this.doorOpen = doorOpen;
    }

    public boolean isDoorOpen() {
        return doorOpen;
    }

    private boolean doorOpen;
}
