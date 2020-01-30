package com.tracktik.scheduler.configuration;

import com.tracktik.scheduler.domain.ConfigFact;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * This is a class to house the fields of a Can not Work Simultaneous Shifts request fact
 *
 * Methods imported by lombok:
 * Data
 */
@Accessors(chain = true)
@Data
public class CanNotWorkSimultaneousShifts extends ConfigFact {

    private int impactMultiplier = -100;
    private boolean active = true;
    private boolean hardImpact = false;
    private boolean hardFailure = true;

    /* This is the basic constructor
     * @param impactMultiplier is the integer value of the multiplier to be set
     * @param active is to toggle the requestfact being set to active
     * @param hardImpact is to set whether the fact has a hard or soft impact
     * @param hardFailure is to toggle whether the facts hard setting cannot be ignored
     */
    public CanNotWorkSimultaneousShifts(int impactMultiplier, boolean active, boolean hardImpact, boolean hardFailure) {
        this.impactMultiplier = impactMultiplier;
        this.active = active;
        this.hardImpact = hardImpact;
        this.hardFailure = hardFailure;
    }

    public CanNotWorkSimultaneousShifts() {

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
        CanNotWorkSimultaneousShifts that = (CanNotWorkSimultaneousShifts) o;
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
