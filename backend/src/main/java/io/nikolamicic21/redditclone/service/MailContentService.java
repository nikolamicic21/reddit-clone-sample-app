package io.nikolamicic21.redditclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailContentService {

    private final TemplateEngine templateEngine;

    public String render(@NonNull String message) {
        var context = new Context();
        context.setVariable("message", message);

        return this.templateEngine.process("verification-mail-template", context);
    }

}
