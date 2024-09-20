package org.barrikeit.core.config;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class JavaMailConfiguration {
  public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
  public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
  public static final String MAIL_SMTP_SSL_TRUST = "mail.smtp.ssl.trust";
  public static final String MAIL_DEBUG = "mail.debug";

  @Value("${mail.host:null}")
  private String mailHost;

  @Value("${mail.port:0}")
  private int mailPort;

  @Value("${mail.user:null}")
  private String mailUser;

  @Value("${mail.pass:null}")
  private String mailPass;

  @Value("${mail.properties.protocol:null}")
  private String protocol;

  @Value("${mail.properties.auth:false}")
  private String auth;

  @Value("${mail.properties.starttls:false}")
  private String starttls;

  @Value("${mail.properties.debug:false}")
  private String debug;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(mailHost);
    mailSender.setPort(mailPort);
    mailSender.setUsername(mailUser);
    mailSender.setPassword(mailPass);
    mailSender.setProtocol(protocol);
    Properties props = mailSender.getJavaMailProperties();
    props.put(MAIL_SMTP_AUTH, auth);
    props.put(MAIL_SMTP_STARTTLS_ENABLE, starttls);
    props.put(MAIL_SMTP_SSL_TRUST, mailHost);
    Session session = getMailSession(mailUser, mailPass, auth, starttls, debug);
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
