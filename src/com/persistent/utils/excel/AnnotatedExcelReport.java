package com.persistent.utils.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Reference: http://persistentdesigns.com/wp/?p=512
 * 
 */
public class AnnotatedExcelReport {
	// static Logger logger =
	// Logger.getLogger(AnnotatedExcelReport.class.getName());
	private Workbook workbook = null;
	private String workbookName = "workbook.xls";
	private Map<String, String> fieldLabelMap = new HashMap<String, String>();
	private List<String> orderLabels = new ArrayList<String>();
	private CellStyle columnHeaderCellStyle = null;

	public AnnotatedExcelReport() {
		initialize();
	}

	private void initialize() {
		setWorkbook(new HSSFWorkbook());
		setColumnHeaderCellStyle(createColumnHeaderCellStyle());
	}

	private CellStyle createColumnHeaderCellStyle() {
		CellStyle cellStyle = getWorkbook().createCellStyle();
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillBackgroundColor(new HSSFColor.GREY_25_PERCENT()
				.getIndex());
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}

	public void closeWorksheet() {
		// FileOutputStream fileOut;
		// try {
		// fileOut = new FileOutputStream(getWorkbookName());
		// getWorkbook().write(fileOut);
		// fileOut.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		System.out.println("worksheet closed (do nothing though)");
	}

