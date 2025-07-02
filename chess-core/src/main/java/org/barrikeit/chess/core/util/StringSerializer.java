package org.barrikeit.chess.core.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Clase que se encarga de devolver los caracteres especiales de los string devueltos por los
 * servicios Json.
 */
public class StringSerializer extends JsonSerializer<String> {

  @Override
  public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    gen.writeString(value != null ? StringSanitizer.sanitize(value) : null);
  }
}
