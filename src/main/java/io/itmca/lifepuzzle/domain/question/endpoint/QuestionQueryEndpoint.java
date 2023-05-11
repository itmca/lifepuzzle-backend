package io.itmca.lifepuzzle.domain.question.endpoint;

import io.itmca.lifepuzzle.domain.question.endpoint.response.RecommendQuestionDTO;
import io.itmca.lifepuzzle.domain.question.service.QuestionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("question")
@RequiredArgsConstructor
@Tag(name="질문 조회 API")
public class QuestionQueryEndpoint {
    private final QuestionQueryService questionQueryService;

    @Operation(summary = "추천 질문 조회")
    @GetMapping("/recommend")
    public List<RecommendQuestionDTO> getRecommendedQuestion(@RequestParam(name = "category", required = false) String category,
                                                             @RequestParam(name = "heroNo") Long heroNo,
                                                             @RequestParam(name = "size", defaultValue = "10") Long size ) {
        var recommendQuestions = questionQueryService.getRecommendedQuestion(category, heroNo, size);

        return recommendQuestions.stream()
                .map(RecommendQuestionDTO::from)
                .toList();
    }
}
