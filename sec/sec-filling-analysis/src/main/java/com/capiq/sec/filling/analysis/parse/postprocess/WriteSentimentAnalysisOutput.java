package com.capiq.sec.filling.analysis.parse.postprocess;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.output.FileOutput;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContext;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContextEnum;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.LineContent;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.SecContent;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.SecDocument;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.SecPage;
import com.capiq.sec.filling.common.util.PropertiesUtil;

public class WriteSentimentAnalysisOutput extends AbstractPostProcess {

	@Override
	public boolean condition(ParseDocumentContext context) {
		return true;
	}

	@Override
	public void process(ParseDocumentContext context) {
		SecDocument document = context.getContextValue(
				ParseDocumentContextEnum.CLEAN_SEC_DOCUMENT_OBJECT, SecDocument.class);

		LinkedList<StringBuilder> contentToWrite = new LinkedList<StringBuilder>();
		for (SecPage documentPage : document.getPageContent()) {

			// TODO: imply logic for fetching document as per data structure.
			LinkedList<SecContent> secContentList = documentPage
					.getPageContent();
			for (SecContent secContent : secContentList) {
				for (LineContent content : secContent.getPageContent()) {
					if (content.getContentPropertyMap().size() > 0) {
						StringBuilder temp = new StringBuilder();
						temp.append(content.getDocumentLineNumber() + ",");
						Map<Object, Object> property = content.getContentPropertyMap();

						for (Entry<Object, Object> entry : property.entrySet()) {
							temp.append(entry.getKey() + " ");
						}

						contentToWrite.add(temp);
					}
				}
			}
		}

		String filepath = PropertiesUtil
				.getProperty("file.sec.form-10k.sentiment.folder")
				+ "/"
				+ context.getContextValue(ParseDocumentContextEnum.FILE_NAME,
						String.class);

		try {
			FileOutput.writeDataInfile(filepath, contentToWrite);
		} catch (SymantecAnalysisGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
