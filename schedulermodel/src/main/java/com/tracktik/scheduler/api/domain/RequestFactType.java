package com.tracktik.scheduler.api.domain;

import com.tracktik.scheduler.domain.FactType;

public enum RequestFactType {
    FAR_FROM_SITE(FactType.FAR_FROM_SITE),
    FAIRLY_FAR_FROM_SITE(FactType.FAIRLY_FAR_FROM_SITE),
    CLOSE_BY_SITE(FactType.CLOSE_BY_SITE),
    HARD_SKILL_MISSING(FactType.HARD_SKILL_MISSING),
    SOFT_SKILL_MISSING(FactType.SOFT_SKILL_MISSING),
    MAYBE_AVAILABLE(FactType.MAYBE_AVAILABLE),
    NOT_AVAILABLE(FactType.NOT_AVAILABLE),
    MINIMUM_REST_PERIOD(FactType.MINIMUM_REST_PERIOD),
    LESS_THAN_EXPECTED_HOURS(FactType.LESS_THAN_EXPECTED_HOURS),
    MORE_THAN_EXPECTED_HOURS(FactType.MORE_THAN_EXPECTED_HOURS),
    NO_EXPERIENCE_AT_SITE(FactType.NO_EXPERIENCE_AT_SITE),
    NOT_ASSIGNED_TO_SITE(FactType.NOT_ASSIGNED_TO_SITE);

    private final FactType factType;

    private RequestFactType(FactType factType){
        this.factType = factType;
    }

    public FactType asFactType(){
        return factType;
    }


}
