package com.tracktik.test.steps;

import com.tracktik.scheduler.domain.*;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.drools.core.base.RuleNameMatchesAgendaFilter;
import org.drools.core.command.assertion.AssertEquals;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OvertimeSteps implements En {

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  class TestTimePeriod {
    String start;
    String end;
  }

  class TestPeriodOvertime {
    String min;
    String max;
    String type;
  }

  class TestDailyOvertime {
    String min;
    String max;
    String type;
  }

  class TestConsecutiveOvertime {
    String minDays;
    String maxDays;
    String minHours;
    String maxHours;
    String type;
  }

  class TestPayroll {
    String id;
    String hour;
    String minute;
    String second;
    String totHours;
    String timestampDifference;
    String payrollType;
  }

  Employee employee = new Employee().setId("1").setOvertimeRuleId("1").setPayScheduleId("1");
  PayrollSchedule payrollSchedule = new PayrollSchedule().setId("1");
  ZoneId zoneId;

  public OvertimeSteps(DroolsTestApi droolsTestApi) {

    Given("^Payroll start of '(.*?)' '(.*?)' in '(.*?)'$", (String sStartDate, String sStartTime, String sTimeZone) -> {
      zoneId = ZoneId.of(sTimeZone);
      ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(LocalDate.parse(sStartDate), LocalTime.parse(sStartTime)), zoneId);
      payrollSchedule.setPeriodStartTime(zonedDateTime.toLocalTime()).setPeriodStartDate(zonedDateTime.toLocalDate());
    });
    And("^pay cycle frequency of (.*?)$", (String payCycleFrequency) -> {
      payrollSchedule.setFrequency(payCycleFrequency);
    });
    And("^hours spanning daily periods will be cut between periods$", () -> {
      payrollSchedule.setOverlappingMethod(OverlappingMethodType.CUT);
    });
    And("^hours spanning daily periods$", () -> {
      payrollSchedule.setOverlappingMethod(OverlappingMethodType.SPAN);
    });
    And("^holiday periods of$", (DataTable table) -> {
      Set<HolidayPeriod> periods = table.asList(TestTimePeriod.class).stream().map(testTimePeriod -> {
        return new HolidayPeriod()
                .setPostId("1")
                .setStart(LocalDateTime.parse(testTimePeriod.start, dateTimeFormatter.withZone(zoneId)))
                .setEnd(LocalDateTime.parse(testTimePeriod.end, dateTimeFormatter.withZone(zoneId)));
      }).collect(Collectors.toSet());
      periods.forEach(HolidayPeriod::setTimeStamps);
      //System.out.println("holiday periods of: " + periods);
      periods.forEach(droolsTestApi.ksession::insert);
    });
    And("^employee shifts of$", (DataTable table) -> {
      AtomicInteger shiftId = new AtomicInteger(1);
      Set<Shift> shifts = table.asList(TestTimePeriod.class).stream().map(testShift -> {
        return new Shift()
                .setId(Integer.toString(shiftId.getAndIncrement()))
                .setPost(new Post().setId("1"))
                .setEmployee(employee)
                .setStart(LocalDateTime.parse(testShift.start, dateTimeFormatter))
                .setEnd(LocalDateTime.parse(testShift.end, dateTimeFormatter));
      }).collect(Collectors.toSet());
      shifts.forEach(Shift::setTimeStamps);
      //for(Shift shift: shifts){
      //  System.out.println("Employee: " + shift.getEmployee().getId());
      //  System.out.print("shift start time: " + shift.getStart() + "  ||  ");
      //  System.out.println("shift end time: " + shift.getEnd());
      //  System.out.print("shift start time stamp: " + shift.getStartTimeStamp() + "  ||  ");
      //  System.out.println("shift end time stamp: " + shift.getEndTimeStamp());
      //}
      shifts.forEach(droolsTestApi.ksession::insert);
    });
    And("^period overtime definitions with id '(.*?)' of$", (String id, DataTable table) -> {
      Set<PeriodOvertimeDefinition> periodOvertimeDefinitions = table.asList(TestPeriodOvertime.class).stream().map(testDefinition -> {
        return new PeriodOvertimeDefinition()
                .setId(id)
                .setOvertimeType(testDefinition.type)
                .setMinimumHours(new Long(testDefinition.min))
                .setMaximumHours(testDefinition.max.equals("INF") ? Long.MAX_VALUE : new Long(testDefinition.max));
      }).collect(Collectors.toSet());
      periodOvertimeDefinitions.forEach(droolsTestApi.ksession::insert);
    });
    And("^daily overtime definitions with id '(.*?)' of$", (String id, DataTable table) -> {
      Set<DayOvertimeDefinition> dayOvertimeDefinitions = table.asList(OvertimeSteps.TestDailyOvertime.class).stream().map(testDefinition -> {
        return new DayOvertimeDefinition()
                .setId(id)
                .setOvertimeType(testDefinition.type)
                .setMinimumHours(new Long(testDefinition.min))
                .setMaximumHours(testDefinition.max.equals("INF") ? Long.MAX_VALUE : new Long(testDefinition.max));
      }).collect(Collectors.toSet());
      dayOvertimeDefinitions.forEach(droolsTestApi.ksession::insert);
    });
    And("^consecutive day overtime definitions with id '(.*?)' of$", (String id, DataTable table) -> {
      Set<ConsecutiveDaysOvertimeDefinition> daysOvertimeDefinitions = table.asList(OvertimeSteps.TestConsecutiveOvertime.class).stream().map(testDefinition -> {
        return new ConsecutiveDaysOvertimeDefinition()
                .setId(id)
                .setOvertimeType(testDefinition.type)
                .setMinimumDay(new Long(testDefinition.minDays))
                .setMaximumDay(testDefinition.maxDays.equals("INF") ? Long.MAX_VALUE : new Long(testDefinition.maxDays))
                .setMinimumHours(new Long(testDefinition.minHours))
                .setMaximumHours(testDefinition.maxHours.equals("INF") ? Long.MAX_VALUE : new Long(testDefinition.maxHours));
      }).collect(Collectors.toSet());
      daysOvertimeDefinitions.forEach(droolsTestApi.ksession::insert);
    });
    And("^count holiday hours towards period overtime$", () -> {
      payrollSchedule.setCountHolidayHoursTowardsPeriodOvertime(true);
    });
    And("^holiday hours use start time$", () -> {
      payrollSchedule.setAlignHolidaysWithPeriodStartTime(true);
    });
    When("^overtime is calculated$", () -> {
      //System.out.println("payrollSchedule " + payrollSchedule);
      //Clockwise clockwise = new Clockwise();
      //employee.setClockwise(clockwise);
      //System.out.println("employee: " + employee.getId());
      droolsTestApi.ksession.insert(employee);
      droolsTestApi.ksession.insert(payrollSchedule);
      droolsTestApi.ksession.fireAllRules();
    });
    Then("^softscore is (-?\\d+)$", (Integer softScore) -> {
      assertEquals(softScore.longValue(), droolsTestApi.getScoreHolder().getSoftScore());
      //for (Object object : droolsTestApi.ksession.getObjects()) {
      //  if (object.getClass().equals(Employee.class)) {
      //    Employee employee = (Employee) object;
      //    for (Payroll payroll : employee.getClockwise().getPayrollSet()) {
      //      if (payroll.getTimestampDifference() > 0) {
      //        System.out.println(payroll);
      //      }
      //    }
      //  }
      //}
    });

    Then("^Payroll results are as expected$", (DataTable table) -> {
      Set<Payroll> expectedPayrolls = table.asList(TestPayroll.class).stream().map(testPayroll -> {
        return new Payroll()
                .setId(Integer.parseInt(testPayroll.id))
                .setHour(Integer.parseInt(testPayroll.hour))
                .setMinute(Integer.parseInt(testPayroll.minute))
                .setSecond(Integer.parseInt(testPayroll.second))
                .setTotHours(Long.parseLong(testPayroll.totHours))
                .setTimestampDifference(Long.parseLong(testPayroll.timestampDifference))
                .setPayrollType(PayrollType.valueOf(testPayroll.payrollType));
      }).collect(Collectors.toSet());
      int trues = 0;
      for (Object object : droolsTestApi.ksession.getObjects()) {
        if (object.getClass().equals(Employee.class)) {
          Employee employee = (Employee) object;
          for(Payroll ksessionPayroll: employee.getClockwise().getPayrollSet()){
            //if(ksessionPayroll.getId() == 0 && ksessionPayroll.getPayrollType() == PayrollType.REG || ksessionPayroll.getId() == 1 && ksessionPayroll.getPayrollType() == PayrollType.REG) {
            //  System.out.println("from ksession: " + ksessionPayroll);
            //}
            //if(ksessionPayroll.getId() == 0 && ksessionPayroll.getPayrollType() == PayrollType.HOL || ksessionPayroll.getId() == 1 && ksessionPayroll.getPayrollType() == PayrollType.HOL) {
            //  System.out.println("from ksession: " + ksessionPayroll);
            //}
            //if(ksessionPayroll.getId() == 0 && ksessionPayroll.getPayrollType() == PayrollType.OT || ksessionPayroll.getId() == 1 && ksessionPayroll.getPayrollType() == PayrollType.OT) {
            //  System.out.println("from ksession: " + ksessionPayroll);
            //}
              for (Payroll expectedPayroll : expectedPayrolls) {
                //if(ksessionPayroll.getId() == 0 || ksessionPayroll.getId() == 1 && ksessionPayroll.getPayrollType() == PayrollType.REG) {
                //  System.out.println("expected: " + expectedPayrolls);
                //}
                if (ksessionPayroll.equals(expectedPayroll)) {
                  trues++;
                }
              }
          }
          assertTrue(trues==expectedPayrolls.size());
        }
      }
    });

  }
}
//new RuleNameMatchesAgendaFilter("^Overtime .*"

/*
And("^employee shifts of$", (DataTable table) -> {
      AtomicInteger shiftId = new AtomicInteger(1);
      Set<Shift> shifts = table.asList(TestTimePeriod.class).stream().map(testShift -> {
        return new Shift()
            .setId(Integer.toString(shiftId.getAndIncrement()))
            .setPost(new Post().setId("1"))
            .setEmployee(employee)
            .setStart(LocalDateTime.parse(testShift.start, dateTimeFormatter))
            .setEnd(LocalDateTime.parse(testShift.end, dateTimeFormatter));
      }).collect(Collectors.toSet());
      shifts.forEach(Shift::setTimeStamps);
      //for(Shift shift: shifts){
      //  System.out.println("Employee: " + shift.getEmployee().getId());
      //  System.out.print("shift start time: " + shift.getStart() + "  ||  ");
      //  System.out.println("shift end time: " + shift.getEnd());
      //  System.out.print("shift start time stamp: " + shift.getStartTimeStamp() + "  ||  ");
      //  System.out.println("shift end time stamp: " + shift.getEndTimeStamp());
      //}
      shifts.forEach(droolsTestApi.ksession::insert);
 */