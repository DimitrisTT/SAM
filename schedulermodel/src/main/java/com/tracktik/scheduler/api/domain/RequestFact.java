package com.tracktik.scheduler.api.domain;

import lombok.ToString;

/**
 * This is a class to marshall the fields related to Request Facts used for configurative purposes
 * e.g. FAR_FROM_SITE, MINIMUM_REST_PERIOD, etc.
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestFact {

    public RequestFactType type;
    public boolean active;
    public int impact;
    public boolean isHardImpact;
    public int definition;
    public boolean isHardFailure;
}
