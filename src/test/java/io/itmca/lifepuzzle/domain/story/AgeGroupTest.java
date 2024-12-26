package io.itmca.lifepuzzle.domain.story;

import static org.assertj.core.api.Assertions.assertThat;

import io.itmca.lifepuzzle.domain.story.type.AgeGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeGroupTest {

  // TODO 적절한 테스트 케이스 추가 및 수정 필요
  @DisplayName("10대부터 90대까지 적절한 AgeGroup으로 변환한다")
  @ParameterizedTest
  @CsvSource({
      "1,UNDER_TEENAGER",
      "11, TEENAGER"
  })
  public void testAgeGroupByAge(Integer age, AgeGroup expectedAgeGroup) {
    // Given
    // When
    var ageGroup = AgeGroup.of(age);
    // Then
    assertThat(ageGroup).isSameAs(expectedAgeGroup);
  }
}
