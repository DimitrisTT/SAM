package com.tracktik.scheduler.application;

import com.tracktik.scheduler.domain.Schedule;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  @JmsListener(destination = "tracktik.scheduler", containerFactory = "schedulerFactory")
  public void receiveMessage(Schedule schedule) {
    System.out.println("Received <" + schedule + ">");
  }
}
