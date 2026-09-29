package com.temenos.infinity.product.bulkpaymentservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.product.bulkpaymentservices.resource.api.BulkPaymentFileResource;
import com.temenos.infinity.product.bulkpaymentservices.resource.api.BulkPaymentPOResource;
import com.temenos.infinity.product.bulkpaymentservices.resource.api.BulkPaymentRecordResource;
import com.temenos.infinity.product.bulkpaymentservices.resource.api.BulkPaymentTemplateResource;
import com.temenos.infinity.product.bulkpaymentservices.resource.impl.BulkPaymentFileResourceImpl;
import com.temenos.infinity.product.bulkpaymentservices.resource.impl.BulkPaymentPOResourceImpl;
import com.temenos.infinity.product.bulkpaymentservices.resource.impl.BulkPaymentRecordResourceImpl;
import com.temenos.infinity.product.bulkpaymentservices.resource.impl.BulkPaymentTemplateResourceImpl;

public class BulkPaymentServicesResourceMapper implements DBPAPIMapper<Resource>{

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
		map.put(BulkPaymentFileResource.class, BulkPaymentFileResourceImpl.class);
		map.put(BulkPaymentPOResource.class, BulkPaymentPOResourceImpl.class);
		map.put(BulkPaymentRecordResource.class, BulkPaymentRecordResourceImpl.class);
		map.put(BulkPaymentTemplateResource.class, BulkPaymentTemplateResourceImpl.class);
		return map;
	}

}
