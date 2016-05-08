package com.sec.filing.analysis.exception;

public class SymantecAnalysisGeneralException extends Exception {

	private static final long serialVersionUID = -8916590134212849752L;

	public SymantecAnalysisGeneralException(final Throwable throwable) {
        super(throwable);
    }
	
	public SymantecAnalysisGeneralException(final String exceptionMessage) {
        super(exceptionMessage);
    }
	
	public SymantecAnalysisGeneralException(final String exceptionMessage, final Throwable throwable) {
        super(exceptionMessage, throwable);
    }
}
