package org.barrikeit.chess.core.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.barrikeit.chess.core.config.ApplicationProperties;
import org.barrikeit.chess.core.util.enums.MailTipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

  @Autowired private JavaMailSender emailsender;
  @Autowired private MessageSource messageSource;
  private ApplicationProperties.MailProperties mailProperties;

  @Override
  public void sendEmail(String receiver, String[] ccs, String subject, String mailBody) {
    try {
      MimeMessage message = emailsender.createMimeMessage();
      MimeMessageHelper helper =
          new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
      helper.setFrom(mailProperties.getFrom());
      helper.setTo(receiver);
      if (ccs != null) {
        helper.setCc(ccs);
      }
      helper.setSubject(subject);
      helper.setText(mailBody, true);
      helper.addInline("ajedrezillo_logo", new ClassPathResource("static/images/ajedrezillo.svg"));
      emailsender.send(message);
    } catch (MessagingException messagingException) {
      String logMessage =
          "\nError al generar el email: "
              + logDetails(receiver, subject, mailBody, messagingException);
      log.error(logMessage, messagingException);
    } catch (MailException mailException) {
      String logMessage =
          "\nError al enviar el email: " + logDetails(receiver, subject, mailBody, mailException);
      log.error(logMessage, mailException);
    }
  }

  private String logDetails(
      String mailReceiver, String mailSubject, String mailBody, Exception exception) {
    return "\nmailReceiver: "
        + mailReceiver
        + "\nmailSubject: "
        + mailSubject
        + "\nmailBody: "
        + mailBody
        + "\nstackTrace: "
        + exception;
  }

  @Override
  public String getMailSubject(MailTipo mailTipo) {
    return switch (mailTipo) {
      case CREATE_USER ->
          messageSource.getMessage(
              "mail.subject.createUser", null, LocaleContextHolder.getLocale());
      case VERIFY_USER ->
          messageSource.getMessage(
              "mail.subject.verifyUser", null, LocaleContextHolder.getLocale());
      case BAN_USER ->
          messageSource.getMessage("mail.subject.banUser", null, LocaleContextHolder.getLocale());
      case UPDATE_USER ->
          messageSource.getMessage(
              "mail.subject.updateUser", null, LocaleContextHolder.getLocale());
    };
  }
}
