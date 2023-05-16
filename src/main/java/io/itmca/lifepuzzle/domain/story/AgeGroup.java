package io.itmca.lifepuzzle.domain.story;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum AgeGroup {
  UNDER_TEENAGER("10대 미만", 0), TEENAGER("10대", 10), TWENTIES("20대", 20),
  THIRTY("30대", 30), FORTY("40대", 40), FIFTY("50대", 50), SIXTY("60대", 60),
  SEVENTY("70대", 70), EIGHTY("80대", 80), NINETY("90대", 90), UPPER_NINETY("90대 이상", 100);

  private final String displayName;
  private final Integer representativeAge;
  private static final Map<Integer, AgeGroup> representAgeMap = Arrays.stream(values()).collect(
      Collectors.toUnmodifiableMap(
          ageGroup -> ageGroup.representativeAge,
          ageGroup -> ageGroup
      )
  );

  AgeGroup(String displayName, Integer representativeAge) {
    this.displayName = displayName;
    this.representativeAge = representativeAge;
  }

  public static AgeGroup of(Integer age) {
    var representativeAge = (age / 10) * 10;
    return representAgeMap.getOrDefault(representativeAge, UPPER_NINETY);
  }
}
