package com.capiq.sec.filling.analysis.parse.preprocess;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContext;

public interface IPreProcess {
	
	public boolean condition(ParseDocumentContext context);
	
	public void process(ParseDocumentContext context) throws SymantecAnalysisGeneralException;
	public void execute(ParseDocumentContext context) throws SymantecAnalysisGeneralException;

}
