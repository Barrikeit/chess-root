package org.barrikeit.core.service.excel;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {

  void downloadXlsx(HttpServletResponse response, Class<?> clazz, List<?> data);

  void downloadXlsx(
      HttpServletResponse response,
      String fileName,
      String sheetName,
      List<Field> headers,
      List<?> data);

  XSSFWorkbook generateXlsx(String spreadSheetName, List<Field> headers, List<?> data);

  String[] getColumnsFromXlsx(MultipartFile file);

  String[] getColumnsFromXlsx(File file);

  boolean isValidXLSX(MultipartFile file);
}
