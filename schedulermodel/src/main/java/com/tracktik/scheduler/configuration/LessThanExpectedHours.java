package com.tracktik.scheduler.configuration;

import com.tracktik.scheduler.domain.ConfigFact;

import java.util.Objects;

/**
 * This is a class to house the fields of a Less Than Expected Hours requestFact
 *
 */
public class LessThanExpectedHours extends ConfigFact {

    private int impact = -10;
    private boolean active = true;
    private boolean hardImpact = false;

    /* This is the basic constructor
     * @param impact is the integer value of the multiplier to be set
     * @param active is to toggle the requestfact being set to active
     * @param hardImpact is to set whether the fact has a hard or soft impact
     * @param hardFailure is to toggle whether the facts hard setting cannot be ignored
     */
     public LessThanExpectedHours(int impact, boolean active, boolean hardImpact) {
        this.impact = impact;
        this.active = active;
        this.hardImpact = hardImpact;
    }

    public LessThanExpectedHours() {

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
        return hardImpact;
    }

    public void setHardImpact(boolean hardImpact) {
        this.hardImpact = hardImpact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessThanExpectedHours that = (LessThanExpectedHours) o;
        return impact == that.impact &&
                active == that.active &&
                hardImpact == that.hardImpact;
    }

    @Override
    public int hashCode() {
        return Objects.hash(impact, active, hardImpact);
    }

    @Override
    public String toString() {
        return "LessThanExpectedHours{" +
                "impact=" + impact +
                ", active=" + active +
                ", hardImpact=" + hardImpact +
                '}';
    }
}
