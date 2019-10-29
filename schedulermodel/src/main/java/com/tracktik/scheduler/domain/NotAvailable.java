package com.tracktik.scheduler.domain;

import java.util.Objects;

public class NotAvailable {

    private int scoreImpact = -100;
    private boolean isHardImpact = true;
    private boolean active = true;

    public NotAvailable(int scoreImpact, boolean isHardImpact, boolean active) {
        this.scoreImpact = scoreImpact;
        this.isHardImpact = isHardImpact;
        this.active = active;
    }

    public int getScoreImpact() {
        return scoreImpact;
    }

    public void setScoreImpact(int scoreImpact) {
        this.scoreImpact = scoreImpact;
    }

    public boolean isHardImpact() {
        return isHardImpact;
    }

    public void setHardImpact(boolean hardImpact) {
        isHardImpact = hardImpact;
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
        NotAvailable that = (NotAvailable) o;
        return scoreImpact == that.scoreImpact &&
                isHardImpact == that.isHardImpact &&
                active == that.active;
    }

    @Override
    public int hashCode() {
        return Objects.hash(scoreImpact, isHardImpact, active);
    }

    @Override
    public String toString() {
        return "NotAvailable{" +
                "scoreImpact=" + scoreImpact +
                ", isHardImpact=" + isHardImpact +
                ", active=" + active +
                '}';
    }
}
