package io.nikolamicic21.redditclone.service;

import io.nikolamicic21.redditclone.exception.RedditCloneException;
import io.nikolamicic21.redditclone.model.NotificationMail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailOutboundService {

    private final MailContentService mailContentService;
    private final JavaMailSender mailSender;

    @Async
    public void send(@NonNull NotificationMail notificationMail) {
        try {
            final MimeMessagePreparator messagePreparator = mimeMessage -> {
                var messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom("reddit.clone@email.com");
                messageHelper.setTo(notificationMail.getRecipient());
                messageHelper.setSubject(notificationMail.getSubject());
                final var text = this.mailContentService.render(notificationMail.getBody());
                messageHelper.setText(text, true);
            };

            this.mailSender.send(messagePreparator);
            log.debug("Verification E-Mail sent!");
        } catch (MailException e) {
            throw new RedditCloneException("Exception occurred when sending E-Mail to " + notificationMail.getRecipient());
        }
    }
}
