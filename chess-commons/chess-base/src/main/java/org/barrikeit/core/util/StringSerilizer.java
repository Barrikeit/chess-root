package org.barrikeit.core.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

/**
 * Clase que se encarga de escapar los caracteres especiales
 * de los string devueltos por los servicios Json.
 */
public class StringSerilizer extends JsonSerializer<String> {

  @Override
  public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    if (value != null) {
      gen.writeString(sanitize(value));
    } else {
      gen.writeString(value);
    }
  }

  private String sanitize(final String input) {
    if (!ObjectUtils.isEmpty(input)) {
      return Jsoup.clean(
              input, "", Safelist.simpleText(), new Document.OutputSettings().prettyPrint(false));
    }
    return input;
  }
}
