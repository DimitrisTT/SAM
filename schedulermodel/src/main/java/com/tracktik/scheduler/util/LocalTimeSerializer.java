package com.tracktik.scheduler.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("kk:mm");

  @Override
  public void serialize(LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

    String sValue = localTime.format(dtf);
    jsonGenerator.writeString(sValue);
    //System.out.println("Serialized " + localTime.toString() + " to " + sValue);
  }
}
