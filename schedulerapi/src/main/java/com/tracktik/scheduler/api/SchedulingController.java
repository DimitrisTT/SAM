package com.tracktik.scheduler.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracktik.scheduler.api.domain.QueueNames;
import com.tracktik.scheduler.api.domain.RequestForScheduling;
import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.domain.SchedulingResponse;
import com.tracktik.scheduler.domain.SolverStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.jms.JMSException;
import javax.jms.Message;
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

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  public SchedulingResponse getSchedule(@PathVariable String id) {

    SchedulingResponse response = Session.solutions.getIfPresent(id);

    if (response == null) return null;

    if (response.getStatus() == SolverStatus.COMPLETED) {
      Session.solutions.invalidate(id);
    }

    return response;
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  public ResponseEntity<Schedule> cancelScheduling(@PathVariable String id) {

    return new ResponseEntity<Schedule>(HttpStatus.NO_CONTENT);
  }

}
