package com.sec.filing.analysis.parse.preprocess;

import com.sec.filing.analysis.exception.SymantecAnalysisGeneralException;
import com.sec.filing.analysis.parse.document.ParseDocumentContext;

public interface IPreProcess {
	
	public boolean condition(ParseDocumentContext context);
	
	public void process(ParseDocumentContext context) throws SymantecAnalysisGeneralException;
	public void execute(ParseDocumentContext context) throws SymantecAnalysisGeneralException;

}
