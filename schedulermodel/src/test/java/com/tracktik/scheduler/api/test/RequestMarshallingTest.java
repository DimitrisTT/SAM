package com.tracktik.scheduler.api.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracktik.scheduler.api.domain.RequestForScheduling;
import com.tracktik.scheduler.domain.EmployeeConstraintMultiplier;
import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.util.RequestResponseMapper;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;

//@RunWith(SpringJUnit4ClassRunner.class)
public class RequestMarshallingTest {

  @Test
  public void employeeMultipliers() throws ParseException {

    ObjectMapper mapper = new ObjectMapper();
    try {
      RequestForScheduling request = mapper.readValue(new File("src/test/data/parameterized.json"), RequestForScheduling.class);
      System.out.println(request);
      assert(request.employee_multipliers.containsKey("1235"));
      Schedule schedule= RequestResponseMapper.requestToSchedule(UUID.randomUUID().toString(), request);
      Optional<EmployeeConstraintMultiplier> optional = schedule.getEmployeeConstraintMultipliers().stream().filter(employeeConstraintMultiplier -> employeeConstraintMultiplier.getEmployeeId().equals("1235") && employeeConstraintMultiplier.getName().equals("MINIMUM_REST_PERIOD")).findAny();
      assert optional.isPresent();
    } catch (IOException e) {
      e.printStackTrace();
      assert(false);
    }

  }

}

