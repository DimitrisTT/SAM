package com.tracktik.scheduler.api.domain;

import lombok.ToString;

@ToString
public class RequestFact {

    public RequestFactType type;
    public boolean active;
    public int impact;
    public boolean isHardImpact;
    public int definition;
    public boolean isHardFailure;
}
