package com.tracktik.scheduler.configuration;

import com.tracktik.scheduler.domain.ConfigFact;

import java.util.Objects;

public class MaybeAvailable extends ConfigFact {

    private int scoreImpact = -100;
    private boolean active = true;

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
