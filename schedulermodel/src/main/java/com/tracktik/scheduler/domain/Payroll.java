package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Accessors(chain = true)
@EqualsAndHashCode
@Data
public class Payroll {
    private int id;
    private int hour;
    private int minute;
    private int second;
    private Long timestampDifference = 0L;
    private PayrollType payrollType;

    public Payroll(int id, PayrollType payrollType){
        this.id = id;
        this.payrollType = payrollType;
    }

    public static Set<Payroll> gimmeFive(int id){
        HashSet<Payroll> payrolls = new HashSet<Payroll>();
        payrolls.add(new Payroll(id, PayrollType.HOL));
        payrolls.add(new Payroll(id, PayrollType.OT));
        payrolls.add(new Payroll(id, PayrollType.DBL));
        payrolls.add(new Payroll(id, PayrollType.REG));
        payrolls.add(new Payroll(id, PayrollType.PTO));
        return payrolls;
    }

    public void setTimes(){
        int timestamp = Integer.parseInt(Long.toString(timestampDifference));
        System.out.println("timestamp: " + timestamp);
        hour = timestamp/3600;
        timestamp -= hour*3600;
        System.out.println("timestamp: " + timestamp + " hours: " + hour);
        if(timestamp > 0) {
            minute = timestamp/60;
            timestamp -= minute*60;
            System.out.println("timestamp: " + timestamp + " minute: " + minute);
        }
        if(timestamp > 0) {
            second = timestamp;
            timestamp -= second;
            System.out.println("timestamp: " + timestamp + " second: " + second);
        }
        if(timestamp < 0) {
            System.out.println("+*^*+ I think you messed up somewhere +*^*+");
        }
    }
}
