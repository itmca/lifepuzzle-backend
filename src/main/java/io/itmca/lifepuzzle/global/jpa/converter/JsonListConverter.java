package io.itmca.lifepuzzle.global.jpa.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Converter
public class JsonListConverter implements AttributeConverter<List<Integer>, String> {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<Integer> list) {
    try {
      if (list == null) {
        return "[]";
      }
      return objectMapper.writeValueAsString(list);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Failed to convert list to JSON", e);
    }
  }

  @Override
  public List<Integer> convertToEntityAttribute(String json) {
    try {
      if (json == null) {
        return Collections.emptyList();
      }
      return objectMapper.readValue(json, new TypeReference<>() {
      });
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to convert JSON to list", e);
    }
  }
}
