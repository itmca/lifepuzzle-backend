package io.itmca.lifepuzzle.domain.question.endpoint;

import io.itmca.lifepuzzle.domain.question.endpoint.response.QuestionQueryResponse;
import io.itmca.lifepuzzle.domain.question.service.QuestionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("question")
@RequiredArgsConstructor
public class QuestionQueryEndpoint {
    private final QuestionQueryService questionQueryService;

    @GetMapping("/recommend")
    public List<QuestionQueryResponse> getRecommendedQuestion(@RequestParam("category") String category,
                                                              @RequestParam("heroNo") Long heroNo,
                                                              @RequestParam(name = "size", defaultValue = "10") Long size ) {
        var recommendQuestions = questionQueryService.getRecommendedQuestion(category, heroNo, size);

        return recommendQuestions.stream()
                .map(QuestionQueryResponse::from)
                .toList();
    }
}
