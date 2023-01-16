package io.itmca.lifepuzzle.domain.story;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeGroupTest {

    @DisplayName("나이가 주어졌을 때 적절한 AgeGroup으로 변환한다")
    @ParameterizedTest
    @CsvSource({"1,UNDER_TEENAGER", "11, TEENAGER"})
    public void getAgeGroupByAgeTest(Long age, AgeGroup expectedAgeGroup){
        // Given
        // When
        var ageGroup = AgeGroup.of(age);
        // Then
        assertThat(ageGroup).isSameAs(expectedAgeGroup);
    }
}