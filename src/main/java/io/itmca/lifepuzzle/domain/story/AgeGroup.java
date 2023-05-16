package io.itmca.lifepuzzle.domain.story;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum AgeGroup {
  UNDER_TEENAGER("10대 미만", 0L), TEENAGER("10대", 10L), TWENTIES("20대", 20L),
  THIRTY("30대", 30L), FORTY("40대", 40L), FIFTY("50대", 50L), SIXTY("60대", 60L),
  SEVENTY("70대", 70L), EIGHTY("80대", 80L), NINETY("90대", 90L), UPPER_NINETY("90대 이상", 100L);

  private final String displayName;
  private final Long priority;
  private static final Map<Long, AgeGroup> representAgeMap = Arrays.stream(values()).collect(
      Collectors.toUnmodifiableMap(
          ageGroup -> ageGroup.priority,
          ageGroup -> ageGroup
      )
  );

  AgeGroup(String displayName, Long priority) {
    this.displayName = displayName;
    this.priority = priority;
  }

  public static AgeGroup of(Long age) {
    return representAgeMap.getOrDefault(age, UPPER_NINETY);
  }
}
