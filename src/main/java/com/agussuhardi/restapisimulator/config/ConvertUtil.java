package com.agussuhardi.restapisimulator.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ConvertUtil {

  public static String mapToJson(Map<String, Object> map) {
    try {
      return new ObjectMapper().writeValueAsString(map);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static Object mapToObject(Map<String, Object> map) {
    final ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(map, Object.class);
  }

  public static Map<String, Object> objectToMap(Object object) {
    final ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(object, new TypeReference<>() {});
  }

  public static Map<String, Object> jsonToMap(String json) {
    final ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(json, new TypeReference<>() {});
    } catch (JsonProcessingException e) {
      return new HashMap<>();
    }
  }

  public static Object jsonToObject(String json) throws JsonProcessingException {
    final ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(json, Object.class);
  }

  public static Map<String, Object> base64ToMap(String base64) throws IOException {
    byte[] decoded = Base64.decodeBase64(base64);
    String json = new String(decoded, StandardCharsets.UTF_8);
    return new ObjectMapper().readValue(json, new TypeReference<Map<String, Object>>() {});
  }

  public static String objectToJson(Object object) throws IOException {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    return ow.writeValueAsString(object);
  }
}
