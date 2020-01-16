package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@EqualsAndHashCode
@Data
public class Period {
    private int id;
    private int startTimeStamp;
    private int endTimeStamp;
}
