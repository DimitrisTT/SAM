package com.tracktik.scheduler.application;

import com.tracktik.scheduler.application.domain.SchedulingResponse;
import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.application.domain.RequestForScheduling;
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

@RestController
@RequestMapping("/schedule")
public class SchedulingController {

  private static final Logger logger = LoggerFactory.getLogger(SchedulingController.class);

  @Autowired
  private JmsTemplate jmsTemplate;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<?> schedule(@RequestBody RequestForScheduling request) {

    String id = UUID.randomUUID().toString();
    Schedule schedule = request.toSchedule(id);

    logger.info("Placing request on queue " + id);

    Session.solutions.put(schedule.getId(), new SchedulingResponse(schedule, Long.MIN_VALUE, Long.MIN_VALUE, SolverStatus.QUEUED));

    jmsTemplate.convertAndSend("tracktik.scheduler", schedule);
    logger.info("Sending response to caller");

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest().path("/{id}")
        .buildAndExpand(id).toUri();


    return ResponseEntity.created(location).build();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  public SchedulingResponse getSchedule(@PathVariable String id) {

    return Session.solutions.getIfPresent(id);
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  public ResponseEntity<Schedule> cancelScheduling(@PathVariable String id) {

    return new ResponseEntity<Schedule>(HttpStatus.NO_CONTENT);
  }

}
