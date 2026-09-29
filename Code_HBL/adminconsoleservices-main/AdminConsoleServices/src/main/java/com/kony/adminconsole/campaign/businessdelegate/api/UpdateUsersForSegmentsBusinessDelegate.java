package com.kony.adminconsole.campaign.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.kony.adminconsole.dto.campaign.Segment;
import com.kony.adminconsole.exception.ApplicationException;

public interface UpdateUsersForSegmentsBusinessDelegate extends BusinessDelegate {

	boolean updateActiveUsersForAllSegments(String[] additionalParams) throws ApplicationException;
	
	boolean getAndupdateActiveUsersForSegment(Segment segment,String[] additionalParams) throws ApplicationException;	
	
}

