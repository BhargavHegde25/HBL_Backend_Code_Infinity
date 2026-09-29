package com.kony.adminconsole.campaign.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.kony.adminconsole.dto.campaign.DataContextRequestDTO;
import com.kony.adminconsole.exception.ApplicationException;

public interface DataContextBusinessDelegate extends BusinessDelegate {

	String getActiveCustomersForSegment(DataContextRequestDTO dcReqDTO) throws ApplicationException;
	
}
