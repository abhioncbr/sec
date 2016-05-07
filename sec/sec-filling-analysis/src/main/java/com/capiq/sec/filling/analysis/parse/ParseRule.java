package com.capiq.sec.filling.analysis.parse;

import java.util.LinkedList;
import java.util.List;

public class ParseRule {
	
	private final List<Rule> ruleList = new LinkedList<Rule>();
	
	public List<Rule> getRuleList() {
		return ruleList;
	}

	public void setRule(Double weight, String ruleString, Integer group, Boolean trim, Boolean caseInsensitive){
		Rule rule = new Rule(weight, ruleString, group, trim, caseInsensitive);
		this.ruleList.add(rule);
	}


	public class Rule {
		private String rule;
		private Double weight;
		private Integer group;
		private Boolean trim;
		private Boolean caseInsensitive;
		
		public Rule(Double weight, String rule, Integer group, Boolean trim, Boolean caseInsensitive){
			this.rule = rule;
			this.weight = weight;
			this.group = group;
			this.trim = trim;
			this.caseInsensitive = caseInsensitive;
		}

		public String getRule() {
			return rule;
		}

		public Double getWeight() {
			return weight;
		}

		public Integer getGroup() {
			return group;
		}

		public Boolean getTrim() {
			return trim;
		}

		public Boolean getCaseInsensitive() {
			return caseInsensitive;
		}
	}
}
