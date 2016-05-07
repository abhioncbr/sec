package com.capiq.sec.filling.analysis.output;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemWriter;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.model.DomainData;

public class ExcelWriter<T extends DomainData> implements ItemWriter<DomainData> {

	private String fileName;
	private String outputFilename;
	private Workbook workbook;
	
	private int currRow = 0;
	private int sheetCount = 0;
	
	private List<String> sheetHeaders;
	
	public void addSheet(String sheetTitle, List<String> sheetHeaders){
		currRow=0;
		Sheet sheet = workbook.createSheet(sheetTitle);
		sheet.createFreezePane(0, 1, 0, 1);
		sheet.setDefaultColumnWidth(20);
		this.sheetHeaders = sheetHeaders;
		addTitleToSheet(sheet);
		initDataStyle();
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void closeExcelFile() throws SymantecAnalysisGeneralException {
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(outputFilename);
			workbook.write(fos);
			fos.close();
		} catch (IOException e) {
			throw new SymantecAnalysisGeneralException(e);
		} finally {
			try {
				if(fos != null)
					fos.close();
			} catch (IOException e) {
				throw new SymantecAnalysisGeneralException(e);
			}
		}
		
	}

	public void openExcelFile() {
		outputFilename = fileName + ".xlsx";
		workbook = new XSSFWorkbook();
	}
	
	@Override
	public void write(List<? extends DomainData> items) throws Exception {
		Sheet sheet = workbook.getSheetAt(sheetCount++);
		
		//writing header
		addHeaders(sheet);
		
		//writing content
		for (DomainData data : items) {
			List<String> rowData = data.getDomainData();
			Row row = sheet.createRow(currRow++);
			int columnCount = 0;
			for (String columnData : rowData) {
					createStringCell(row, columnData, columnCount++);
				}
			}
	}

	private void createStringCell(Row row, String val, int col) {
		Cell cell = row.createCell(col);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(val);
	}
	
	private void addHeaders(Sheet sheet) {
		Workbook wb = sheet.getWorkbook();

		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();

		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Row row = sheet.createRow(currRow++);
		int col = 0;
		
		for (String columnHeader : sheetHeaders) {
			Cell cell = row.createCell(col);
			cell.setCellValue(columnHeader);
			cell.setCellStyle(style);
			col++;
		}
	}

	private void addTitleToSheet(Sheet sheet) {

		Workbook wb = sheet.getWorkbook();

		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();

		font.setFontHeightInPoints((short) 14);
		font.setFontName("Arial");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);
	}
	
	private void initDataStyle() {
		CellStyle dataCellStyle = workbook.createCellStyle();
		Font font = workbook.createFont();

		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		dataCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		dataCellStyle.setFont(font);
	}
}