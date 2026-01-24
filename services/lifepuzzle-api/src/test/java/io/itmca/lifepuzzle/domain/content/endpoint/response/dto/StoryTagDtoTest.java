package io.itmca.lifepuzzle.domain.content.endpoint.response.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StoryTagDtoTest {

  @Test
  @DisplayName("StoryTagDto는 priority 기준으로 오름차순 정렬되어야 한다")
  void shouldSortByPriorityAscending() {
    // Given
    var tag1 = new StoryTagDto("30", "30대", 30);
    var tag2 = new StoryTagDto("10", "10대", 10);
    var tag3 = new StoryTagDto("20", "20대", 20);

    List<StoryTagDto> tags = new ArrayList<>(List.of(tag1, tag2, tag3));

    // When
    Collections.sort(tags);

    // Then
    assertEquals("10", tags.get(0).key());
    assertEquals("20", tags.get(1).key());
    assertEquals("30", tags.get(2).key());
  }

  @Test
  @DisplayName("compareTo는 priority가 작은 것이 앞에 오도록 음수를 반환해야 한다")
  void compareToShouldReturnNegativeWhenPriorityIsSmaller() {
    // Given
    var smallerPriority = new StoryTagDto("10", "10대", 10);
    var largerPriority = new StoryTagDto("30", "30대", 30);

    // When & Then
    assertTrue(smallerPriority.compareTo(largerPriority) < 0);
    assertTrue(largerPriority.compareTo(smallerPriority) > 0);
  }

  @Test
  @DisplayName("compareTo는 priority가 같으면 0을 반환해야 한다")
  void compareToShouldReturnZeroWhenPriorityIsEqual() {
    // Given
    var tag1 = new StoryTagDto("20", "20대 A", 20);
    var tag2 = new StoryTagDto("20", "20대 B", 20);

    // When & Then
    assertEquals(0, tag1.compareTo(tag2));
  }
}
