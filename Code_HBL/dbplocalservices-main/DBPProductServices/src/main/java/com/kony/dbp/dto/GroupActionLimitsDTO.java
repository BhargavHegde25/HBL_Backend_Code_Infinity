package com.kony.dbp.dto;

import java.util.HashMap;
import java.util.Map;

public class GroupActionLimitsDTO {
	
	private Map<String, Double> retailLimits;
	private Map<String, Double> businessLimits;
	
	public GroupActionLimitsDTO() {
		retailLimits = new HashMap<>();
		businessLimits = new HashMap<>();
	}

	public Map<String, Double> getRetailLimits() {
		return retailLimits;
	}

	public void setRetailLimits(Map<String, Double> retailLimits) {
		this.retailLimits = retailLimits;
	}

	public Map<String, Double> getBusinessLimits() {
		return businessLimits;
	}

	public void setBusinessLimits(Map<String, Double> businessLimits) {
		this.businessLimits = businessLimits;
	}
	
}