	private Sheet getSheetWithName(String name) {
		Sheet sheet = null;
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			if (name.compareTo(workbook.getSheetName(i)) == 0) {
				sheet = workbook.getSheetAt(i);
				break;
			}
		}
		return sheet;
	}

	public byte[] retrieveWorksheet() {
		byte[] workbookBytes = null;
		try {
			ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
			Workbook wb = getWorkbook();
			wb.write(fileOut);
			workbookBytes = fileOut.toByteArray();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbookBytes;
	}

	public ByteArrayOutputStream getWorksheetOutputStream() {
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			Workbook wb = getWorkbook();
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileOut;
	}

	private void initializeForRead() throws Exception {
		System.out.println("Open file");
		// InputStream inp = new FileInputStream(getWorkbookName());
		InputStream inp = new ByteArrayInputStream(retrieveWorksheet());
		System.out.println("create wb");
//		workbook = WorkbookFactory.create(inp);
	}

	private <T> void processAnnotations(T object) {
		Class<?> clazz = object.getClass();
		ExcelReport reportAnnotation = (ExcelReport) clazz
				.getAnnotation(ExcelReport.class);
		System.out.println("Report Name  : " + reportAnnotation.reportName());
		String reportName = reportAnnotation.reportName();
		if ((reportName == null) || (reportName.trim().length() < 1)) {
			// should never get here.
			System.err.println("Invalid Worksheet Name");
		}
		setWorkbookName(reportAnnotation.reportName());
		for (Method method : clazz.getMethods()) {
			ExcelColumn excelColumn = method.getAnnotation(ExcelColumn.class);
			if ((excelColumn != null) && !excelColumn.ignore()) {
				getFieldLabelMap().put(excelColumn.label(), method.getName());
				getOrderLabels().add(excelColumn.label());
				System.out.println("Annotation on method: " + method.getName());
				System.out.println("Ignore: " + excelColumn.ignore()
						+ "  label: " + excelColumn.label());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> readData(String classname) throws Exception {
		Class clazz = Class.forName(classname);
		processAnnotations(clazz.newInstance());
		initializeForRead();
		Sheet sheet = getSheetWithName(classname);
		List<T> result = new ArrayList<T>();
		if (sheet != null) {
			Row row;
			for (int rowCount = 1; rowCount < 4; rowCount++) {
				T one = (T) clazz.newInstance();
				row = sheet.getRow(rowCount);
				int colCount = 0;
				result.add(one);
				for (Cell cell : row) {
					if (colCount >= getOrderLabels().size()) {
						break;
					}
					int type = cell.getCellType();
					String labelName = getOrderLabels().get(colCount++);
					String getter = getFieldLabelMap().get(labelName);
					String fieldName = getter.substring(3);
					fieldName = decapitalize(fieldName);
					System.out.println("Getting field: " + fieldName);
					Method method = constructMethod(clazz, fieldName);

					if (type == 1) {
						String value = cell.getStringCellValue();
						Object[] values = new Object[1];
						values[0] = value;
						method.invoke(one, values);
					} else if (type == 0) {
						Double num = cell.getNumericCellValue();
						Class<?> returnType = getGetterReturnClass(clazz,
								fieldName);
						if (returnType == Integer.class) {
							method.invoke(one, num.intValue());
						} else if (returnType == Double.class) {
							method.invoke(one, num);
						} else if (returnType == Float.class) {
							method.invoke(one, num.floatValue());
						}

					} else if (type == 3) {
						double num = cell.getNumericCellValue();
						Object[] values = new Object[1];
						values[0] = num;
						method.invoke(one, values);
					}
				}
			}
		}
		System.out.println("The result set contains: " + result.size()
				+ " items.");
		return result;
	}

	private Class<?> getGetterReturnClass(Class<?> clazz, String fieldName) {
		String methodName = "get" + capitalize(fieldName);
		Class<?> returnType = null;
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName)) {
				returnType = method.getReturnType();
				break;
			}
		}
		return returnType;
	}

	@SuppressWarnings("unchecked")
	private Method constructMethod(Class clazz, String fieldName)
			throws SecurityException, NoSuchMethodException {
		Class<?> fieldClass = getGetterReturnClass(clazz, fieldName);
		return clazz.getMethod("set" + capitalize(fieldName), fieldClass);
	}

	public <T> void writeReportToExcel(List<T> data) throws Exception {
		processAnnotations(data.get(0));
		Sheet sheet = getWorkbook().createSheet(
				data.get(0).getClass().getName());
		int rowCount = 0;
		int columnCount = 0;

		Row row = sheet.createRow(rowCount++);
		for (String labelName : getOrderLabels()) {
			Cell cel = row.createCell(columnCount++);
			cel.setCellValue(labelName);
			cel.setCellStyle(getColumnHeaderCellStyle());
		}
		Class<? extends Object> classz = data.get(0).getClass();
		for (T t : data) {
			row = sheet.createRow(rowCount++);

			columnCount = 0;

			for (String label : getOrderLabels()) {
				String methodName = getFieldLabelMap().get(label);
				Cell cel = row.createCell(columnCount);
				Method method = classz.getMethod(methodName);
				Object value = method.invoke(t, (Object[]) null);
				if (value != null) {
					if (value instanceof String) {
						cel.setCellValue((String) value);
					} else if (value instanceof Long) {
						cel.setCellValue((Long) value);
					} else if (value instanceof Integer) {
						cel.setCellValue((Integer) value);
					} else if (value instanceof Double) {
						cel.setCellValue((Double) value);
					}
				}
				columnCount++;
			}
		}
	}

	public Map<String, String> getFieldLabelMap() {
		return fieldLabelMap;
	}

	public void setFieldLabelMap(Map<String, String> fieldLabelMap) {
		this.fieldLabelMap = fieldLabelMap;
	}

	public List<String> getOrderLabels() {
		return orderLabels;
	}

	public void setOrderLabels(List<String> orderLabels) {
		this.orderLabels = orderLabels;
	}

	public String capitalize(String string) {
		String capital = string.substring(0, 1).toUpperCase();
		return capital + string.substring(1);
	}

	public String decapitalize(String string) {
		String capital = string.substring(0, 1).toLowerCase();
		return capital + string.substring(1);
	}

	public String getWorkbookName() {
		return workbookName;
	}

	public void setWorkbookName(String workbookName) {
		this.workbookName = workbookName;
	}

	void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	Workbook getWorkbook() {
		return workbook;
	}

	public CellStyle getColumnHeaderCellStyle() {
		return columnHeaderCellStyle;
	}

	public void setColumnHeaderCellStyle(CellStyle columnHeaderCellStyle) {
		this.columnHeaderCellStyle = columnHeaderCellStyle;
	}

}
