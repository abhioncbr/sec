package com.capiq.sec.filling.analysis.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.ResourceAware;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.Resource;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;

public class ExcelReader<E> implements ItemReader<E>, ResourceAware {
	
	private Resource resource;
	
	private Sheet sheet;
	private Workbook workbook;
	private InputStream inputStream;
	
	private ExcelDataMapper<E> mapper;
	private Class<E> type;
	public ExcelReader(Class<E> type, ExcelDataMapper<E> mapper) {
		this.type = type;
	    this.mapper = mapper;
	}
	 
	public void setResource(Resource resource) {
		this.resource = resource;
	}	
	
	public void closeExcelFile() throws SymantecAnalysisGeneralException {
		try {
			inputStream.close();
		} catch (IOException e) {
			throw new SymantecAnalysisGeneralException(e);
		} finally {
			if(inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new SymantecAnalysisGeneralException(e);
				}
		}
		
	}

	public void openExcelFile() throws SymantecAnalysisGeneralException {
        try {
			inputStream = resource.getInputStream();
			workbook = new XSSFWorkbook(inputStream);
		} catch (IOException e) {
			throw new SymantecAnalysisGeneralException(e);
		} finally {
			if(inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new SymantecAnalysisGeneralException(e);
				}
		}
	}
	
	public E read(int sheetNumber) throws SymantecAnalysisGeneralException{
		try {
			sheet = workbook.getSheetAt(sheetNumber);
			return read();
		} catch (UnexpectedInputException e) {
			throw new SymantecAnalysisGeneralException(e);
		} catch (ParseException e) {
			throw new SymantecAnalysisGeneralException(e);
		} catch (NonTransientResourceException e) {
			throw new SymantecAnalysisGeneralException(e);
		} catch (Exception e) {
			throw new SymantecAnalysisGeneralException(e);
		}
	}

	@Override
	public E read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
        Iterator<Row> iterator = sheet.iterator();
        
        E object= type.newInstance();
        
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            
            LinkedList<Object> values = new LinkedList<Object>();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                values.add(readCellData(cell));
            }
            mapper.setObjectData(values, object);
        }
         
		return object;
	}
	
	private Object readCellData(Cell cell) {
		Object output = null;

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			output = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			output = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			output = cell.getNumericCellValue();
			break;
		}
		return output;
	}
}