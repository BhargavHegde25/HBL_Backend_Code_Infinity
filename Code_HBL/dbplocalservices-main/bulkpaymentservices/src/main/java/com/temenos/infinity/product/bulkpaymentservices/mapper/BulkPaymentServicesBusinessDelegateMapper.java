package com.temenos.infinity.product.bulkpaymentservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.product.bulkpaymentservices.businessdelegate.api.BulkPaymentFileBusinessDelegate;
import com.temenos.infinity.product.bulkpaymentservices.businessdelegate.api.BulkPaymentPOBusinessDelegate;
import com.temenos.infinity.product.bulkpaymentservices.businessdelegate.api.BulkPaymentRecordBusinessDelegate;
import com.temenos.infinity.product.bulkpaymentservices.businessdelegate.api.BulkPaymentTemplateBusinessDelegate;
import com.temenos.infinity.product.bulkpaymentservices.businessdelegate.impl.BulkPaymentFileBusinessDelegateImpl;
import com.temenos.infinity.product.bulkpaymentservices.businessdelegate.impl.BulkPaymentPOBusinessDelegateImpl;
import com.temenos.infinity.product.bulkpaymentservices.businessdelegate.impl.BulkPaymentRecordBusinessDelegateImpl;
import com.temenos.infinity.product.bulkpaymentservices.businessdelegate.impl.BulkPaymentTemplateBusinessDelegateImpl;

public class BulkPaymentServicesBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {

		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
        map.put(BulkPaymentFileBusinessDelegate.class, BulkPaymentFileBusinessDelegateImpl.class);
        map.put(BulkPaymentPOBusinessDelegate.class, BulkPaymentPOBusinessDelegateImpl.class);
        map.put(BulkPaymentRecordBusinessDelegate.class, BulkPaymentRecordBusinessDelegateImpl.class);
        map.put(BulkPaymentTemplateBusinessDelegate.class, BulkPaymentTemplateBusinessDelegateImpl.class);
        return map;
	}

}
