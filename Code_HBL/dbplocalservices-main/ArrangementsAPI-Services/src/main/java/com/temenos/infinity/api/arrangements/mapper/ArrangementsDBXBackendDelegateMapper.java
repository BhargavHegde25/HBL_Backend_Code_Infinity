package com.temenos.infinity.api.arrangements.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.arrangements.backenddelegate.api.AccountClosureBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.ArrangementOverviewBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.ArrangementsExperienceAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.BusinessUserBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.GetAccountsArrangementsExperienceAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.MortgageFacilityDetailsAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.MortgageFacilityDrawingAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.SubmitChangeRepaymentBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.SubmitPartialRepaymentBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.UserManagementAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.impl.AccountClosureBackenDelegateImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.impl.AccountClosureDBXBackenDelegateImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.impl.ArrangementOverviewBackendDelegateImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.impl.ArrangementsExperienceAPIBackendDelegateImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.impl.BusinessUserBackendDelegateImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.impl.GetAccountsArrangementsExperienceAPIBackendDelegateImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.impl.MortgageFacilityDetailsAPIBackendDelegateImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.impl.MortgageFacilityDrawingAPIBackendDelegateImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.impl.SubmitChangeRepaymentBackendDelegateImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.impl.SubmitPartialRepaymentBackendDelegateImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.impl.UserManagementAPIBackendDelegateImpl;

public class ArrangementsDBXBackendDelegateMapper implements DBPAPIMapper<BackendDelegate>{

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
		map.put(ArrangementsExperienceAPIBackendDelegate.class, ArrangementsExperienceAPIBackendDelegateImpl.class);
        map.put(BusinessUserBackendDelegate.class, BusinessUserBackendDelegateImpl.class);
        map.put(GetAccountsArrangementsExperienceAPIBackendDelegate.class,
                GetAccountsArrangementsExperienceAPIBackendDelegateImpl.class);
        map.put(ArrangementOverviewBackendDelegate.class, ArrangementOverviewBackendDelegateImpl.class);
        map.put(MortgageFacilityDetailsAPIBackendDelegate.class, MortgageFacilityDetailsAPIBackendDelegateImpl.class);
        map.put(MortgageFacilityDrawingAPIBackendDelegate.class, MortgageFacilityDrawingAPIBackendDelegateImpl.class);
        map.put(UserManagementAPIBackendDelegate.class, UserManagementAPIBackendDelegateImpl.class);
        map.put(SubmitChangeRepaymentBackendDelegate.class, SubmitChangeRepaymentBackendDelegateImpl.class);
        map.put(SubmitPartialRepaymentBackendDelegate.class, SubmitPartialRepaymentBackendDelegateImpl.class);
		map.put(AccountClosureBackendDelegate.class, AccountClosureDBXBackenDelegateImpl.class);
		return map;
	}

}
