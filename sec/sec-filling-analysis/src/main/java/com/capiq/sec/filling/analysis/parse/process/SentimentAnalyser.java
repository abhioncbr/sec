package com.capiq.sec.filling.analysis.parse.process;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.input.ExcelReader;
import com.capiq.sec.filling.analysis.input.FileInput;
import com.capiq.sec.filling.analysis.parse.FinancialDictionary;
import com.capiq.sec.filling.analysis.parse.FinancialDictionary.FinancailKeyword;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContext;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContextEnum;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.LineContent;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.SecContent;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.SecDocument;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.SecPage;
import com.capiq.sec.filling.common.util.SpringAppContext;

public class SentimentAnalyser extends AbstractProcess {
	//private final static String NEW_LINE = System.getProperty("line.separator");

	private final ExcelReader<FinancialDictionary> FINANCIAL_DICTIONARY = SpringAppContext.getBean("financialDictionaryReader");
	private final FinancialDictionary SENTIMENT_ANALYSER_KEWORDS;

	List<String> sentimentText = new ArrayList<String>();
	
	public SentimentAnalyser() throws SymantecAnalysisGeneralException {
		SENTIMENT_ANALYSER_KEWORDS = FileInput.readExcelFile(1,	FINANCIAL_DICTIONARY);
	}

	@Override
	public boolean condition(ParseDocumentContext context) {
		List<FinancailKeyword> financialWords = SENTIMENT_ANALYSER_KEWORDS.getFinancialDictionaryList();
		for(FinancailKeyword keywords: financialWords){
			sentimentText.add(keywords.getWord());
		}
		
		if(sentimentText.size() > 0)
			return true;
		
		return false;
	}

	@Override
	public void process(ParseDocumentContext context) {
		SecDocument document = context.getContextValue(ParseDocumentContextEnum.CLEAN_SEC_DOCUMENT_OBJECT, SecDocument.class);

		for(SecPage documentPage : document.getPageContent()){
			
			//TODO: imply logic for fetching document as per data structure.
			LinkedList<SecContent> secContentList = documentPage.getPageContent();
			for(SecContent secContent : secContentList){
				for(@SuppressWarnings("unused") LineContent content: secContent.getPageContent()){
					//analyze(content);
				}	
			}
			

		}
	}
	
/*	private void analyze(LineContent content){
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		props.setProperty("ner.model", "edu/stanford/nlp/models/sentiment/sentiment.binary.ser.gz");
		
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		String[] temp = sentimentText.toArray(new String[sentimentText.size()]);
		
		int mainSentiment = 0;
		int longest = 0;
		Annotation annotation = pipeline.process(content.getStringContent());
		for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
			int score = RNNCoreAnnotations.getPredictedClass(tree);
			
			String partText = sentence.toString();
            if (partText.length() > longest) {
                mainSentiment = score;
                longest = partText.length();
            }
			
            if (!(mainSentiment == 2 || mainSentiment > 4 || mainSentiment < 0)) {
            	String sentimentAnalyzed = temp[score];
    			content.setContentProperty(sentimentAnalyzed, true);
    			
    			System.out.println("###");
    			System.out.println(sentimentText.toArray(new String[sentimentText.size()])[score]);
            } 
		}
		
	}*/
}
