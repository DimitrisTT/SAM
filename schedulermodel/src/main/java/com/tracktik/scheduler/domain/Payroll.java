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
    private int hour = 0;
    private int minute = 0;
    private int second = 0;
    private Long totHours = 0L;
    private Long timestampDifference = 0L;
    private PayrollType payrollType;

    public Payroll(int id, PayrollType payrollType){
        this.id = id;
        this.payrollType = payrollType;
    }

    public static Set<Payroll> gimmeSix(int id){
        HashSet<Payroll> payrolls = new HashSet<Payroll>();
        payrolls.add(new Payroll(id, PayrollType.HOL));
        payrolls.add(new Payroll(id, PayrollType.OT));
        payrolls.add(new Payroll(id, PayrollType.DBL));
        payrolls.add(new Payroll(id, PayrollType.REG));
        payrolls.add(new Payroll(id, PayrollType.PTO));
        payrolls.add(new Payroll(id, PayrollType.OTHOL));
        return payrolls;
    }

    public void setTimes(){
        int timestamp = Integer.parseInt(Long.toString(timestampDifference));
        //System.out.println("timestamp: " + timestamp);
        hour = timestamp/3600;
        timestamp -= hour*3600;
        //System.out.println("timestamp: " + timestamp + " hours: " + hour);
        if(timestamp > 0) {
            minute = timestamp/60;
            timestamp -= minute*60;
          //  System.out.println("timestamp: " + timestamp + " minute: " + minute);
        }
        if(timestamp > 0) {
            second = timestamp;
            timestamp -= second;
            //System.out.println("timestamp: " + timestamp + " second: " + second);
        }
        if(timestamp < 0) {
            //System.out.println("+*^*+ I think you messed up somewhere +*^*+");
        }
        totHours = 0L;
        totHours += hour;
        totHours += (((minute*60)+second)/3600);
        //totHours = Math.round(totHours * 100.0)/100.0;
    }
}
