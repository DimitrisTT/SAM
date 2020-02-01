package com.tracktik.scheduler.domain;

/**
 * This is an enum representing the various types of Configuration Facts available
 * Each can be seen in the instantiative list below.
 *
 */
public enum FactType {
    FAR_FROM_SITE,
    FAIRLY_FAR_FROM_SITE,
    CLOSE_BY_SITE,
    HARD_SKILL_MISSING,
    SOFT_SKILL_MISSING,
    MAYBE_AVAILABLE,
    NOT_AVAILABLE,
    MINIMUM_REST_PERIOD,
    LESS_THAN_EXPECTED_HOURS,
    MORE_THAN_EXPECTED_HOURS,
    NO_EXPERIENCE_AT_SITE,
    CAN_NOT_WORK_SIMULTANEOUS_SHIFTS,
    NOT_ASSIGNED_TO_SITE;


}
