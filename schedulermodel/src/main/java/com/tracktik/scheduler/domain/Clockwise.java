package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
@Data
public class Clockwise {
    private int id;
    private Set<Payroll> payrollSet = new HashSet<Payroll>();
    private Set<WorkPeriod> workPeriodSet = new HashSet<WorkPeriod>();
    private Set<DaySlice> daySliceSet = new HashSet<DaySlice>();
    private boolean workPeriodCutShifts = false;
    private boolean countHolidays = false;
    private boolean alignHolidays = false;

    /*
    @param pst is a payrollSchedule's WorkPeriodStartTime
    @param psd is a payrollSchedule's WorkPeriodStartDate
    @param freq is a payrollSchedule's Frequency
     */
    public Clockwise(LocalTime pst, LocalDate psd, boolean count, boolean align, String freq, OverlappingMethodType omt){
        workPeriodSet = new HashSet<WorkPeriod>();
        payrollSet =  new HashSet<Payroll>();
        countHolidays = count;
        alignHolidays = align;
        int tempTime = 0;
        switch (freq){
            case "weekly":
                for(int i=0; i<12; i++) {
                    WorkPeriod workPeriod = new WorkPeriod();
                    workPeriod.setPeriodId(i);
                    if(i == 0){
                        tempTime += (psd.getDayOfYear() * 86400);
                        tempTime += (pst.getHour() * 3600);
                        tempTime += (pst.getMinute() * 60);
                        tempTime += (pst.getSecond());
                        workPeriod.setStartTimeStamp(tempTime);
                    } else {
                        tempTime++;
                        workPeriod.setStartTimeStamp(tempTime);
                    }
                    tempTime += (7*86400); //days in a week * seconds in a day
                    tempTime--; //to not offset every workPeriod by one second
                    workPeriod.setEndTimeStamp(tempTime);
                    workPeriodSet.add(workPeriod);
                    payrollSet.addAll(Payroll.gimmeSix(i));
                }
                if(omt==OverlappingMethodType.CUT){
                    workPeriodCutShifts = true;
                }
                break;
            case "bi_weekly":
                for(int i=0; i<6; i++){
                    WorkPeriod workPeriod = new WorkPeriod();
                    workPeriod.setPeriodId(i);
                    if(i == 0){
                        tempTime += (psd.getDayOfYear() * 86400);
                        tempTime += (pst.getHour() * 3600);
                        tempTime += (pst.getMinute() * 60);
                        tempTime += (pst.getSecond());
                        workPeriod.setStartTimeStamp(tempTime);
                    } else {
                        tempTime++;
                        workPeriod.setStartTimeStamp(tempTime);
                    }
                    tempTime += (14*86400); //days in two weeks * seconds in a day
                    tempTime--; //to not offset every workPeriod by one second
                    workPeriod.setEndTimeStamp(tempTime);
                    workPeriodSet.add(workPeriod);
                    payrollSet.addAll(Payroll.gimmeSix(i));
                }
                if(omt==OverlappingMethodType.CUT){
                    workPeriodCutShifts = true;
                }
                break;
            default:
                break;
        }
    }

    public void printSets(){
        System.out.println("Printing Clockwise: " + id);
        System.out.println("Payrolls: ");
        for (Payroll payroll: payrollSet) {
            System.out.println(payroll);
        }
        System.out.println("WorkPeriods: ");
        for (WorkPeriod workPeriod: workPeriodSet) {
            System.out.println(workPeriod);
        }

    }
}
