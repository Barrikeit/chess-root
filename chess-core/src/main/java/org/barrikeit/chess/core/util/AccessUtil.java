package org.barrikeit.chess.core.util;

import static org.barrikeit.chess.core.util.constants.FileConstants.ATTACHMENT;
import static org.barrikeit.chess.core.util.constants.FileConstants.CONTENT_TYPE_ACCESS;
import static org.barrikeit.chess.core.util.constants.FileConstants.EXTENSION_ACCESS;
import static org.barrikeit.chess.core.util.constants.FileConstants.FILENAME;
import static org.barrikeit.chess.core.util.constants.FileConstants.HEADER_KEY;

import com.healthmarketscience.jackcess.Column;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Table;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.barrikeit.chess.core.service.dto.GenericDto;
import org.barrikeit.chess.core.util.annotations.AccessColumn;
import org.barrikeit.chess.core.util.annotations.AccessTable;
import org.barrikeit.chess.core.util.exceptions.BadRequestException;

@Log4j2
public class AccessUtil {
  private AccessUtil() {
    throw new IllegalStateException("AccessUtil class");
  }

  public static Database getDataBase(String filePath) {
    try {
      File accessFile = new File(filePath);
      return DatabaseBuilder.open(accessFile);
    } catch (NullPointerException e) {
      log.error("Ubicación del archivo nula o no válida: {} : {}", filePath, e.getMessage());
    } catch (IOException e) {
      log.error("Error abriendo el archivo Access {} : {}", filePath, e.getMessage());
    }
    return null;
  }

  public static Table getTable(Database db, String tableName) {
    if (db == null) {
      log.warn("Base de datos nula, no se puede obtener la tabla {}", tableName);
      return null;
    }

    try {
      return db.getTable(tableName);
    } catch (IOException e) {
      log.error("Error al obtener la tabla {} : {}", tableName, e.getMessage());
    }
    return null;
  }

  public static List<String> getColumnNames(Table table) {
    if (table == null) {
      log.warn("Tabla nula al intentar obtener los nombres de columna");
      return Collections.emptyList();
    }

    return table.getColumns().stream().map(Column::getName).toList();
  }

  public static boolean existsTableWithColumns(
      Database db, String tableName, List<String> expectedColumnNames) {
    if (db == null) {
      log.warn("Base de datos nula al verificar existencia de tabla {}", tableName);
      return false;
    }

    Table table = getTable(db, tableName);
    if (table == null) return false;

    Set<String> actualColumnNames =
        table.getColumns().stream().map(Column::getName).collect(Collectors.toSet());

    return actualColumnNames.containsAll(expectedColumnNames);
  }
}
