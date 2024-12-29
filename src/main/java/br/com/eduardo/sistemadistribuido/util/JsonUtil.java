package br.com.eduardo.sistemadistribuido.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtil {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static <T> String serialize(T obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }

  public static <T> T deserialize(String json, Class<T> clazz) throws IOException {
    return objectMapper.readValue(json, clazz);
  }

  public static JsonNode readTree(String json) throws IOException {
    return objectMapper.readTree(json);
  }

  public static <T> T treeToValue(JsonNode jsonNode, Class<T> clazz) throws JsonProcessingException {
    return objectMapper.treeToValue(jsonNode, clazz);
  }
}