package com.tracktik.scheduler.api;

import com.tracktik.scheduler.domain.SchedulingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  Logger logger = LoggerFactory.getLogger(Receiver.class);

  @JmsListener(destination = "schedule.response", containerFactory = "schedulerFactory")
  public void receiveMessage(SchedulingResponse response) {
    logger.info("Received response for " + response.id);
    Session.solutions.put(response.id, response);
  }

}
