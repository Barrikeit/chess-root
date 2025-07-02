package org.barrikeit.chess.core.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.springframework.util.ObjectUtils;

/** Utilidad para sanitizar strings. Evita nullpointers si se usa este metodo de forma directa */
public class StringSanitizer {
  private StringSanitizer() {}

  public static String sanitize(final String input) {
    return ObjectUtils.isEmpty(input)
        ? ""
        : Jsoup.clean(
            input, "", Safelist.simpleText(), new Document.OutputSettings().prettyPrint(false));
  }
}
