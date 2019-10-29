package com.tracktik.scheduler.domain;

import java.util.Objects;

public class HardSkillMissing {

    private int impactMultiplier = -100;
    private boolean active = true;
    private boolean isHardImpact = false;
    private boolean isHardFailure = true;

    public HardSkillMissing(int impactMultiplier, boolean active, boolean isHardImpact, boolean isHardFailure) {
        this.impactMultiplier = impactMultiplier;
        this.active = active;
        this.isHardImpact = isHardImpact;
        this.isHardFailure = isHardFailure;
    }

    public int getImpactMultiplier() {
        return impactMultiplier;
    }

    public void setImpactMultiplier(int impactMultiplier) {
        this.impactMultiplier = impactMultiplier;
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

    public boolean isHardFailure() {
        return isHardFailure;
    }

    public void setHardFailure(boolean hardFailure) {
        isHardFailure = hardFailure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HardSkillMissing that = (HardSkillMissing) o;
        return impactMultiplier == that.impactMultiplier &&
                active == that.active &&
                isHardImpact == that.isHardImpact &&
                isHardFailure == that.isHardFailure;
    }

    @Override
    public int hashCode() {
        return Objects.hash(impactMultiplier, active, isHardImpact, isHardFailure);
    }

    @Override
    public String toString() {
        return "SoftSkillMissing{" +
                "impactMultiplier=" + impactMultiplier +
                ", active=" + active +
                ", isHardImpact=" + isHardImpact +
                ", isHardFailure=" + isHardFailure +
                '}';
    }
}
