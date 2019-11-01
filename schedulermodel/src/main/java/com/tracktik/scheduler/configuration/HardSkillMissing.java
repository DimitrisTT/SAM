package com.tracktik.scheduler.configuration;

import com.tracktik.scheduler.domain.ConfigFact;

import java.util.Objects;

public class HardSkillMissing extends ConfigFact {

    private int impactMultiplier = -100;
    private boolean active = true;
    private boolean hardImpact = false;
    private boolean hardFailure = true;

    public HardSkillMissing(int impactMultiplier, boolean active, boolean hardImpact, boolean hardFailure) {
        this.impactMultiplier = impactMultiplier;
        this.active = active;
        this.hardImpact = hardImpact;
        this.hardFailure = hardFailure;
    }

    public HardSkillMissing() {

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
        return hardImpact;
    }

    public void setHardImpact(boolean hardImpact) {
        this.hardImpact = hardImpact;
    }

    public boolean isHardFailure() {
        return hardFailure;
    }

    public void setHardFailure(boolean hardFailure) {
        this.hardFailure = hardFailure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HardSkillMissing that = (HardSkillMissing) o;
        return impactMultiplier == that.impactMultiplier &&
                active == that.active &&
                hardImpact == that.hardImpact &&
                hardFailure == that.hardFailure;
    }

    @Override
    public int hashCode() {
        return Objects.hash(impactMultiplier, active, hardImpact, hardFailure);
    }

    @Override
    public String toString() {
        return "SoftSkillMissing{" +
                "impactMultiplier=" + impactMultiplier +
                ", active=" + active +
                ", HardImpact=" + hardImpact +
                ", HardFailure=" + hardFailure +
                '}';
    }
}
