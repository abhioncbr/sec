package com.capiq.sec.filling.analysis.corpwatch.rest.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class JsonResponseFilings {
    public final Meta meta;
    public final Result result;

    @JsonCreator
    public JsonResponseFilings(@JsonProperty("meta") Meta meta, @JsonProperty("result") Result result){
        this.meta = meta;
        this.result = result;
    }

    public static final class Result {
    	public List<Filing> filings = new ArrayList<Filing>();

        @JsonCreator
        public Result(@JsonProperty("filings") List<Filing> filings){
            this.filings = filings;
        }
        
        @Override
    	public String toString() {
        	StringBuilder builder = new StringBuilder();
        	for(Filing filing: filings){
        		builder.append(" [ " + filing.toString() + " ] ");
        	}
    		return builder.toString();
    	}  
    }

	@Override
	public String toString() {
		return "CikModel [meta=" + meta + ", result=" + result + "]";
	} 
}