package com.temenos.dbx.transaction.businessdelegate.api;
import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.transaction.dto.SwiftCodeDTO;

public interface SwiftCodeBusinessDelegate extends BusinessDelegate {

	public List<SwiftCodeDTO> getSwiftBICcode(String bankName, String city, String country, String branchName);
	public List<SwiftCodeDTO> getSwiftBICcode(String iban);
}
