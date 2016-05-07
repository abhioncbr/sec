package com.capiq.sec.filling.analysis.parse.preprocess;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.parse.ParseRule.Rule;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContext;

public abstract class AbstractPreProcess implements IPreProcess {

	@Override
	public void execute(ParseDocumentContext context) throws SymantecAnalysisGeneralException {
		if(condition(context))
			process(context);
		return;
	}
	
	public class ConditionRule {
		public LinkedList<Rule> ruleList = new LinkedList<Rule>();
		Map<String, Object> ruleProperties = new HashMap<String,Object>();
		Double hitCount = new Double("0.0");

		public ConditionRule(Rule rule) {
			addRule(rule);
		}
		
		public void addRule(Rule rule){
			ruleList.add(rule);
		}
		
		public void addRuleProperty(String propertyName, Object value){
			ruleProperties.put(propertyName, value);
		}

		public void incrementHitCount(Double weight) {
			hitCount += weight;
		}
	}
	
}
