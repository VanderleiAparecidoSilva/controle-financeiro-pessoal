package com.vanderlei.cfp.config.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class JsonHelper {

  private final ObjectMapper mapper;

  public <T> T toObject(final String pathJson, final Class<T> clazz) {
    try {
      return mapper.readValue(getJson(pathJson), clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T jsonToObject(final String json, final Class<T> clazz) {
    try {
      return mapper.readValue(json, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public <T> List<T> toListObject(final String pathJson, final Class<T> clazz) {
    try {
      return mapper.readValue(
          getJson(pathJson), mapper.getTypeFactory().constructCollectionType(List.class, clazz));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private URL getJson(final String pathJson) {
    return this.getClass().getClassLoader().getResource(pathJson);
  }

  public String toJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  public <T> T fromJson(final String message, final Class<T> clazz) {
    try {
      return mapper.readValue(message, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
