package org.barrikeit.chess.core.service.files;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.barrikeit.chess.core.util.ExcelUtil;
import org.barrikeit.chess.core.util.ReflectionUtil;
import org.barrikeit.chess.core.util.TimeUtil;
import org.barrikeit.chess.core.util.annotations.ExcelColumn;
import org.barrikeit.chess.core.util.annotations.ExcelFile;
import org.barrikeit.chess.core.util.constants.ExceptionConstants;
import org.barrikeit.chess.core.util.constants.FileConstants;
import org.barrikeit.chess.core.util.constants.UtilConstants;
import org.barrikeit.chess.core.util.exceptions.BadRequestException;
import org.barrikeit.chess.core.util.exceptions.FieldValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
public class ExcelService {

  private final MessageSource messageSource;

  @Autowired
  public ExcelService(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public void downloadXlsx(HttpServletResponse response, Class<?> clazz, List<?> data) {
    Map<String, Object> excelFile = ReflectionUtil.getAnnotationProperties(clazz, ExcelFile.class);
    List<Field> headers = ExcelUtil.getExcelColumns(clazz, false);
    String fileName =
        this.messageSource.getMessage(
                excelFile.get("file").toString(), null, LocaleContextHolder.getLocale())
            + "_"
            + TimeUtil.localDateTimeNow().format(UtilConstants.DATE_TIME_FORMATTER_DOWNLOAD);
    String sheetName =
        this.messageSource.getMessage(
            excelFile.get("sheet").toString(), null, LocaleContextHolder.getLocale());
    downloadXlsx(response, fileName, sheetName, headers, data);
  }

  public void downloadXlsx(
      HttpServletResponse response,
      String fileName,
      String sheetName,
      List<Field> headers,
      List<?> data) {
    try {
      XSSFWorkbook workbook = generateXlsx(sheetName, headers, data);
      String headerValue = FileConstants.FILENAME + fileName + FileConstants.EXTENSION_EXCEL;
      response.setHeader(FileConstants.HEADER_KEY, FileConstants.ATTACHMENT + headerValue);
      response.setContentType(FileConstants.CONTENT_TYPE_EXCEL + headerValue);

      ServletOutputStream output = response.getOutputStream();
      workbook.write(output);
      workbook.close();
    } catch (IOException e) {
      throw new BadRequestException(ExceptionConstants.ERROR_DESCARGA_EXCEL, e.getMessage());
    }
  }

  public XSSFWorkbook generateXlsx(String spreadSheetName, List<Field> headers, List<?> data) {
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet(spreadSheetName);
    int[] maxColumnWidths = new int[headers.size()];

    // Mapa de estilos por libro de trabajo
    Map<String, CellStyle> cellStylesCache = new HashMap<>();

    createHeader(workbook, sheet, headers, maxColumnWidths);

    createRows(headers, data, sheet, workbook, maxColumnWidths, cellStylesCache);

    return workbook;
  }

  public String[] getColumnsFromXlsx(File file) {
    try (FileInputStream fis = new FileInputStream(file)) {
      return getColumnsFromInputStream(fis);
    } catch (IOException e) {
      throw new BadRequestException("error.msg.excel.obtenerColumnas", e.getMessage());
    }
  }

  public String[] getColumnsFromXlsx(MultipartFile file) {
    try (InputStream inputStream = file.getInputStream()) {
      return getColumnsFromInputStream(inputStream);
    } catch (IOException e) {
      throw new BadRequestException("error.msg.excel.obtenerColumnas", e.getMessage());
    }
  }

  public boolean isValidXLSX(MultipartFile file) {
    try {
      Workbook workbook = WorkbookFactory.create(file.getInputStream());
      workbook.close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  public boolean isValidXLSX(File file) {
    try {
      InputStream inputStream = new FileInputStream(file);
      Workbook workbook = WorkbookFactory.create(inputStream);
      workbook.close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  private String[] getColumnsFromInputStream(InputStream inputStream) throws IOException {
    List<String> columnNames = new ArrayList<>();
    try (Workbook workbook = new XSSFWorkbook(inputStream)) {
      Sheet sheet = workbook.getSheetAt(0);
      Row headerRow = sheet.getRow(0);

      if (headerRow != null) {
        for (Cell cell : headerRow) {
          columnNames.add(cell.getStringCellValue());
        }
      }
    }
    return columnNames.toArray(new String[0]);
  }

  private void createHeader(
      XSSFWorkbook workbook, XSSFSheet sheet, List<Field> headers, int[] maxColumnWidths) {
    XSSFCellStyle headerStyle = this.createHeaderCellStyle(workbook);
    XSSFRow headerRow = sheet.createRow(0);
    int columnIndex;
    for (columnIndex = 0; columnIndex < headers.size(); ++columnIndex) {
      XSSFCell cell = headerRow.createCell(columnIndex);
      String header =
          this.messageSource.getMessage(
              headers.get(columnIndex).getAnnotation(ExcelColumn.class).name(),
              null,
              LocaleContextHolder.getLocale());
      cell.setCellValue(header);
      cell.setCellStyle(headerStyle);
      maxColumnWidths[columnIndex] = header.length();
    }

    sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, headers.size() - 1));
    sheet.createFreezePane(0, 1);
  }

  private XSSFCellStyle createHeaderCellStyle(XSSFWorkbook workbook) {
    XSSFCellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setAlignment(HorizontalAlignment.CENTER);
    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    headerStyle.setBorderTop(BorderStyle.MEDIUM);
    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
    headerStyle.setBorderLeft(BorderStyle.MEDIUM);
    headerStyle.setBorderRight(BorderStyle.MEDIUM);
    headerStyle.setWrapText(true);
    XSSFFont headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setFontName("Calibri");
    headerFont.setFontHeightInPoints((short) 12);
    headerStyle.setFont(headerFont);
    return headerStyle;
  }

  private void createRows(
      List<Field> headers,
      List<?> data,
      XSSFSheet sheet,
      XSSFWorkbook workbook,
      int[] maxColumnWidths,
      Map<String, CellStyle> cellStylesCache) {
    int columnIndex;

    for (columnIndex = 0; columnIndex < data.size(); ++columnIndex) {
      XSSFRow row = sheet.createRow(columnIndex + 1);
      Object rowData = data.get(columnIndex);
      dataRow(workbook, row, rowData, headers, maxColumnWidths, cellStylesCache);
    }

    for (columnIndex = 0; columnIndex < headers.size(); ++columnIndex) {
      int width = (int) Math.ceil((maxColumnWidths[columnIndex] + 5) * 1.24388 * 256.0);
      sheet.setColumnWidth(
          columnIndex, Math.min(width, 50000)); // Limitar el ancho de la columna a 50000
    }
  }

  private void dataRow(
      XSSFWorkbook workbook,
      XSSFRow row,
      Object rowData,
      List<Field> headers,
      int[] maxColumnWidths,
      Map<String, CellStyle> cellStylesCache) {

    for (int columnIndex = 0; columnIndex < headers.size(); ++columnIndex) {
      Field field = headers.get(columnIndex);

      try {
        Object value = ReflectionUtil.getFieldValue(rowData, field.getName());
        XSSFCell cell = row.createCell(columnIndex);
        ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);

        // Utiliza el formato para obtener o crear el estilo de celda
        String format = excelColumn.format();
        CellStyle cellStyle =
            cellStylesCache.computeIfAbsent(
                format,
                fmt -> {
                  DataFormat dataFormat = workbook.createDataFormat();
                  CellStyle newCellStyle = workbook.createCellStyle();
                  newCellStyle.setDataFormat(dataFormat.getFormat(fmt));
                  return newCellStyle;
                });

        if (value != null) {
          setCellValue(cell, value, cellStyle);
          if (value.toString().length() > maxColumnWidths[columnIndex]) {
            maxColumnWidths[columnIndex] = value.toString().length();
          }
        } else {
          cell.setCellValue("");
        }
      } catch (FieldValueException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  private void setCellValue(XSSFCell cell, Object value, CellStyle cellStyle) {
    if (value instanceof String stringValue) {
      cell.setCellValue(stringValue);
    } else if (value instanceof Integer intValue) {
      cell.setCellValue(intValue);
    } else if (value instanceof Long longValue) {
      cell.setCellValue(longValue);
    } else if (value instanceof Double doubleValue) {
      cell.setCellValue(doubleValue);
    } else if (value instanceof Float floatValue) {
      cell.setCellValue(floatValue);
    } else if (value instanceof BigDecimal bigDecimalValue) {
      cell.setCellValue(bigDecimalValue.doubleValue());
    } else if (value instanceof Date dateValue) {
      cell.setCellValue(dateValue);
    } else if (value instanceof LocalDate localDateValue) {
      cell.setCellValue(localDateValue);
    } else if (value instanceof LocalDateTime localDateTimeValue) {
      cell.setCellValue(localDateTimeValue);
    } else {
      cell.setCellValue(value.toString());
    }
    cell.setCellStyle(cellStyle);
  }
}
