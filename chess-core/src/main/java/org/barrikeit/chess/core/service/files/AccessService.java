package org.barrikeit.chess.core.service.files;

import static org.barrikeit.chess.core.util.constants.FileConstants.ATTACHMENT;
import static org.barrikeit.chess.core.util.constants.FileConstants.CONTENT_TYPE_ACCESS;
import static org.barrikeit.chess.core.util.constants.FileConstants.EXTENSION_ACCESS;
import static org.barrikeit.chess.core.util.constants.FileConstants.FILENAME;
import static org.barrikeit.chess.core.util.constants.FileConstants.HEADER_KEY;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.barrikeit.chess.core.service.dto.GenericDto;
import org.barrikeit.chess.core.util.AccessUtil;
import org.barrikeit.chess.core.util.ReflectionUtil;
import org.barrikeit.chess.core.util.annotations.AccessColumn;
import org.barrikeit.chess.core.util.annotations.AccessTable;
import org.barrikeit.chess.core.util.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AccessService {

  private final MessageSource messageSource;

  @Autowired
  public AccessService(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public void downloadAccess(HttpServletResponse response, File accessFile, String fileName) {
    String headerValue = FILENAME + fileName + EXTENSION_ACCESS;
    response.setHeader(HEADER_KEY, ATTACHMENT + headerValue);
    response.setContentType(CONTENT_TYPE_ACCESS + headerValue);
    try {
      ServletOutputStream output = response.getOutputStream();
      // write en output
      InputStream is = new FileInputStream(accessFile);
      byte[] byteArray = IOUtils.toByteArray(is);
      is.close();
      IOUtils.write(byteArray, output);
    } catch (IOException e) {
      log.error("IOException al descargar el fichero Access {}.\n {}", fileName, e);
      throw new BadRequestException("error.msg.access.descarga", fileName);
    }
  }

  @SuppressWarnings("unchecked")
  public <G extends GenericDto> List<G> fetchData(String filePath, Class<G> inputClass) {
    Map<String, Object> accessTable =
        ReflectionUtil.getAnnotationProperties(inputClass, AccessTable.class);
    Database database = AccessUtil.getDataBase(filePath);
    Table table = AccessUtil.getTable(database, accessTable.get("name").toString());
    List<G> items = new ArrayList<>();
    for (Row row : table) {
      G item = (G) ReflectionUtil.newInstance(inputClass);
      if (item != null) {
        mapRowToItem(row, inputClass);
        items.add(item);
      }
    }
    return items;
  }

  public <G extends GenericDto> void mapRowToItem(Row row, Class<G> inputClass) {
    List<Field> fields =
        ReflectionUtil.getFieldsWithAnnotation(inputClass, AccessColumn.class).stream().toList();
    for (Field field : fields) {
      Object rowValue = row.get(field.getAnnotation(AccessColumn.class).name());
      Object value = ReflectionUtil.castFieldToType(rowValue, field.getType());
      if (value != null && !field.getName().equals("id"))
        ReflectionUtil.setFieldValue(inputClass, field.getName(), value);
    }
  }

  public <G extends GenericDto> void insertData(
      String pathPlantilla, Collection<G> listaDatosCargaDto, Class<G> outputClass) {
    try {
      Map<String, Object> accessTable =
          ReflectionUtil.getAnnotationProperties(outputClass, AccessTable.class);
      Database database = AccessUtil.getDataBase(pathPlantilla);
      Table table = AccessUtil.getTable(database, accessTable.get("name").toString());
      List<Map<String, Object>> rows = new ArrayList<>();
      for (G datoCargaDto : listaDatosCargaDto) {
        rows.add(
            ReflectionUtil.getMapFieldValues(
                datoCargaDto,
                ReflectionUtil.getAnnotatedFields(datoCargaDto.getClass(), AccessColumn.class)));
      }
      for (Map<String, Object> row : rows) {
        table.addRowFromMap(row);
      }
      database.close();
    } catch (IOException e) {
      log.error("Error loading plantilla", e);
    }
  }
}
