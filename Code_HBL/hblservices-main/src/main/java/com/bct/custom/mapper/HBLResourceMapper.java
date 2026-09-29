package com.bct.custom.mapper;

import java.util.HashMap;
import java.util.Map;

import com.bct.custom.resource.api.GetAllMerchantOperationsResource;
import com.bct.custom.resource.api.ManualMerchantsResource;
import com.bct.custom.resource.api.BillPaymentHistoryResource;
import com.bct.custom.resource.api.BranchDetailsResource;
import com.bct.custom.resource.api.CIPSTransactionHistoryResource;
import com.bct.custom.resource.api.FavoriteMerchantManageResource;
import com.bct.custom.resource.api.MerchantCategoriesCRUDResource;
import com.bct.custom.resource.api.MerchantChargesResource;
import com.bct.custom.resource.api.MerchantDetailsResource;
import com.bct.custom.resource.api.MerchantFieldsResource;
import com.bct.custom.resource.api.MerchantSubCategoriesResource;
import com.bct.custom.resource.api.PaymentAggregatorResource;
import com.bct.custom.resource.impl.GetAllMerchantOperationsResourceImpl;
import com.bct.custom.resource.impl.ManualMerchantsResourceImpl;
import com.bct.custom.resource.impl.BillPaymentHistoryResourceImpl;
import com.bct.custom.resource.impl.BranchDetailsResourceImpl;
import com.bct.custom.resource.impl.CIPSTransactionHistoryResourceImpl;
import com.bct.custom.resource.impl.FavoriteMerchantManageResourceImpl;
import com.bct.custom.resource.impl.MerchantCategoriesCRUDResourceImpl;
import com.bct.custom.resource.impl.MerchantChargesResourceImpl;
import com.bct.custom.resource.impl.MerchantDetailsResourceImpl;
import com.bct.custom.resource.impl.MerchantFieldsResourceImpl;
import com.bct.custom.resource.impl.MerchantSubCategoriesResourceImpl;
import com.bct.custom.resource.impl.PaymentAggregatorResourceImpl;
import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;

public class HBLResourceMapper implements DBPAPIMapper<Resource>{

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		map.put(GetAllMerchantOperationsResource.class, GetAllMerchantOperationsResourceImpl.class);
		map.put(PaymentAggregatorResource.class, PaymentAggregatorResourceImpl.class);
		map.put(MerchantDetailsResource.class, MerchantDetailsResourceImpl.class);
		map.put(MerchantFieldsResource.class, MerchantFieldsResourceImpl.class);
		map.put(MerchantCategoriesCRUDResource.class, MerchantCategoriesCRUDResourceImpl.class);
		map.put(BillPaymentHistoryResource.class, BillPaymentHistoryResourceImpl.class);
		map.put(BranchDetailsResource.class, BranchDetailsResourceImpl.class);
		map.put(ManualMerchantsResource.class, ManualMerchantsResourceImpl.class);
		map.put(MerchantChargesResource.class, MerchantChargesResourceImpl.class);
		map.put(MerchantSubCategoriesResource.class, MerchantSubCategoriesResourceImpl.class);
		map.put(FavoriteMerchantManageResource.class, FavoriteMerchantManageResourceImpl.class);
		map.put(CIPSTransactionHistoryResource.class, CIPSTransactionHistoryResourceImpl.class);
		return map;
	}
}
