package com.tracktik.scheduler.domain;

import java.util.Objects;

public class NotAssignedToSite {

    private int scoreImpact = -50;
    private boolean active = true;
    private boolean isHardImpact = false;

    public NotAssignedToSite(int scoreImpact, boolean active, boolean isHardImpact) {
        this.scoreImpact = scoreImpact;
        this.active = active;
        this.isHardImpact = isHardImpact;
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
        NotAssignedToSite that = (NotAssignedToSite) o;
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
