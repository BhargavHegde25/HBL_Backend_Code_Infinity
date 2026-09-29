package com.bct.custom.backenddeligate.api;

import java.util.ArrayList;
import java.util.Map;

import com.bct.custom.dto.CIPSMerchantCategoriesDTO;
import com.bct.custom.dto.MerchantCategoriesDTO;
import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface MerchantCategoriesBackendDeligate extends BackendDelegate {
	public Result updateMerchantCategories(MerchantCategoriesDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException;
	public Result createMerchantCategories(MerchantCategoriesDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException;
	Result createCIPSMerchantCategories(CIPSMerchantCategoriesDTO dto, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException;
	public Result createMerchantCategories(ArrayList<MerchantCategoriesDTO> categoriesArray,
			DataControllerRequest dcRequest, Map<String, Object> otherInput) throws ApplicationException;
	public Boolean checkCodeAvailableInDatabase(String code, String tableName, DataControllerRequest dcRequest);
}
