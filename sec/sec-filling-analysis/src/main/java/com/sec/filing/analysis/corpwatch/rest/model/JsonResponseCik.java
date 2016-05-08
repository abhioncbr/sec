package com.sec.filing.analysis.corpwatch.rest.model;

import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class JsonResponseCik {
    public final Meta meta;
    public final Result result;

    @JsonCreator
    public JsonResponseCik(@JsonProperty("meta") Meta meta, @JsonProperty("result") Result result){
        this.meta = meta;
        this.result = result;
    }

    public static final class Result {
        public final Map<String,CompanyInfo> companies;

        @JsonCreator
        public Result(@JsonProperty("companies") Map<String,CompanyInfo> companies){
            this.companies = companies;
        }
        
        @Override
    	public String toString() {
        	StringBuilder builder = new StringBuilder();
        	for(Entry<String,CompanyInfo> entry: companies.entrySet()){
        		builder.append(entry.getKey() + " [ " + entry.getValue() + " ] ");
        	}
    		return builder.toString();
    	}  
    }

	@Override
	public String toString() {
		return "CikModel [meta=" + meta + ", result=" + result + "]";
	} 
}