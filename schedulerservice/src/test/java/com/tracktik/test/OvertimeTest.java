package com.tracktik.test;

import com.tracktik.scheduler.domain.*;
import com.tracktik.scheduler.service.DailyHours;
import com.tracktik.scheduler.service.DateWorked;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameMatchesAgendaFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class OvertimeTest extends ConstraintRuleTestBase {

  @Test
  public void shiftIncludesSameStart() {

    Shift shift = new Shift()
        .setStart(LocalDateTime.parse("2018-03-12 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 17:00:00", dtf));

    LocalDate date = LocalDate.parse("2018-03-12");
    LocalTime time = LocalTime.parse("09:00:00");
    LocalDate endDate = LocalDate.parse("2018-03-30");

    assertTrue(shift.includesTime(date, time, endDate));
  }

  @Test
  public void shiftNotIncludingEnd() {

    Shift shift = new Shift()
        .setStart(LocalDateTime.parse("2018-03-12 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 17:00:00", dtf));

    LocalDate date = LocalDate.parse("2018-03-12");
    LocalTime time = LocalTime.parse("17:00:00");
    LocalDate endDate = LocalDate.parse("2018-03-30");

    assertFalse(shift.includesTime(date, time, endDate));
  }

  @Test
  public void shiftBeforePeriod() {

    Shift shift = new Shift()
        .setStart(LocalDateTime.parse("2018-03-12 02:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 09:00:00", dtf));

    LocalDate date = LocalDate.parse("2018-03-12");
    LocalTime time = LocalTime.parse("09:00:00");
    LocalDate endDate = LocalDate.parse("2018-03-30");

    assertFalse(shift.includesTime(date, time, endDate));
  }

  @Test
  public void priodBeforeShift() {

    Shift shift = new Shift()
        .setStart(LocalDateTime.parse("2018-03-12 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 17:00:00", dtf));

    LocalDate date = LocalDate.parse("2018-02-12");
    LocalTime time = LocalTime.parse("09:00:00");
    LocalDate endDate = LocalDate.parse("2018-03-11");

    assertFalse(shift.includesTime(date, time, endDate));
  }

  @Test
  public void sameDayShiftHours() {

    Shift shift = new Shift()
        .setStart(LocalDateTime.parse("2018-03-12 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 17:00:00", dtf));

    Long hours = shift.durationHoursOnDate(LocalDate.parse("2018-03-12"));

    assertEquals(800L, hours.longValue());
  }

  @Test
  public void previousDayShiftHours() {

    Shift shift = new Shift()
        .setStart(LocalDateTime.parse("2018-03-12 22:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 03:00:00", dtf));

    Long hours = shift.durationHoursOnDate(LocalDate.parse("2018-03-12"));

    assertEquals(200L, hours.longValue());
  }

  @Test
  public void nextDayShiftHours() {

    Shift shift = new Shift()
        .setStart(LocalDateTime.parse("2018-03-12 22:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 03:00:00", dtf));

    Long hours = shift.durationHoursOnDate(LocalDate.parse("2018-03-13"));

    assertEquals(300L, hours.longValue());
  }

  @Test
  public void noMatchingShiftHours() {

    Shift shift = new Shift()
        .setStart(LocalDateTime.parse("2018-03-12 22:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 03:00:00", dtf));

    Long hours = shift.durationHoursOnDate(LocalDate.parse("2018-03-14"));

    assertEquals(0L, hours.longValue());
  }

  @Test
  public void hasNoPeriodOvertime() {

    PeriodDefinition periodDefinition = new PeriodDefinition().setNumberOfDays(7).setStartDay(DayOfWeek.MONDAY);
    PeriodOvertimeDefinition periodOvertimeDefinition = new PeriodOvertimeDefinition().setId("1")
        .setMinimumHours(40L).setMaximumHours(Long.MAX_VALUE).setOvertimeType("OT");

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L).setOvertimeRuleId("1").setCost(1000L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 17:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-13 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 17:00:00", dtf));

    Shift shift3 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-14 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 17:00:00", dtf));

    Shift shift4 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-15 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-15 17:00:00", dtf));

    Shift shift5 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-16 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-16 17:00:00", dtf));


    ksession.insert(periodDefinition);
    ksession.insert(periodOvertimeDefinition);

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);
    ksession.insert(shift3);
    ksession.insert(shift4);
    ksession.insert(shift5);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime period .*"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  @Test
  public void hasPeriodOvertime() {

    PeriodOvertimeDefinition periodOvertimeDefinition = new PeriodOvertimeDefinition().setId("1")
        .setMinimumHours(40L).setMaximumHours(Long.MAX_VALUE).setOvertimeType("OT");

    PayrollSchedule schedule = new PayrollSchedule().setId("1")
        .setPeriodStartDate(LocalDate.parse("2018-03-12"))
        .setPeriodStartTime(LocalTime.parse("06:00:00"))
        .setOverlappingMethod(OverlappingMethodType.SPAN)
        .setFrequency("weekly");

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L)
        .setOvertimeRuleId("1").setPayScheduleId("1").setCost(1000L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 19:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-13 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 19:00:00", dtf));

    Shift shift3 = new Shift()
        .setId("3")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-14 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 19:00:00", dtf));

    Shift shift4 = new Shift()
        .setId("4")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-15 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-15 19:00:00", dtf));

    Shift shift5 = new Shift()
        .setId("5")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-16 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-16 19:00:00", dtf));

    ksession.insert(periodOvertimeDefinition);

    ksession.insert(schedule);
    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);
    ksession.insert(shift3);
    ksession.insert(shift4);
    ksession.insert(shift5);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime period .*"));

    assertEquals(-1500L, getScoreHolder().getSoftScore());
  }

  @Test
  public void hasPeriodDoubletime() {

    PeriodDefinition periodDefinition = new PeriodDefinition().setNumberOfDays(7).setStartDay(DayOfWeek.MONDAY);

    PayrollSchedule schedule = new PayrollSchedule().setId("1")
        .setPeriodStartDate(LocalDate.parse("2018-03-12"))
        .setPeriodStartTime(LocalTime.parse("06:00:00"))
        .setFrequency("weekly");

    PeriodOvertimeDefinition periodOvertimeDefinition = new PeriodOvertimeDefinition().setId("1")
        .setMinimumHours(40L).setMaximumHours(50L).setOvertimeType("OT");

    PeriodOvertimeDefinition periodDoubleTimeDefinition = new PeriodOvertimeDefinition().setId("1")
        .setMinimumHours(50L).setMaximumHours(Long.MAX_VALUE).setOvertimeType("DBL");

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L)
        .setOvertimeRuleId("1").setPayScheduleId("1").setCost(1000L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 21:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-13 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 21:00:00", dtf));

    Shift shift3 = new Shift()
        .setId("3")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-14 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 21:00:00", dtf));

    Shift shift4 = new Shift()
        .setId("4")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-15 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-15 21:00:00", dtf));

    Shift shift5 = new Shift()
        .setId("5")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-16 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-16 21:00:00", dtf));


    ksession.insert(schedule);
    ksession.insert(periodDefinition);
    ksession.insert(periodOvertimeDefinition);
    ksession.insert(periodDoubleTimeDefinition);

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);
    ksession.insert(shift3);
    ksession.insert(shift4);
    ksession.insert(shift5);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime period .*"));

    assertEquals(-3500L, getScoreHolder().getSoftScore());
  }

  @Test
  public void dateWorkedSameDay() {

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L).setOvertimeRuleId("1").setCost(1000L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 02:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 04:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 07:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime daily -- date worked.*"));

    Optional<DateWorked> dailyHours = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DateWorked.class.getSimpleName())).map(fact -> (DateWorked)fact).findAny();

    Long numItems = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DateWorked.class.getSimpleName())).count();

    assertEquals(1L, numItems.longValue());
    assertTrue(dailyHours.isPresent());
    assertTrue(dailyHours.get().getDate().isEqual(LocalDate.parse("2018-03-12")));
  }

  @Test
  public void dateWorkedTwoDays() {

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L).setOvertimeRuleId("1").setCost(1000L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 02:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 04:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-14 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 07:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime daily -- date worked.*"));

    Optional<DateWorked> firstDate = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DateWorked.class.getSimpleName()))
        .map(fact -> (DateWorked)fact)
        .filter(dateWorked -> dateWorked.getDate().isEqual(LocalDate.parse("2018-03-12"))).findAny();

    Optional<DateWorked> secondDate = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DateWorked.class.getSimpleName()))
        .map(fact -> (DateWorked)fact)
        .filter(dateWorked -> dateWorked.getDate().isEqual(LocalDate.parse("2018-03-14"))).findAny();

    Long numItems = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DateWorked.class.getSimpleName())).count();

    assertEquals(2L, numItems.longValue());
    assertTrue(firstDate.isPresent());
    assertTrue(secondDate.isPresent());
  }

  @Test
  public void dateWorkedSpanningTwoDays() {

    Employee employee = new Employee().setId("1")
        .setMinimumRestPeriod(5L).setOvertimeRuleId("1").setCost(1000L).setPayScheduleId("1").setOvertimeRuleId("1");
    PayrollSchedule payrollSchedule = new PayrollSchedule()
        .setId("1")
        .setPeriodStartDate(LocalDate.parse("2018-03-12"))
        .setPeriodStartTime(LocalTime.parse("00:00"))
        .setOverlappingMethod(OverlappingMethodType.SPAN)
        .setFrequency("weekly");

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 02:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 04:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 23:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 07:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(payrollSchedule);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime daily -- date worked.*"));

    Optional<DateWorked> firstDate = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DateWorked.class.getSimpleName()))
        .map(fact -> (DateWorked)fact)
        .filter(dateWorked -> dateWorked.getDate().isEqual(LocalDate.parse("2018-03-12"))).findAny();

    Optional<DateWorked> secondDate = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DateWorked.class.getSimpleName()))
        .map(fact -> (DateWorked)fact)
        .filter(dateWorked -> dateWorked.getDate().isEqual(LocalDate.parse("2018-03-13"))).findAny();

    Long numItems = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DateWorked.class.getSimpleName())).count();

    assertEquals(1L, numItems.longValue());
    assertTrue(firstDate.isPresent());
    assertFalse(secondDate.isPresent());
  }

  @Test
  public void dateWorkedSpanningTwoDaysWithCut() {

    Employee employee = new Employee().setId("1")
        .setMinimumRestPeriod(5L).setOvertimeRuleId("1").setCost(1000L).setPayScheduleId("1").setOvertimeRuleId("1");
    PayrollSchedule payrollSchedule = new PayrollSchedule()
        .setId("1")
        .setPeriodStartDate(LocalDate.parse("2018-03-12"))
        .setPeriodStartTime(LocalTime.parse("00:00"))
        .setOverlappingMethod(OverlappingMethodType.CUT)
        .setFrequency("weekly");

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 02:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 04:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 23:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 07:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(payrollSchedule);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime daily -- date worked spanning.*"));

    Optional<DateWorked> firstDate = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DateWorked.class.getSimpleName()))
        .map(fact -> (DateWorked)fact)
        .filter(dateWorked -> dateWorked.getDate().isEqual(LocalDate.parse("2018-03-12"))).findAny();

    Optional<DateWorked> secondDate = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DateWorked.class.getSimpleName()))
        .map(fact -> (DateWorked)fact)
        .filter(dateWorked -> dateWorked.getDate().isEqual(LocalDate.parse("2018-03-13"))).findAny();

    Long numItems = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DateWorked.class.getSimpleName())).count();

    assertEquals(2L, numItems.longValue());
    assertTrue(firstDate.isPresent());
    assertTrue(secondDate.isPresent());
  }

  @Test
  public void sameDayHours() {

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L).setOvertimeRuleId("1").setCost(1000L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 02:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 04:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 07:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime daily.*"));
    Optional<DailyHours> dailyHours = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DailyHours.class.getSimpleName())).map(fact -> (DailyHours)fact).findAny();

    Long numItems = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DailyHours.class.getSimpleName())).count();

    assertEquals(1L, numItems.longValue());
    assertTrue(dailyHours.isPresent());
    assertEquals(400L, dailyHours.get().getHours().longValue());
  }

  @Test
  public void somePreviousDayHours() {

    Employee employee = new Employee().setId("1")
        .setMinimumRestPeriod(5L).setOvertimeRuleId("1").setCost(1000L)
        .setOvertimeRuleId("1").setPayScheduleId("1");

    PayrollSchedule payrollSchedule = new PayrollSchedule()
        .setId("1")
        .setPeriodStartDate(LocalDate.parse("2018-03-11"))
        .setPeriodStartTime(LocalTime.parse("00:00"))
        .setOverlappingMethod(OverlappingMethodType.CUT)
        .setFrequency("weekly");

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-11 23:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 02:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 07:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(payrollSchedule);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime daily.*"));

    Optional<DailyHours> firstDay = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DailyHours.class.getSimpleName())).map(fact -> (DailyHours)fact)
        .filter(dailyHours -> dailyHours.getDate().equals(LocalDate.parse("2018-03-11"))).findAny();

    Optional<DailyHours> secondDay = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DailyHours.class.getSimpleName())).map(fact -> (DailyHours)fact)
        .filter(dailyHours -> dailyHours.getDate().equals(LocalDate.parse("2018-03-12"))).findAny();

    Long numItems = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DailyHours.class.getSimpleName())).count();

    assertEquals(2L, numItems.longValue());
    assertTrue(firstDay.isPresent());
    assertTrue(secondDay.isPresent());
    assertEquals(100L, firstDay.get().getHours().longValue());
    assertEquals(400L, secondDay.get().getHours().longValue());
  }

  @Test
  public void somePreviousDayHoursWithGap() {

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L).setOvertimeRuleId("1").setCost(1000L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-11 23:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 02:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-13 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 07:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime daily.*"));

    Optional<DailyHours> firstDay = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DailyHours.class.getSimpleName())).map(fact -> (DailyHours)fact)
        .filter(dailyHours -> dailyHours.getDate().equals(LocalDate.parse("2018-03-11"))).findAny();

    Optional<DailyHours> secondDay = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DailyHours.class.getSimpleName())).map(fact -> (DailyHours)fact)
        .filter(dailyHours -> dailyHours.getDate().equals(LocalDate.parse("2018-03-13"))).findAny();

    Long numItems = ksession.getObjects().stream()
        .filter(fact -> fact.getClass().getSimpleName().equals(DailyHours.class.getSimpleName())).count();

    assertEquals(3L, numItems.longValue());
    assertTrue(firstDay.isPresent());
    assertTrue(secondDay.isPresent());
    assertEquals(1L, firstDay.get().getHours().longValue());
    assertEquals(200L, secondDay.get().getHours().longValue());
  }

  @Test
  public void hasNoDailyOvertime() {

    DayDefinition dayDefinition = new DayDefinition().setStartHour(0);
    DayOvertimeDefinition dayOvertimeDefinition = new DayOvertimeDefinition().setId("1")
        .setMinimumHours(8L).setMaximumHours(Long.MAX_VALUE).setOvertimeType("OT");

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L).setOvertimeRuleId("1").setCost(1000L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 17:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-13 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 17:00:00", dtf));

    Shift shift3 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-14 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 17:00:00", dtf));

    Shift shift4 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-15 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-15 17:00:00", dtf));

    Shift shift5 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-16 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-16 17:00:00", dtf));


    ksession.insert(dayDefinition);
    ksession.insert(dayOvertimeDefinition);

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);
    ksession.insert(shift3);
    ksession.insert(shift4);
    ksession.insert(shift5);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime daily .*"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  @Test
  public void hasDailyOvertime() {

    DayOvertimeDefinition dayOvertimeDefinition = new DayOvertimeDefinition().setId("1")
        .setMinimumHours(8L).setMaximumHours(Long.MAX_VALUE).setOvertimeType("OT");

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L).setOvertimeRuleId("1").setCost(1000L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 19:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-13 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 19:00:00", dtf));

    Shift shift3 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-14 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 19:00:00", dtf));

    Shift shift4 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-15 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-15 19:00:00", dtf));

    Shift shift5 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-16 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-16 19:00:00", dtf));

    ksession.insert(dayOvertimeDefinition);

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);
    ksession.insert(shift3);
    ksession.insert(shift4);
    ksession.insert(shift5);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime daily .*"));

    assertEquals(-15L, getScoreHolder().getSoftScore());
  }

  @Test
  public void hasDailyOvertimeFromPreviousDay() {

    DayOvertimeDefinition dayOvertimeDefinition = new DayOvertimeDefinition().setId("1")
        .setMinimumHours(8L).setMaximumHours(Long.MAX_VALUE).setOvertimeType("OT");

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L).setOvertimeRuleId("1").setCost(1000L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-11 20:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 10:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-13 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 19:00:00", dtf));

    Shift shift3 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-14 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 19:00:00", dtf));

    Shift shift4 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-15 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-15 19:00:00", dtf));

    Shift shift5 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-16 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-16 19:00:00", dtf));

    ksession.insert(dayOvertimeDefinition);

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);
    ksession.insert(shift3);
    ksession.insert(shift4);
    ksession.insert(shift5);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime daily .*"));

    assertEquals(-15L, getScoreHolder().getSoftScore());
  }

  @Test
  public void hasDailyDoubletime() {

    DayDefinition periodDefinition = new DayDefinition().setStartHour(0);

    DayOvertimeDefinition dayOvertimeDefinition = new DayOvertimeDefinition().setId("1")
        .setMinimumHours(8L).setMaximumHours(10L).setOvertimeType("OT");

    DayOvertimeDefinition dayDoubleTimeDefinition = new DayOvertimeDefinition().setId("1")
        .setMinimumHours(10L).setMaximumHours(Long.MAX_VALUE).setOvertimeType("DBL");

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L).setOvertimeRuleId("1").setCost(1000L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 21:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-13 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 21:00:00", dtf));

    Shift shift3 = new Shift()
        .setId("3")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-14 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 21:00:00", dtf));

    Shift shift4 = new Shift()
        .setId("4")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-15 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-15 21:00:00", dtf));

    Shift shift5 = new Shift()
        .setId("5")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-16 09:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-16 21:00:00", dtf));


    ksession.insert(periodDefinition);
    ksession.insert(dayOvertimeDefinition);
    ksession.insert(dayDoubleTimeDefinition);

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);
    ksession.insert(shift3);
    ksession.insert(shift4);
    ksession.insert(shift5);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime daily .*"));

    assertEquals(-35L, getScoreHolder().getSoftScore());
  }

  @Test
  public void consecutiveOvertimeDefinition() {

    ConsecutiveDaysOvertimeDefinition definition = new ConsecutiveDaysOvertimeDefinition()
        .setMinimumDay(2L)
        .setMaximumDay(Long.MAX_VALUE)
        .setMinimumHours(0L)
        .setMaximumHours(Long.MAX_VALUE)
        .setOvertimeType("OT");

    List<Shift> shifts = new ArrayList<>();

    Shift shift1 = new Shift()
        .setStart(LocalDateTime.parse("2018-03-12 02:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 04:00:00", dtf));
    shifts.add(shift1);

    Shift shift2 = new Shift()
        .setId("2")
        .setStart(LocalDateTime.parse("2018-03-13 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 07:00:00", dtf));
    shifts.add(shift2);

    Shift shift3 = new Shift()
        .setId("3")
        .setStart(LocalDateTime.parse("2018-03-14 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 07:00:00", dtf));
    shifts.add(shift3);

    assertEquals(300L, definition.overtimeHours(shifts).longValue());
  }

  @Test
  public void consecutiveOvertimeDefinitionMaxHours() {

    ConsecutiveDaysOvertimeDefinition definition = new ConsecutiveDaysOvertimeDefinition()
        .setMinimumDay(2L)
        .setMaximumDay(Long.MAX_VALUE)
        .setMinimumHours(0L)
        .setMaximumHours(Long.MAX_VALUE)
        .setOvertimeType("OT");

    List<Shift> shifts = new ArrayList<>();

    Shift shift1 = new Shift()
        .setStart(LocalDateTime.parse("2018-03-12 02:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 04:00:00", dtf));
    shifts.add(shift1);

    Shift shift2 = new Shift()
        .setId("2")
        .setStart(LocalDateTime.parse("2018-03-13 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 07:00:00", dtf));
    shifts.add(shift2);

    Shift shift3 = new Shift()
        .setId("3")
        .setStart(LocalDateTime.parse("2018-03-14 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 07:00:00", dtf));
    shifts.add(shift3);

    assertEquals(300, definition.overtimeHours(shifts).longValue());
  }
  @Test
  public void consecutiveOvertimeDefinitionNotMaxHours() {

    ConsecutiveDaysOvertimeDefinition definition = new ConsecutiveDaysOvertimeDefinition()
        .setMinimumDay(2L)
        .setMaximumDay(Long.MAX_VALUE)
        .setMinimumHours(0L)
        .setMaximumHours(1L)
        .setOvertimeType("OT");

    List<Shift> shifts = new ArrayList<>();

    Shift shift1 = new Shift()
        .setStart(LocalDateTime.parse("2018-03-12 02:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 04:00:00", dtf));
    shifts.add(shift1);

    Shift shift2 = new Shift()
        .setId("2")
        .setStart(LocalDateTime.parse("2018-03-13 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 07:00:00", dtf));
    shifts.add(shift2);

    Shift shift3 = new Shift()
        .setId("3")
        .setStart(LocalDateTime.parse("2018-03-14 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 07:00:00", dtf));
    shifts.add(shift3);

    assertEquals(150, definition.overtimeHours(shifts).longValue());
  }
  @Test
  public void consecutiveDoubletimeDefinitionNotMaxHours() {

    ConsecutiveDaysOvertimeDefinition definition = new ConsecutiveDaysOvertimeDefinition()
        .setMinimumDay(2L)
        .setMaximumDay(Long.MAX_VALUE)
        .setMinimumHours(0L)
        .setMaximumHours(1L)
        .setOvertimeType("DBL");

    List<Shift> shifts = new ArrayList<>();

    Shift shift1 = new Shift()
        .setStart(LocalDateTime.parse("2018-03-12 02:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 04:00:00", dtf));
    shifts.add(shift1);

    Shift shift2 = new Shift()
        .setId("2")
        .setStart(LocalDateTime.parse("2018-03-13 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 07:00:00", dtf));
    shifts.add(shift2);

    Shift shift3 = new Shift()
        .setId("3")
        .setStart(LocalDateTime.parse("2018-03-14 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 07:00:00", dtf));
    shifts.add(shift3);

    assertEquals(200, definition.overtimeHours(shifts).longValue());
  }
  @Test
  public void consecutiveDoubletimeDefinitionMaxHours() {

    ConsecutiveDaysOvertimeDefinition definition = new ConsecutiveDaysOvertimeDefinition()
        .setMinimumDay(2L)
        .setMaximumDay(Long.MAX_VALUE)
        .setMinimumHours(3L)
        .setMaximumHours(Long.MAX_VALUE)
        .setOvertimeType("DBL");

    List<Shift> shifts = new ArrayList<>();

    Shift shift1 = new Shift()
        .setStart(LocalDateTime.parse("2018-03-12 02:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 04:00:00", dtf));
    shifts.add(shift1);

    Shift shift2 = new Shift()
        .setId("2")
        .setStart(LocalDateTime.parse("2018-03-13 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 07:00:00", dtf));
    shifts.add(shift2);

    Shift shift3 = new Shift()
        .setId("3")
        .setStart(LocalDateTime.parse("2018-03-14 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 09:00:00", dtf));
    shifts.add(shift3);

    assertEquals(200, definition.overtimeHours(shifts).longValue());
  }

  @Test
  public void consecutiveDays() {

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L).setOvertimeRuleId("1").setCost(1000L);
    ConsecutiveDaysOvertimeDefinition definition = new ConsecutiveDaysOvertimeDefinition()
        .setId("1")
        .setMinimumDay(2L)
        .setMaximumDay(Long.MAX_VALUE)
        .setMinimumHours(0L)
        .setMaximumHours(Long.MAX_VALUE)
        .setOvertimeType("OT");

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-12 02:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-12 04:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-13 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-13 07:00:00", dtf));

    Shift shift3 = new Shift()
        .setId("3")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-03-14 05:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-14 07:00:00", dtf));

    PayPeriod period = new PayPeriod().setId("1")
        .setStart(LocalDateTime.parse("2018-03-11 00:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-03-15 23:59:59", dtf));

    ksession.insert(employee);
    ksession.insert(definition);
    ksession.insert(shift1);
    ksession.insert(shift2);
    ksession.insert(shift3);
    ksession.insert(period);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter("^Overtime consecutive --.*"));
    assertEquals(-1L, getScoreHolder().getSoftScore());

  }
}

