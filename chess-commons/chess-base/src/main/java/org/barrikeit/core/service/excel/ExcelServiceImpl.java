package org.barrikeit.core.service.excel;

import static org.barrikeit.core.util.constants.ApplicationConstants.HEADER_KEY;
import static org.barrikeit.core.util.constants.ApplicationConstants.PATTERN_LOCAL_DATE_TIME_DOWNLOAD;
import static org.barrikeit.core.util.constants.ApplicationConstants.RESPONSE_CONTENT_TYPE_EXCEL;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.barrikeit.core.error.BadRequestException;
import org.barrikeit.core.util.ExcelUtil;
import org.barrikeit.core.util.ReflectionUtil;
import org.barrikeit.core.util.TimeUtil;
import org.barrikeit.core.util.annotations.ExcelColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ExcelServiceImpl implements ExcelService {

  private final MessageSource messageSource;

  @Autowired
  public ExcelServiceImpl(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public void downloadXlsx(HttpServletResponse response, Class<?> clazz, List<?> data) {
    List<String> names = ExcelUtil.getExcelNames(clazz);
    List<Field> headers = ExcelUtil.getExcelColumns(clazz);
    String fileName =
        this.messageSource.getMessage(names.get(0), null, LocaleContextHolder.getLocale())
            + "_"
            + TimeUtil.formatLocalDateTime(TimeUtil.localDateTimeNow(), PATTERN_LOCAL_DATE_TIME_DOWNLOAD);
    String sheetName =
        this.messageSource.getMessage(names.get(1), null, LocaleContextHolder.getLocale());
    downloadXlsx(response, fileName, sheetName, headers, data);
  }

  @Override
  public void downloadXlsx(
      HttpServletResponse response,
      String fileName,
      String sheetName,
      List<Field> headers,
      List<?> data) {
    try {
      XSSFWorkbook workbook = generateXlsx(sheetName, headers, data);
      String headerValue = ";filename=" + fileName + ".xlsx";
      response.setHeader(HEADER_KEY, "attachment" + headerValue);
      response.setContentType(RESPONSE_CONTENT_TYPE_EXCEL + headerValue);

      ServletOutputStream output = response.getOutputStream();
      workbook.write(output);
      workbook.close();
    } catch (IOException e) {
      throw new BadRequestException("error.msg.excel.descarga", e.getMessage());
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

  @Override
  public String[] getColumnsFromXlsx(File file) {
    try (FileInputStream fis = new FileInputStream(file)) {
      return getColumnsFromInputStream(fis);
    } catch (IOException e) {
      throw new BadRequestException("error.msg.excel.obtenerColumnas", e.getMessage());
    }
  }

  @Override
  public String[] getColumnsFromXlsx(MultipartFile file) {
    try (InputStream inputStream = file.getInputStream()) {
      return getColumnsFromInputStream(inputStream);
    } catch (IOException e) {
      throw new BadRequestException("error.msg.excel.obtenerColumnas", e.getMessage());
    }
  }

  @Override
  public boolean isValidXLSX(MultipartFile file) {
    try {
      Workbook workbook = WorkbookFactory.create(file.getInputStream());
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
              headers.get(columnIndex).getAnnotation(ExcelColumn.class).label(),
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
        Method method = ReflectionUtil.getGetterMethod(rowData.getClass(), field.getName());
        Object value = method.invoke(rowData);
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

      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        String logMessage =
            "\nError al intentar acceder a uno de los campos de la clase en columna "
                + columnIndex
                + ", fila "
                + row.getRowNum();
        log.error(logMessage, e);
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
