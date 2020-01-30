package com.tracktik.scheduler.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/*
 * This class used to have a pingable endpoint for debugging purposes, or integrative checkups that endpoints are active
 */
@RestController
@RequestMapping("/ping")
public class PingController {

  private static final Logger logger = LoggerFactory.getLogger(PingController.class);

  @Autowired
  private JmsTemplate jmsTemplate;

  @RequestMapping(method = RequestMethod.GET)
  public void getSchedule() {

    return;
  }

}
