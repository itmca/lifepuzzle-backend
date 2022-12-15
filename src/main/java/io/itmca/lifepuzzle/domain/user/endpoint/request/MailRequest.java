package io.itmca.lifepuzzle.domain.user.endpoint.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MailRequest {
    private String to;
    private String from;
    private String subject;
    private String html;
}
