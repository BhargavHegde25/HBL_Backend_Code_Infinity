package com.bct.custom.mapper;

import java.util.HashMap;
import java.util.Map;

import com.bct.custom.backenddeligate.api.BillPaymentHistoryBackendDeligate;
import com.bct.custom.backenddeligate.impl.BillPaymentHistoryBackendDelegateImpl;
import com.bct.custom.businessdeligate.api.BillPaymentHistoryBusinessDelegate;
import com.bct.custom.businessdeligate.api.BranchDetailsBusinessDelegate;
import com.bct.custom.businessdeligate.api.CIPSPaymentHistoryBusinessDelegate;
import com.bct.custom.businessdeligate.api.FavoriteMerchantBusinessDelegate;
import com.bct.custom.businessdeligate.api.GetAllMerchantOperationsBusinessDeligate;
import com.bct.custom.businessdeligate.api.ManualMerchantsBusinessDelagate;
import com.bct.custom.businessdeligate.api.MerchantCategoriesBusinessDeligate;
import com.bct.custom.businessdeligate.api.MerchantChargesBusinessDelegate;
import com.bct.custom.businessdeligate.api.MerchantDetailsBusinessDeligate;
import com.bct.custom.businessdeligate.api.MerchantFieldsBusinessDeligate;
import com.bct.custom.businessdeligate.api.MerchantSubCategoriesBusinessDelegate;
import com.bct.custom.businessdeligate.api.PaymentAggregatorBusinessDeligate;
import com.bct.custom.businessdeligate.impl.BillPaymentHistoryBusinessDeligateImpl;
import com.bct.custom.businessdeligate.impl.BranchDetailsBusinessDeligateImpl;
import com.bct.custom.businessdeligate.impl.CIPSPaymentHistoryBusinessDelegateImpl;
import com.bct.custom.businessdeligate.impl.FavoriteMerchantBusinessDelegateImpl;
import com.bct.custom.businessdeligate.impl.GetAllMerchantOperationsBusinessDeligateImpl;
import com.bct.custom.businessdeligate.impl.ManualMerchantsBusinessDelagateImpl;
import com.bct.custom.businessdeligate.impl.MerchantCategoriesBusinessDeligateImpl;
import com.bct.custom.businessdeligate.impl.MerchantChargesBusinessDelegateImpl;
import com.bct.custom.businessdeligate.impl.MerchantDetailsBusinessDeligateImpl;
import com.bct.custom.businessdeligate.impl.MerchantFieldsBusinessDeligateImpl;
import com.bct.custom.businessdeligate.impl.MerchantSubCategoriesBusinessDelegateImpl;
import com.bct.custom.businessdeligate.impl.PaymentAggregatorBusinessDeligateImpl;
import com.bct.custom.resource.api.FavoriteMerchantManageResource;
import com.bct.custom.resource.impl.FavoriteMerchantManageResourceImpl;
import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;

public class HBLCustomBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		map.put(GetAllMerchantOperationsBusinessDeligate.class, GetAllMerchantOperationsBusinessDeligateImpl.class);
		map.put(PaymentAggregatorBusinessDeligate.class, PaymentAggregatorBusinessDeligateImpl.class);
		map.put(MerchantDetailsBusinessDeligate.class, MerchantDetailsBusinessDeligateImpl.class);
		map.put(MerchantFieldsBusinessDeligate.class, MerchantFieldsBusinessDeligateImpl.class);
		map.put(MerchantCategoriesBusinessDeligate.class, MerchantCategoriesBusinessDeligateImpl.class);
		map.put(BillPaymentHistoryBusinessDelegate.class, BillPaymentHistoryBusinessDeligateImpl.class);
		map.put(BranchDetailsBusinessDelegate.class, BranchDetailsBusinessDeligateImpl.class);
		map.put(ManualMerchantsBusinessDelagate.class, ManualMerchantsBusinessDelagateImpl.class);
		map.put(MerchantChargesBusinessDelegate.class, MerchantChargesBusinessDelegateImpl.class);
		map.put(MerchantSubCategoriesBusinessDelegate.class, MerchantSubCategoriesBusinessDelegateImpl.class);
		map.put(FavoriteMerchantBusinessDelegate.class, FavoriteMerchantBusinessDelegateImpl.class);
		map.put(CIPSPaymentHistoryBusinessDelegate.class, CIPSPaymentHistoryBusinessDelegateImpl.class);
		return map;
	}
}