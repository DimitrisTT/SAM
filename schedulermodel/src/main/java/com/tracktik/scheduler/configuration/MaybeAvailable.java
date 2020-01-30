package com.tracktik.scheduler.configuration;

import com.tracktik.scheduler.domain.ConfigFact;

import java.util.Objects;

/**
 * This is a class to house the fields of a Maybe Available requestFact
 *
 */
public class MaybeAvailable extends ConfigFact {

    private int scoreImpact = -100;
    private boolean active = true;

    /* This is the basic constructor
     * @param scoreImpact is the integer value of the multiplier to be set
     * @param active is to toggle the requestfact being set to active
     */
    public MaybeAvailable(int scoreImpact, boolean active) {
        this.scoreImpact = scoreImpact;
        this.active = active;
    }

    public MaybeAvailable() {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaybeAvailable that = (MaybeAvailable) o;
        return scoreImpact == that.scoreImpact &&
                active == that.active;
    }

    @Override
    public int hashCode() {
        return Objects.hash(scoreImpact, active);
    }

    @Override
    public String toString() {
        return "NotAvailable{" +
                "scoreImpact=" + scoreImpact +
                ", active=" + active +
                '}';
    }
}
