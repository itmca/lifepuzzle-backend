package io.itmca.lifepuzzle.domain.question.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("question")
public class QuestionQueryEndpoint {

    @GetMapping("/recommend")
    public void getRecommendedQuestion() {}
}
