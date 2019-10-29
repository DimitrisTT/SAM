package com.tracktik.scheduler.domain;

import java.util.Objects;

public class MoreThanExpectedHours {

    private int impact = -10;
    private boolean active = true;
    private boolean isHardImpact = false;

    public MoreThanExpectedHours(int impact, boolean active, boolean isHardImpact) {
        this.impact = impact;
        this.active = active;
        this.isHardImpact = isHardImpact;
    }

    public int getImpact() {
        return impact;
    }

    public void setImpact(int impact) {
        this.impact = impact;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isHardImpact() {
        return isHardImpact;
    }

    public void setHardImpact(boolean hardImpact) {
        isHardImpact = hardImpact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoreThanExpectedHours that = (MoreThanExpectedHours) o;
        return impact == that.impact &&
                active == that.active &&
                isHardImpact == that.isHardImpact;
    }

    @Override
    public int hashCode() {
        return Objects.hash(impact, active, isHardImpact);
    }

    @Override
    public String toString() {
        return "LessThanExpectedHours{" +
                "impact=" + impact +
                ", active=" + active +
                ", isHardImpact=" + isHardImpact +
                '}';
    }
}
