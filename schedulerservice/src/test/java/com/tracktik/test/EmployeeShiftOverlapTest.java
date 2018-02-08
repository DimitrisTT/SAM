package com.tracktik.test;

import com.tracktik.scheduler.domain.*;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeShiftOverlapTest {

  @Test
  public void employeeNotAvailableDaysBeforeShift() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 22:00:00", "2018-01-18 02:00:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.SUNDAY) //Shift is on Wednesday
        .setStartTime(LocalTime.MIDNIGHT.plus(12L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(20L, ChronoUnit.HOURS));

    Boolean overlaps = shift.overlaps(availability);

    assert(!overlaps);
  }
  @Test
  public void employeeNotAvailableJustBeforeShift() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 22:00:00", "2018-01-18 02:00:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.WEDNESDAY) //Shift is on Wednesday
        .setStartTime(LocalTime.MIDNIGHT.plus(12L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(20L, ChronoUnit.HOURS));

    Boolean overlaps = shift.overlaps(availability);

    assert(!overlaps);
  }
  @Test
  public void employeeNotAvailableAdjacentStartShift() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 22:00:00", "2018-01-18 02:00:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.WEDNESDAY) //Shift is on Wednesday
        .setStartTime(LocalTime.MIDNIGHT.plus(12L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(22L, ChronoUnit.HOURS));

    Boolean overlaps = shift.overlaps(availability);

    assert(!overlaps);
  }
  @Test
  public void employeeNotAvailableOverStartShift() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 22:00:00", "2018-01-18 02:00:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.WEDNESDAY) //Shift is on Wednesday
        .setStartTime(LocalTime.MIDNIGHT.plus(12L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(23L, ChronoUnit.HOURS));

    Boolean overlaps = shift.overlaps(availability);

    assert(overlaps);
  }
  @Test
  public void employeeNotAvailableForShift() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 12:00:00", "2018-01-18 02:00:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.WEDNESDAY) //Shift is on Wednesday
        .setStartTime(LocalTime.MIDNIGHT.plus(12L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(14L, ChronoUnit.HOURS));

    Boolean overlaps = shift.overlaps(availability);

    assert(overlaps);
  }
  @Test
  public void employeeNotAvailableOverEndShift() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 22:00:00", "2018-01-18 02:00:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.THURSDAY) //Shift is on Wednesday
        .setStartTime(LocalTime.MIDNIGHT.plus(0L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(8L, ChronoUnit.HOURS));

    Boolean overlaps = shift.overlaps(availability);

    assert(overlaps);
  }
  @Test
  public void employeeNotAvailableAdjacentEndShift() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 22:00:00", "2018-01-18 02:00:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.THURSDAY)
        .setStartTime(LocalTime.MIDNIGHT.plus(2L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(10L, ChronoUnit.HOURS));

    Boolean overlaps = shift.overlaps(availability);

    assert(!overlaps);
  }
  @Test
  public void employeeNotAvailableJustAfterShift() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 22:00:00", "2018-01-18 02:00:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.THURSDAY) //Shift is on Wednesday
        .setStartTime(LocalTime.MIDNIGHT.plus(3L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(11L, ChronoUnit.HOURS));

    Boolean overlaps = shift.overlaps(availability);

    assert(!overlaps);
  }

  @Test
  public void employeeNotAvailableDayAfterShift() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 22:00:00", "2018-01-18 02:00:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.FRIDAY) //Shift is on Wednesday
        .setStartTime(LocalTime.MIDNIGHT.plus(3L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(11L, ChronoUnit.HOURS));

    Boolean overlaps = shift.overlaps(availability);

    assert(!overlaps);
  }

  @Test
  public void employeeNotAvailableMondayShiftOnMonday() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Monday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-02-05 00:00:00", "2018-02-05 08:00:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.MONDAY)
        .setStartTime(LocalTime.MIDNIGHT)
        .setEndTime(LocalTime.MAX);

    Boolean overlaps = shift.overlaps(availability);

    assert(overlaps);
  }

  @Test
  public void employeeNotAvailableMondayShiftEndsOnSunday() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Monday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-02-04 16:00:00", "2018-02-05 00:00:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.MONDAY)
        .setStartTime(LocalTime.MIDNIGHT)
        .setEndTime(LocalTime.MAX);

    Boolean overlaps = shift.overlaps(availability);

    assert(!overlaps);
  }

  @Test
  public void employeeNotAvailableMondayShiftOnTuesday() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Monday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-02-06 00:00:00", "2018-02-06 08:00:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.MONDAY)
        .setStartTime(LocalTime.MIDNIGHT)
        .setEndTime(LocalTime.MAX);

    Boolean overlaps = shift.overlaps(availability);

    assert(!overlaps);
  }

  @Test
  public void employeeTEst() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Monday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-02-04 23:00:00", "2018-02-05 02:00:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.MONDAY)
        .setStartTime(LocalTime.MIDNIGHT)
        .setEndTime(LocalTime.MAX);

    Boolean overlaps = shift.overlaps(availability);

    assert(overlaps);
  }

}

