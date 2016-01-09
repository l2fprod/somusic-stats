package com.fredericlavigne.somusic.services.stats;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.fredericlavigne.somusic.common.BaseTask;

@Component
public class HealthCheck extends BaseTask implements HealthIndicator {

  @Override
  public Health health() {    
    try {
      getStatsDb();
      return Health.up().build();
    } catch (Exception e) {
      return Health.down(e).build();
    }
  }

}