package com.tracktik.scheduler.configuration;

import com.tracktik.scheduler.domain.ConfigFact;

import java.util.Objects;

/**
 * This is a class to house the fields of a More than expected hours requestFact
 *
 */
public class MoreThanExpectedHours extends ConfigFact {

    private int impact = -10;
    private boolean active = true;
    private boolean hardImpact = false;

    /* This is the basic constructor
     * @param scoreImpact is the integer value of the multiplier to be set
     * @param active is to toggle the requestfact being set to active
     * @param hardImpact is to set whether the fact has a hard or soft impact
     * @param hardFailure is to toggle whether the facts hard setting cannot be ignored
     */
    public MoreThanExpectedHours(int impact, boolean active, boolean hardImpact) {
        this.impact = impact;
        this.active = active;
        this.hardImpact = hardImpact;
    }

    public MoreThanExpectedHours() {

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

    public boolean ishardImpact() {
        return hardImpact;
    }

    public void setHardImpact(boolean hardImpact) {
        this.hardImpact = hardImpact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoreThanExpectedHours that = (MoreThanExpectedHours) o;
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
