package com.tracktik.scheduler.configuration;

import com.tracktik.scheduler.domain.ConfigFact;

import java.util.Objects;

public class SoftSkillMissing extends ConfigFact {

    private int impactMultiplier = -10;
    private boolean active = true;
    private boolean isHardImpact = false;

    public SoftSkillMissing(int impactMultiplier, boolean active, boolean isHardImpact) {
        this.impactMultiplier = impactMultiplier;
        this.active = active;
        this.isHardImpact = isHardImpact;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoftSkillMissing that = (SoftSkillMissing) o;
        return impactMultiplier == that.impactMultiplier &&
                active == that.active &&
                isHardImpact == that.isHardImpact;
    }

    @Override
    public int hashCode() {
        return Objects.hash(impactMultiplier, active, isHardImpact);
    }

    @Override
    public String toString() {
        return "SoftSkillMissing{" +
                "impactMultiplier=" + impactMultiplier +
                ", active=" + active +
                ", isHardImpact=" + isHardImpact +
                '}';
    }
}
