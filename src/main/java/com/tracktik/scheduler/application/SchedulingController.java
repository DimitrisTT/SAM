package com.tracktik.scheduler.application;

import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.application.domain.RequestForScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/schedule")
public class SchedulingController {

  private static final Logger logger = LoggerFactory.getLogger(SchedulingController.class);

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<?> schedule(@RequestBody RequestForScheduling request) {

    String id = UUID.randomUUID().toString();
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest().path("/{id}")
        .buildAndExpand(id).toUri();

    logger.info("Got a request " + request);

    return ResponseEntity.created(location).build();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  public Schedule getSchedule(@PathVariable String id) {

    return new Schedule();
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  public ResponseEntity<Schedule> cancelScheduling(@PathVariable String id) {

    return new ResponseEntity<Schedule>(HttpStatus.NO_CONTENT);
  }

}
