package org.barrikeit.core.service.mail;

import org.barrikeit.core.util.enums.MailTipo;

public interface EmailService {
  void sendEmail(String mailReceiver, String[] ccs, String mailSubject, String mailBody);

  String getMailSubject(MailTipo mailTipo);
}
