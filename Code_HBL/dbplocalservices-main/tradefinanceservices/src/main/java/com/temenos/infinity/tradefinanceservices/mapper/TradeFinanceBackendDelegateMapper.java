package com.temenos.infinity.tradefinanceservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.*;
import com.temenos.infinity.tradefinanceservices.backenddelegate.impl.*;

public class TradeFinanceBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {

        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
        map.put(GetLetterOfCreditsByIdBackendDelegate.class, GetLetterOfCreditsByIdBackendDelegateImpl.class);
        map.put(GetLetterOfCreditsBackendDelegate.class, GetLetterOfCreditsBackendDelegateImpl.class);
        map.put(CreateLetterOfCreditBackendDelegate.class, CreateLetterOfCreditBackendDelegateImpl.class);
        map.put(UpdateLetterOfCreditsBackendDelegate.class, UpdateLetterOfCreditsBackendDelegateImpl.class);
        map.put(LetterOfCreditDrawingsBackendDelegate.class, LetterOfCreditDrawingsBackendDelegateImpl.class);
        map.put(LetterOfCreditSwiftsAndAdvicesBackendDelegate.class, LetterOfCreditSwiftsAndAdvicesBackendDelegateImpl.class);
        map.put(LetterOfCreditsBackendDelegate.class, LetterOfCreditsBackendDelegateImpl.class);
        map.put(ExportLetterOfCreditsDrawingsBackendDelegate.class, ExportLetterOfCreditsDrawingsBackendDelegateImpl.class);
        map.put(GetExportLetterOfCreditsBackendDelegate.class, GetExportLetterOfCreditsBackendDelegateImpl.class);
        map.put(GetExportLetterOfCreditsByIdBackendDelegate.class, GetExportLetterOfCreditsByIdBackendDelegateImpl.class);
        map.put(GetAmendmentsLetterOfCreditsBackendDelegate.class, GetAmendmentsLetterOfCreditsBackendDelegateImpl.class);
        map.put(CreateExportLCBackendDelegate.class, CreateExportLCBackendDelegateImpl.class);
        map.put(PaymentAdviceBackendDelegate.class, PaymentAdviceBackendDelegateImpl.class);
        map.put(ExportLCAmendmentBackendDelegate.class, ExportLCAmendmentBackendDelegateImpl.class);
        map.put(GuaranteesBackendDelegate.class, GuaranteesBackendDelegateImpl.class);
        map.put(CorporatePayeesBackendDelegate.class, CorporatePayeesBackendDelegateImpl.class);
        map.put(GuaranteeLCAmendmentsBackendDelegate.class, GuaranteeLCAmendmentsBackendDelegateImpl.class);
        map.put(GuaranteesAmendmentsSwiftAndAdvicesBackendDelegate.class, GuaranteesAmendmentsSwiftAndAdvicesBackendDelegateImpl.class);
        map.put(TradeFinanceDocumentsBackendDelegate.class, TradeFinanceDocumentsBackendImpl.class);
        map.put(ReceivedGuaranteeClaimsBackendDelegate.class, ReceivedGuaranteeClaimsBackendDelegateImpl.class);
        map.put(ReceivedGuaranteesBackendDelegate.class, ReceivedGuaranteesBackendDelegateImpl.class);
        map.put(ReceivedGuaranteeAmendmentsBackendDelegate.class, ReceivedGuaranteeAmendmentsBackendDelegateImpl.class);
        map.put(InwardCollectionsBackendDelegate.class, InwardCollectionsBackendDelegateImpl.class);
        map.put(InwardCollectionAmendmentsBackendDelegate.class, InwardCollectionAmendmentsBackendDelegateImpl.class);
        map.put(IssuedGuaranteeClaimsBackendDelegate.class, IssuedGuaranteeClaimsBackendDelegateImpl.class);
        map.put(OutwardCollectionsBackendDelegate.class, OutwardCollectionsBackendDelegateImpl.class);
        map.put(OutwardCollectionAmendmentsBackendDelegate.class, OutwardCollectionAmendmentsBackendDelegateImpl.class);
        map.put(DashboardBackendDelegate.class, DashboardBackendDelegateImpl.class);
        return map;
    }

} 
