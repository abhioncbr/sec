package com.sec.filing.analysis.test.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.sec.filing.analysis.common.util.SpringAppContext;
import com.sec.filing.analysis.exception.SymantecAnalysisGeneralException;
import com.sec.filing.analysis.parse.document.ParseDocument;
import com.sec.filing.analysis.parse.document.ParseDocumentContext;
import com.sec.filing.analysis.parse.document.ParseDocumentContextEnum;
import com.sec.filing.analysis.parse.document.dataStructure.SecDocument;

public class ParseDocumentTest {

	private ParseDocument documentParser;
	
	@Before
	public void setUp() throws SymantecAnalysisGeneralException{
		SpringAppContext.loadApplicationContext();
		documentParser = new ParseDocument();
	}
	
	@Test
	public void getForm10KFilingTest() throws SymantecAnalysisGeneralException{
		String fileName = "0000012927_BA_2_8_2010.txt";//"0000072971_WFC_2_25_2015.txt";//0001193125-06-040952.txt";
		ParseDocumentContext context = new ParseDocumentContext();
		
		documentParser.parse(fileName, context);
		SecDocument document = context.getContextValue(ParseDocumentContextEnum.CLEAN_SEC_DOCUMENT_OBJECT, SecDocument.class);
		
		assertNotNull(document);
		assertTrue(document.getPageContent().size() > 0);
		//assertEquals(document.getPageContent().size(),294);
	}
	
	//@Test
	public void getForm10KFilingTest1() throws SymantecAnalysisGeneralException{
		String fileName =  "0001193125-15-072748.txt";
		ParseDocumentContext context = new ParseDocumentContext();
		
		documentParser.parse(fileName, context);
		SecDocument document = context.getContextValue(ParseDocumentContextEnum.CLEAN_SEC_DOCUMENT_OBJECT, SecDocument.class);
		
		assertNotNull(document);
		assertTrue(document.getPageContent().size() > 0);
		assertEquals(document.getPageContent().size(),404);
	}
	
	//@Test
	public void getForm10KFilingTest2() throws SymantecAnalysisGeneralException{
		String fileName = "0000950123-11-018541.txt";
		ParseDocumentContext context = new ParseDocumentContext();
		
		documentParser.parse(fileName, context);
		SecDocument document = context.getContextValue(ParseDocumentContextEnum.CLEAN_SEC_DOCUMENT_OBJECT, SecDocument.class);
		
		assertNotNull(document);
		assertTrue(document.getPageContent().size() > 0);
		assertEquals(document.getPageContent().size(),282);
	}
	
}
