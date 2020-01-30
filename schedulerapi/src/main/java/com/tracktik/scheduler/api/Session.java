package com.tracktik.scheduler.api;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tracktik.scheduler.domain.SchedulingResponse;

import java.util.concurrent.TimeUnit;

/**
 * This class represents the container that holds the various solutions as they get solved by the service.
 */
public class Session {

  public static final Cache<String, SchedulingResponse> solutions = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).build();

}
