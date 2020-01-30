package com.tracktik.scheduler.configuration;

import com.tracktik.scheduler.domain.ConfigFact;

import java.util.Objects;

/**
 * This is a class to house the fields of a Not Assigned To Site requestFact
 *
 */
public class NotAssignedToSite extends ConfigFact {

    private int scoreImpact = -50;
    private boolean active = true;
    private boolean hardImpact = false;

    /* This is the basic constructor
     * @param scoreImpact is the integer value of the multiplier to be set
     * @param active is to toggle the requestfact being set to active
     * @param hardImpact is to set whether the fact has a hard or soft impact
     */
    public NotAssignedToSite(int scoreImpact, boolean active, boolean hardImpact) {
        this.scoreImpact = scoreImpact;
        this.active = active;
        this.hardImpact = hardImpact;
    }

    public NotAssignedToSite() {

    }

    public int getScoreImpact() {
        return scoreImpact;
    }

    public void setScoreImpact(int scoreImpact) {
        this.scoreImpact = scoreImpact;
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
        NotAssignedToSite that = (NotAssignedToSite) o;
        return scoreImpact == that.scoreImpact &&
                active == that.active &&
                hardImpact == that.hardImpact;
    }

    @Override
    public int hashCode() {
        return Objects.hash(scoreImpact, active, hardImpact);
    }

    @Override
    public String toString() {
        return "NoExperienceAtSite{" +
                "scoreImpact=" + scoreImpact +
                ", active=" + active +
                ", hardImpact=" + hardImpact +
                '}';
    }
}
