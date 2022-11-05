package io.itmca.lifepuzzle.domain.question.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("question")
public class QuestionQueryEndpoint {

    @GetMapping("/recommend")
    public void getRecommendedQuestion() {}
}
