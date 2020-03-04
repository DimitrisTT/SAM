package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.time.DateUtils;

@Accessors(chain = true)
@EqualsAndHashCode
@Data
public class WorkPeriod {
    private int periodId;
    private int startTimeStamp;
    private int endTimeStamp;
    private boolean otCounted = false;
    private boolean dblCounted = false;

    private static final int SECONDS_IN_DAY = 86400;

    public int getDayOfShift(Long shiftTimeStamp){
        int timeIntoPeriod = shiftTimeStamp.intValue() - startTimeStamp;
        int dayOfPeriod = timeIntoPeriod / SECONDS_IN_DAY;
        //if(shift.getHolidayId() < 0){
        //    return dayOfPeriod * -1;
        //}
        return dayOfPeriod;
    }

    public int getDayOfShift(int shiftTimeStamp){
        int timeIntoPeriod = shiftTimeStamp - startTimeStamp;
        int dayOfPeriod = timeIntoPeriod / SECONDS_IN_DAY;
        //if(shift.getHolidayId() < 0){
        //    return dayOfPeriod * -1;
        //}
        return dayOfPeriod;
    }

}
