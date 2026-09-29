package com.temenos.dbx.product.achservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHFileRecordDTO;

public interface ACHFileRecordBusinessDelegate extends BusinessDelegate{
	
	/**
	 * Retrieves the ACH file records by passing the ACH file Id
	 * @param String achFileId
	 * @return List<ACHFileRecordDTO> List of ACH File Records
	 */
	public List<ACHFileRecordDTO> fetchACHFileRecords(String achFileId);
	
}
