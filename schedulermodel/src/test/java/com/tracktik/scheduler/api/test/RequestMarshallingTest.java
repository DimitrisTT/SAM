package com.tracktik.scheduler.api.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.EvictingQueue;
import com.tracktik.scheduler.api.domain.RequestForScheduling;
import com.tracktik.scheduler.domain.EmployeeConstraintMultiplier;
import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.util.RequestResponseMapper;
//import jdk.internal.jline.internal.TestAccessible;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//@RunWith(SpringJUnit4ClassRunner.class)
public class RequestMarshallingTest {

  @Test
  public void employeeMultipliers() throws ParseException {

    ObjectMapper mapper = new ObjectMapper();
    try {
      RequestForScheduling request = mapper.readValue(new File("src/test/data/parameterized.json"), RequestForScheduling.class);
      System.out.println(request);
      assert(request.employee_multipliers.containsKey("1235"));
      Schedule schedule = RequestResponseMapper.requestToSchedule(UUID.randomUUID().toString(), request);
      Optional<EmployeeConstraintMultiplier> optional = schedule.getEmployeeConstraintMultipliers().stream().filter(employeeConstraintMultiplier -> employeeConstraintMultiplier.getEmployeeId().equals("1235") && employeeConstraintMultiplier.getName().equals("MINIMUM_REST_PERIOD")).findAny();
      assert optional.isPresent();
    } catch (IOException e) {
      e.printStackTrace();
      assert(false);
    }

  }

  @Test
  public void overtimeMappedToEmployee() throws ParseException {

    ObjectMapper mapper = new ObjectMapper();
    try {
      RequestForScheduling request = mapper.readValue(new File("src/test/data/overtime.json"), RequestForScheduling.class);
      System.out.println(request);
      Schedule schedule = RequestResponseMapper.requestToSchedule(UUID.randomUUID().toString(), request);
      assert schedule.getEmployees().stream().anyMatch(employee -> employee.getOvertimeRuleId().equals("1"));
      assert schedule.getEmployees().stream().anyMatch(employee -> employee.getPayScheduleId().equals("1"));
    } catch (IOException e) {
      e.printStackTrace();
      assert(false);
    }

  }
  @Test
  public void overtimeRules() throws ParseException {

    ObjectMapper mapper = new ObjectMapper();
    try {
      RequestForScheduling request = mapper.readValue(new File("src/test/data/overtime.json"), RequestForScheduling.class);
      Schedule schedule = RequestResponseMapper.requestToSchedule(UUID.randomUUID().toString(), request);
      assert schedule.getPeriodOvertimeDefinitions().stream().anyMatch(periodOvertimeDefinition -> periodOvertimeDefinition.getName().equals("Standard. Over 40/week"));
    } catch (IOException e) {
      e.printStackTrace();
      assert(false);
    }

  }
  @Test
  public void holidayPeriods() throws ParseException {

    ObjectMapper mapper = new ObjectMapper();
    try {
      RequestForScheduling request = mapper.readValue(new File("src/test/data/export.json"), RequestForScheduling.class);
      Schedule schedule = RequestResponseMapper.requestToSchedule(UUID.randomUUID().toString(), request);
      assert schedule.getHolidayPeriods().size() != 0;
    } catch (IOException e) {
      e.printStackTrace();
      assert(false);
    }

  }

  @Test
  public void factTypes() throws ParseException {
    ObjectMapper mapper = new ObjectMapper();
    try{
      RequestForScheduling request = mapper.readValue(new File("src/test/data/export.json"), RequestForScheduling.class);
      System.out.println(request.requestFacts);
      Schedule schedule = RequestResponseMapper.requestToSchedule(UUID.randomUUID().toString(), request);
      assert schedule.getConfigFacts().size() == 12;
    } catch (IOException e) {
      e.printStackTrace();
      assert(false);
    }

    }

}

