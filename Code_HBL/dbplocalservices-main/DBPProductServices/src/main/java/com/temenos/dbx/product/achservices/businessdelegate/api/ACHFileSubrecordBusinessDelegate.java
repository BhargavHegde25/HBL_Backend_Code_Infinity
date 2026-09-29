package com.temenos.dbx.product.achservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHFileSubrecordDTO;

public interface ACHFileSubrecordBusinessDelegate extends BusinessDelegate {

	/**
	 * Retrieves the ACH file sub records by passing ACH file record Id
	 * @param String achFileRecordId
	 * @return List<ACHFileSubrecordDTO> List of ACH file subrecords
	 */
	public List<ACHFileSubrecordDTO> fetchACHFileSubrecords(String achFileRecordId);
	
}
