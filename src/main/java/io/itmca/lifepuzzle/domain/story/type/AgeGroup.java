package io.itmca.lifepuzzle.domain.story.type;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum AgeGroup {
  UNDER_TEENAGER("10대 미만", 0, "under10"),
  TEENAGER("10대", 10, "10s"),
  TWENTIES("20대", 20, "20s"),
  THIRTY("30대", 30, "30s"),
  FORTY("40대", 40, "40s"),
  FIFTY("50대", 50, "50s"),
  SIXTY("60대", 60, "60s"),
  SEVENTY("70대", 70, "70s"),
  EIGHTY("80대", 80, "80s"),
  NINETY("90대", 90, "90s"),
  UPPER_NINETY("90대 이상", 100, "upper90");

  private final String displayName;
  private final Integer representativeAge;
  private final String tagKey;

  private static final Map<Integer, AgeGroup> representAgeMap = Arrays.stream(values()).collect(
      Collectors.toUnmodifiableMap(
          ageGroup -> ageGroup.representativeAge,
          ageGroup -> ageGroup
      )
  );

  AgeGroup(String displayName, Integer representativeAge, String tagKey) {
    this.displayName = displayName;
    this.representativeAge = representativeAge;
    this.tagKey = tagKey;
  }

  public static AgeGroup of(Integer age) {
    var representativeAge = (age / 10) * 10;
    return representAgeMap.getOrDefault(representativeAge, UPPER_NINETY);
  }

  public int getStartYear(LocalDate birthdate) {
    return birthdate.getYear() + this.representativeAge;
  }

  public int getEndYear(LocalDate birthdate) {
    return getStartYear(birthdate) + 9;
  }
}
