package com.temenos.infinity.tradefinanceservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.*;
import com.temenos.infinity.tradefinanceservices.businessdelegate.impl.*;


public class TradeFinanceBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {

        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(GetLetterOfCreditsBusinessDelegate.class, GetLetterOfCreditsBusinessDelegateImpl.class);
        map.put(CreateLetterOfCreditsBusinessDelegate.class, CreateLetterOfCreditsBusinessDelegateImpl.class);
        map.put(UpdateLetterOfCreditsBusinessDelegate.class, UpdateLetterOfCreditsBusinessDelegateImpl.class);
        map.put(GetLetterOfCreditsByIDBusinessDelegate.class, GetLetterOfCreditsByIDBusinessDelegateImpl.class);
        map.put(InitiateDownloadLetterOfCreditsAckBusinessDelegate.class, InitiateDownloadLetterOfCreditsAckBusinessDelegateImpl.class);
        map.put(LetterOfCreditDrawingsBusinessDelegate.class, LetterOfCreditDrawingsBusinessDelegateImpl.class);
        map.put(LetterOfCreditSwiftsAndAdvicesBusinessDelegate.class, LetterOfCreditSwiftsAndAdvicesBusinessDelegateImpl.class);
        map.put(LetterOfCreditsBusinessDelegate.class, LetterOfCreditsBusinessDelegateImpl.class);
        map.put(ExportLetterOfCreditsDrawingsBusinessDelegate.class, ExportLetterOfCreditsDrawingsBusinessDelegateImpl.class);
        map.put(GetExportLetterOfCreditsBusinessDelegate.class, GetExportLetterOfCreditsBusinessDelegateImpl.class);
        map.put(GetExportLetterOfCreditsByIdBusinessDelegate.class, GetExportLetterOfCreditsByIdBusinessDelegateImpl.class);
        map.put(GetAmendmentsLetterOfCreditsBusinessDelegate.class, GetAmendmentsLetterOfCreditsBusinessDelegateImpl.class);
        map.put(InitiateDownloadImportLCAmendmentsBusinessDelegate.class, InitiateDownloadImportLCAmendmentsBusinessDelegateImpl.class);
        map.put(CreateExportLCBusinessDelegate.class, CreateExportLCBusinessDelegateImpl.class);
        map.put(PaymentAdviceBussinessDelegate.class, PaymentAdviceBussinessDelegateImpl.class);
        map.put(InitiateDownloadExportLCBusinessDelegate.class, InitiateDownloadExportLCBusinessDelegateImpl.class);
        map.put(ExportLCAmendmentBusinessDelegate.class, ExportLCAmendmentBusinessDelegateImpl.class);
        map.put(InitiateDownloadExportLCAmendmentsBusinessDelegate.class, InitiateDownloadExportLCAmendmentsBusinessDelegateImpl.class);
        map.put(InitiateDownloadImportLCSwiftBusinessDelegate.class, InitiateDownloadImportLCSwiftBusinessDelegateImpl.class);
        map.put(GuaranteesBusinessDelegate.class, GuaranteesBusinessDelegateImpl.class);
        map.put(CorporatePayeesBusinessDelegate.class, CorporatePayeesBusinessDelegateImpl.class);
        map.put(GuaranteeLCAmendmentsBusinessDelegate.class, GuaranteeLCAmendmentsBusinessDelegateImpl.class);
        map.put(DownloadGuaranteesPdfBusinessDelegate.class, DownloadGuaranteesPdfBusinessDelegateImpl.class);
        map.put(DownloadGuaranteesSwiftPdfBusinessDelegate.class, DownloadGuaranteesSwiftPdfBusinessDelegateImpl.class);
        map.put(GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate.class, GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegateImpl.class);
        map.put(TradeFinanceDocumentsBusinessDelegate.class, TradeFinanceDocumentsBusinessDelegateImpl.class);
        map.put(ReceivedGuaranteeClaimsBusinessDelegate.class, ReceivedGuaranteeClaimsBusinessDelegateImpl.class);
        map.put(ReceivedGuaranteesBusinessDelegate.class, ReceivedGuaranteesBusinessDelegateImpl.class);
        map.put(ReceivedGuaranteeAmendmentsBusinessDelegate.class, ReceivedGuaranteeAmendmentsBusinessDelegateImpl.class);
        map.put(InwardCollectionsBusinessDelegate.class, InwardCollectionsBusinessDelegateImpl.class);
        map.put(InwardCollectionAmendmentsBusinessDelegate.class, InwardCollectionAmendmentsBusinessDelegateImpl.class);
        map.put(IssuedGuaranteeClaimsBusinessDelegate.class, IssuedGuaranteeClaimsBusinessDelegateImpl.class);
        map.put(OutwardCollectionsBusinessDelegate.class, OutwardCollectionsBusinessDelegateImpl.class);
        map.put(OutwardCollectionAmendmentsBusinessDelegate.class, OutwardCollectionAmendmentsBusinessDelegateImpl.class);
        map.put(DashboardBusinessDelegate.class, DashboardBusinessDelegateImpl.class);
        return map;
    }

}
