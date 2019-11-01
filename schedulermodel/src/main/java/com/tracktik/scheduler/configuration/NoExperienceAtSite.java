package com.tracktik.scheduler.configuration;

import com.tracktik.scheduler.domain.ConfigFact;

import java.util.Objects;

public class NoExperienceAtSite extends ConfigFact {

    private int scoreImpact = -50;
    private boolean active = true;
    private boolean isHardImpact = false;

    public NoExperienceAtSite(int scoreImpact, boolean active, boolean isHardImpact) {
        this.scoreImpact = scoreImpact;
        this.active = active;
        this.isHardImpact = isHardImpact;
    }

    public NoExperienceAtSite() {

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
        NoExperienceAtSite that = (NoExperienceAtSite) o;
        return scoreImpact == that.scoreImpact &&
                active == that.active &&
                isHardImpact == that.isHardImpact;
    }

    @Override
    public int hashCode() {
        return Objects.hash(scoreImpact, active, isHardImpact);
    }

    @Override
    public String toString() {
        return "NoExperienceAtSite{" +
                "scoreImpact=" + scoreImpact +
                ", active=" + active +
                ", isHardImpact=" + isHardImpact +
                '}';
    }
}
