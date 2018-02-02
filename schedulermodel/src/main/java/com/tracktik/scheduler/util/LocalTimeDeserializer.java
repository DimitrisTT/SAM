package com.tracktik.scheduler.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("kk:mm");

  @Override
  public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

    String sValue = jsonParser.getValueAsString();
    LocalTime time = LocalTime.parse(jsonParser.getValueAsString(), dtf);

    return time;
  }
}
