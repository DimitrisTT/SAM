package com.tracktik.scheduler.api;

import com.tracktik.scheduler.api.domain.QueueNames;
import com.tracktik.scheduler.api.domain.RequestForScheduling;
import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.domain.SchedulingResponse;
import com.tracktik.scheduler.domain.SolverStatus;
import com.tracktik.scheduler.util.RequestResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * This class controls scheduling object; holding it in queue and setting it off.
 * Receiving the request, this calls the mapper to marshal the json to an object
 * as well as set off the service to start solving for its solution.
 *
 */ 
@RestController
@RequestMapping("/schedule")
public class SchedulingController {

  private static final Logger logger = LoggerFactory.getLogger(SchedulingController.class);

  @Autowired
  private JmsTemplate jmsTemplate;

/**
 * This is the method to set off the solver and schedule the employees.
 * It is set off by a post call to the endpoint, and takes in the request.
 * @param request This is the request object, marshalled in from the POSTed Json
 *
 */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<?> schedule(@RequestBody RequestForScheduling request) {

    String id = UUID.randomUUID().toString();

    Schedule schedule = RequestResponseMapper.requestToSchedule(id, request);

    logger.info("Placing request " + id + " on queue ");

    SchedulingResponse response = new SchedulingResponse();
    response.setId(schedule.getId()).setStatus(SolverStatus.QUEUED);

    Session.solutions.put(schedule.getId(), response);

    jmsTemplate.convertAndSend(QueueNames.request, schedule);
    logger.info("Sending response to caller for " + schedule.getId());

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest().path("/{id}")
        .buildAndExpand(id).toUri();


    return ResponseEntity.created(location).build();
  }

/**
 * This method gets the schedule from a call, viewing solutions as they become available on the session.
 * It is set off by a GET REST call, with the id of the schedule being looked for.
 * @param id this is the string of the id representing the schedule being looked for.
 */
  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  public SchedulingResponse getSchedule(@PathVariable String id) {

    SchedulingResponse response = Session.solutions.getIfPresent(id);

    if (response == null) return null;

    if (response.getStatus() == SolverStatus.COMPLETED) {
      Session.solutions.invalidate(id);
    }

    return response;
  }


/**
 * This method deletes the schedule from a call, removing a select response entity from the session.
 * It is set off by a DELETE REST call, with the id of the schedule being looked for.
 * @param id this is the string of the id representing the schedule being looked for.
 */
  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  public ResponseEntity<Schedule> cancelScheduling(@PathVariable String id) {

    return new ResponseEntity<Schedule>(HttpStatus.NO_CONTENT);
  }

}
