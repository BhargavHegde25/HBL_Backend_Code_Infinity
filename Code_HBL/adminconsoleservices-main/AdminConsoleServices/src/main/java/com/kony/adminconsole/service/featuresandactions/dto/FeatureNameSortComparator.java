package com.kony.adminconsole.service.featuresandactions.dto;

import java.util.Comparator;

public class FeatureNameSortComparator implements Comparator<FeatureActionsViewDTO>{

	@Override
	public int compare(FeatureActionsViewDTO o1, FeatureActionsViewDTO o2) {
		
		return o1.getFeatureName().compareTo(o2.getFeatureName());
	}

}
