package com.bct.custom.mapper;

import java.util.HashMap;
import java.util.Map;

import com.bct.custom.backenddeligate.api.BillPaymentHistoryBackendDeligate;
import com.bct.custom.backenddeligate.api.BranchdetailsBackendDelegate;
import com.bct.custom.backenddeligate.api.CIPSPaymentHistoryBackendDeligate;
import com.bct.custom.backenddeligate.api.GetAllMerchantOperationsBackendDeligate;
import com.bct.custom.backenddeligate.api.ManualMerchantsBackendDelagate;
import com.bct.custom.backenddeligate.api.MerchantCategoriesBackendDeligate;
import com.bct.custom.backenddeligate.api.MerchantChargesBackendDelegate;
import com.bct.custom.backenddeligate.api.MerchantDetailsBackendDeligate;
import com.bct.custom.backenddeligate.api.MerchantFieldsBackendDeligate;
import com.bct.custom.backenddeligate.api.MerchantSubCategoriesBackendDelegate;
import com.bct.custom.backenddeligate.api.PaymentAggregatorBackendDeligate;
import com.bct.custom.backenddeligate.impl.BillPaymentHistoryBackendDelegateImpl;
import com.bct.custom.backenddeligate.impl.BranchDetailsBackendDelegateImpl;
import com.bct.custom.backenddeligate.impl.CIPSPaymentHistoryBackendDeligateImpl;
import com.bct.custom.backenddeligate.impl.GetAllMerchantOperationsBackendDeligateImpl;
import com.bct.custom.backenddeligate.impl.ManualMerchantsBackendDelagateImpl;
import com.bct.custom.backenddeligate.impl.MerchantCategoriesBackendDeligateImpl;
import com.bct.custom.backenddeligate.impl.MerchantChargesBackendDelegateImpl;
import com.bct.custom.backenddeligate.impl.MerchantDetailsBackendDeligateImpl;
import com.bct.custom.backenddeligate.impl.MerchantFieldsBackendDeligateImpl;
import com.bct.custom.backenddeligate.impl.MerchantSubCategoriesBackendDelegateImpl;
import com.bct.custom.backenddeligate.impl.PaymentAggregatorBackendDeligateImpl;
import com.bct.custom.businessdeligate.api.FavoriteMerchantBusinessDelegate;
import com.bct.custom.businessdeligate.api.ManualMerchantsBusinessDelagate;
import com.bct.custom.businessdeligate.api.MerchantChargesBusinessDelegate;
import com.bct.custom.businessdeligate.api.MerchantSubCategoriesBusinessDelegate;
import com.bct.custom.businessdeligate.impl.FavoriteMerchantBusinessDelegateImpl;
import com.bct.custom.businessdeligate.impl.MerchantSubCategoriesBusinessDelegateImpl;
import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;

public class HBLCustomBackendDelegateMapper implements DBPAPIMapper<BackendDelegate>{

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
		map.put(GetAllMerchantOperationsBackendDeligate.class, GetAllMerchantOperationsBackendDeligateImpl.class);
		map.put(PaymentAggregatorBackendDeligate.class, PaymentAggregatorBackendDeligateImpl.class);
		map.put(MerchantDetailsBackendDeligate.class, MerchantDetailsBackendDeligateImpl.class);
		map.put(MerchantFieldsBackendDeligate.class, MerchantFieldsBackendDeligateImpl.class);
		map.put(MerchantCategoriesBackendDeligate.class, MerchantCategoriesBackendDeligateImpl.class);
		map.put(BillPaymentHistoryBackendDeligate.class, BillPaymentHistoryBackendDelegateImpl.class);
		map.put(BranchdetailsBackendDelegate.class, BranchDetailsBackendDelegateImpl.class);
		map.put(ManualMerchantsBackendDelagate.class, ManualMerchantsBackendDelagateImpl.class);
		map.put(MerchantChargesBackendDelegate.class, MerchantChargesBackendDelegateImpl.class);
		map.put(MerchantSubCategoriesBackendDelegate.class, MerchantSubCategoriesBackendDelegateImpl.class);
		map.put(CIPSPaymentHistoryBackendDeligate.class, CIPSPaymentHistoryBackendDeligateImpl.class);
		return map;
	}
}