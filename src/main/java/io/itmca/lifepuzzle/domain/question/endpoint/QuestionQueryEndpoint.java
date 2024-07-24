package io.itmca.lifepuzzle.domain.question.endpoint;

import io.itmca.lifepuzzle.domain.question.endpoint.response.dto.RecommendQuestionDTO;
import io.itmca.lifepuzzle.domain.question.service.QuestionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "질문 조회")
public class QuestionQueryEndpoint {
  private final QuestionQueryService questionQueryService;

  @Deprecated
  @Operation(summary = "추천 질문 조회")
  @GetMapping({"/question/recommend", // TODO: FE 전환 후 제거
      "/questions/recommend"})
  public List<RecommendQuestionDTO> getRecommendedQuestion(
      @RequestParam(name = "category", required = false) String category,
      @RequestParam(name = "heroNo") Long heroNo,
      @RequestParam(name = "size", defaultValue = "10") Long size) {
    var recommendQuestions = questionQueryService.getRecommendQuestion(category, heroNo, size);

    return recommendQuestions.stream()
        .map(RecommendQuestionDTO::from)
        .toList();
  }

  @Operation(summary = "월별 추천 질문 조회")
  @GetMapping("/questions/month-recommend")
  public List<RecommendQuestionDTO> getRecommendedQuestion(
      @Min(-1) @Max(12)
      @RequestParam(name = "heroNo") Long heroNo,
      @RequestParam(name = "month", required = false) Integer month,
      @RequestParam(name = "size", defaultValue = "4", required = false) Long size) {
    if (month == null) {
      month = LocalDate.now().getMonthValue();
    }

    var recommendQuestions = questionQueryService.getRecommendQuestion(month + "월", heroNo, size);

    return recommendQuestions.stream()
        .map(RecommendQuestionDTO::from)
        .toList();
  }
}
