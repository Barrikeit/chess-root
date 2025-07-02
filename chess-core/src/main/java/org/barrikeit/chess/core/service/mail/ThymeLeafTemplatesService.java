package org.barrikeit.chess.core.service.mail;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@AllArgsConstructor
@Service
public class ThymeLeafTemplatesService {
  private final SpringTemplateEngine templateEngine;

  public String getHTMLTemplateToString(String fileHtml, Context context) {
    return Arrays.stream(templateEngine.process(fileHtml, context).split("\n"))
        .map(String::trim)
        .collect(Collectors.joining());
  }
}
