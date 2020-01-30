package com.tracktik.scheduler.configuration;

import com.tracktik.scheduler.domain.ConfigFact;

import java.util.Objects;

/**
 * This is a class to house the fields of a Far From Site requestFact
 *
 */
public class FarFromSite extends ConfigFact {

    private boolean active = true;
    private Integer scoreImpact = -70;
    private boolean isHardImpact = false;
    private Integer definition = 100;

    /* This is the basic constructor
     * @param impactMultiplier is the integer value of the multiplier to be set
     * @param active is to toggle the requestfact being set to active
     * @param hardImpact is to set whether the fact has a hard or soft impact
     * @param hardFailure is to toggle whether the facts hard setting cannot be ignored
     */
     public FarFromSite(boolean active, int scoreImpact, boolean isHardImpact, int definition) {
        this.active = active;
        this.scoreImpact = scoreImpact;
        this.isHardImpact = isHardImpact;
        this.definition = definition;
    }

    public FarFromSite(){

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getScoreImpact() {
        return scoreImpact;
    }

    public void setScoreImpact(Integer scoreImpact) {
        this.scoreImpact = scoreImpact;
    }

    public boolean isHardImpact() {
        return isHardImpact;
    }

    public void setHardImpact(boolean hardImpact) {
        isHardImpact = hardImpact;
    }

    public int getDefinition() {
        return definition;
    }

    public void setDefinition(Integer definition) {
        this.definition = definition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FarFromSite that = (FarFromSite) o;
        return active == that.active &&
                scoreImpact == that.scoreImpact &&
                isHardImpact == that.isHardImpact &&
                definition == that.definition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, scoreImpact, isHardImpact, definition);
    }

    @Override
    public String toString() {
        return "CloseBySite{" +
                "active=" + active +
                ", scoreImpact=" + scoreImpact +
                ", isHardImpact=" + isHardImpact +
                ", definition=" + definition +
                '}';
    }
}
