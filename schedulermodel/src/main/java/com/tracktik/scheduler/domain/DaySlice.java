package com.tracktik.scheduler.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
@Data
public class DaySlice {
    private int id;
    private int dayOfPeriod;
    private int startTimeStamp;
    private int endTimeStamp;
    private boolean otCounted = false;
    private boolean dblCounted = false;

    public DaySlice(int id, int dayOfPeriod, int startTimeStamp){
        this.id = id;
        this.dayOfPeriod = dayOfPeriod;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = startTimeStamp+86399;
    }

    public static Set<DaySlice> gimmeSeven(int id, int sts){
        HashSet<DaySlice> daySlices = new HashSet<DaySlice>();
        int dayStart = sts;
        DaySlice daySlice;
        for(int i=1; i<8; i++){
            daySlice = new DaySlice(id, i, dayStart*i);
            daySlices.add(daySlice);
        }

        return daySlices;
    }

    public static Set<DaySlice> gimmeFourteen(int id, int sts){
        HashSet<DaySlice> daySlices = new HashSet<DaySlice>();
        for(int i=1; i<15; i++) {
            daySlices.add(new DaySlice(id, i, sts));
            sts += 86400; //seconds in a day
        }
        return daySlices;
    }

}
