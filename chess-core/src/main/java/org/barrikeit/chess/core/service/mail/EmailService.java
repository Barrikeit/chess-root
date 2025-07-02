package org.barrikeit.chess.core.service.mail;

import org.barrikeit.chess.core.util.enums.MailTipo;

public interface EmailService {
  void sendEmail(String mailReceiver, String[] ccs, String mailSubject, String mailBody);

  String getMailSubject(MailTipo mailTipo);
}
