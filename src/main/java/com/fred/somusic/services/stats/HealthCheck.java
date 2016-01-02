package com.fred.somusic.services.stats;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

  @RequestMapping("/services/stats/healthcheck")
  public String healthcheck() {
    return "OK";
  }

}
