package com.tracktik.scheduler.application;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tracktik.scheduler.application.domain.SchedulingResponse;

import java.util.concurrent.TimeUnit;

public class Session {

  public static final Cache<String, SchedulingResponse> solutions = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.HOURS).build();

}
