package com.tracktik.scheduler.api.domain;

import lombok.ToString;

/**
 * This is a class to marshall the fields related to ScaleFacts and their configuration
 * Fields represented here all as strings, but are casted to various types when mapped
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestScaleFact {

    public String id;
    public String scale_tag;
    public String scale_type;
    public String rating;
    public String post_id;
    public String scale_impact_square;
    public String scale_impact;

}
