package com.tracktik.scheduler.api;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tracktik.scheduler.domain.SchedulingResponse;

import java.util.concurrent.TimeUnit;

public class Session {

  public static final Cache<String, SchedulingResponse> solutions = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).build();

}
