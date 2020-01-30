package com.tracktik.scheduler.api;

import com.tracktik.scheduler.api.domain.QueueNames;
import com.tracktik.scheduler.domain.SchedulingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/*
 * This class serves to hold the response from the service after the endpoint has relayed the 
 * Json to the marshaller and set off the optaplanner configurative solver.
 *
 */
@Component
public class Receiver {

  Logger logger = LoggerFactory.getLogger(Receiver.class);

  @JmsListener(destination = QueueNames.response, containerFactory = "schedulerFactory")
  public void receiveMessage(SchedulingResponse response) {
    logger.info("Received " + response.getStatus() + " response for " + response.getId());
    Session.solutions.put(response.getId(), response);
  }

}
