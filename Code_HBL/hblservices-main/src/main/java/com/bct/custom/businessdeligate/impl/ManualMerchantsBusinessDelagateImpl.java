package com.bct.custom.businessdeligate.impl;

import org.json.JSONArray;

import com.bct.custom.backenddeligate.api.ManualMerchantsBackendDelagate;
import com.bct.custom.businessdeligate.api.ManualMerchantsBusinessDelagate;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;

public class ManualMerchantsBusinessDelagateImpl implements  ManualMerchantsBusinessDelagate{

	@Override
	public JSONArray getAvailableMerchantsForCreate(String input, DataControllerRequest dcRequest) throws ApplicationException {
		ManualMerchantsBackendDelagate backendDeligate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ManualMerchantsBackendDelagate.class);
				return backendDeligate.getAvailableMerchantsForCreate(input, dcRequest);
	}

}
