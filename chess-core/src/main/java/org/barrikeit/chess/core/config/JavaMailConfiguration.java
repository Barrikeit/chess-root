package org.barrikeit.chess.core.config;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailConfiguration {
  public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
  public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
  public static final String MAIL_SMTP_SSL_TRUST = "mail.smtp.ssl.trust";
  public static final String MAIL_DEBUG = "mail.debug";

  private ApplicationProperties.MailProperties mailProperties;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(mailProperties.getHost());
    mailSender.setPort(mailProperties.getPort());
    mailSender.setUsername(mailProperties.getUser());
    mailSender.setPassword(mailProperties.getPass());
    mailSender.setProtocol(mailProperties.getProperties().getProtocol());
    Properties props = mailSender.getJavaMailProperties();
    props.put(MAIL_SMTP_AUTH, mailProperties.getProperties().getAuth());
    props.put(MAIL_SMTP_STARTTLS_ENABLE, mailProperties.getProperties().getStarttls());
    props.put(MAIL_SMTP_SSL_TRUST, mailProperties.getHost());
    Session session =
        getMailSession(
            mailProperties.getUser(),
            mailProperties.getPass(),
            mailProperties.getProperties().getAuth(),
            mailProperties.getProperties().getStarttls(),
            mailProperties.getProperties().getDebug());
    mailSender.setSession(session);
    return mailSender;
  }

  private Session getMailSession(
      final String mailUser,
      final String mailPass,
      final String auth,
      final String starttls,
      final String debug) {
    Properties properties = new Properties();
    properties.put(MAIL_SMTP_AUTH, auth);
    properties.put(MAIL_SMTP_STARTTLS_ENABLE, starttls);
    properties.put(MAIL_DEBUG, debug);

    return Session.getInstance(
        properties,
        new Authenticator() {
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(mailUser, mailPass);
          }
        });
  }
}
