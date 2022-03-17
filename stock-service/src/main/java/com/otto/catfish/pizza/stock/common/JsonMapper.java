package com.otto.catfish.pizza.stock.common;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class JsonMapper {
  private final ObjectMapper objectMapper;

  public JsonMapper() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.findAndRegisterModules();
    this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public <T> T fromJson(String jsonString, Class<T> valueType) throws JsonProcessingException {
    return objectMapper.readValue(jsonString, valueType);
  }

  public <T> T fromJson(InputStream jsonStream, Class<T> valueType) throws IOException {
    return objectMapper.readValue(jsonStream, valueType);
  }

  public <T> String toJson(T object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
  }
}