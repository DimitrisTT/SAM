package com.tracktik.test.steps;

import com.tracktik.scheduler.domain.*;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.drools.core.base.RuleNameMatchesAgendaFilter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

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
      System.out.println("shifts: " + shifts);
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
      System.out.println("employee " + employee);
      System.out.println("payrollSchedule " + payrollSchedule);
      droolsTestApi.ksession.insert(employee);
      droolsTestApi.ksession.insert(payrollSchedule);
      droolsTestApi.ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime .*"));
    });
    Then("^softscore is (-?\\d+)$", (Integer softScore) -> {
      assertEquals(softScore.longValue(), droolsTestApi.getScoreHolder().getSoftScore());
    });
  }
}
