package com.temenos.infinity.tradefinanceservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.tradefinanceservices.resource.api.*;
import com.temenos.infinity.tradefinanceservices.resource.impl.*;

public class TradeFinanceResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {

        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        map.put(UpdateLetterOfCreditsResource.class, UpdateLetterOfCredtisResourceImpl.class);
        map.put(GetLetterofCreditsByIdResource.class, GetLetterofCreditsByIdResourceImpl.class);
        map.put(GetLetterOfCreditsResource.class, GetLetterOfCreditsResourceImpl.class);
        map.put(CreateLetterOfCreditsResource.class, CreateLetterOfCreditsResourceImpl.class);
        map.put(InitiateDownloadLetterOfCreditsAckResource.class, InitiateDownloadLetterOfCreditsAckResourceImpl.class);
        map.put(LetterOfCreditDrawingsResource.class, LetterOfCreditDrawingsResourceImpl.class);
        map.put(LetterOfCreditSwiftsAndAdvicesResource.class, LetterOfCreditSwiftsAndAdvicesResourceImpl.class);
        map.put(LetterOfCreditsResource.class, LetterOfCreditsResourceImpl.class);
        map.put(ExportLetterOfCreditsDrawingsResource.class, ExportLetterOfCreditsDrawingsResourceImpl.class);
        map.put(GetExportLetterOfCreditsResource.class, GetExportLetterOfCreditsResourceImpl.class);
        map.put(GetExportLetterOfCreditsByIdResource.class, GetExportLetterOfCreditsByIdResourceImpl.class);
        map.put(CreateExportLCResource.class, CreateExportLCResourceImpl.class);
        map.put(GetAmendmentsLetterOfCreditsResource.class, GetAmendmentsLetterOfCreditsResourceImpl.class);
        map.put(InitiateDownloadImportLCAmendmentsResource.class, InitiateDownloadImportLCAmendmentsResourceImpl.class);
        map.put(PaymentAdviceResource.class, PaymentAdviceResourceImpl.class);
        map.put(GenerateExportLCPdfResource.class, GenerateExportLCPdfResourceImpl.class);
        map.put(ExportLCAmendmentResource.class, ExportLCAmendmentResourceImpl.class);
        map.put(GenerateExportLCAmendmentsPdfResource.class, GenerateExportLCAmendmentsPdfResourceImpl.class);
        map.put(GuaranteesResource.class, GuaranteesResourceImpl.class);
        map.put(CorporatePayeesResource.class, CorporatePayeesResourceImpl.class);
        map.put(GuaranteeLCAmendmentsResource.class, GuaranteeLCAmendmentsResourceImpl.class);
        map.put(DownloadGuaranteesPdfResource.class, DownloadGuaranteesPdfResourceImpl.class);
        map.put(TradeFinanceDocumentsResource.class, TradeFinanceDocumentsResourceImpl.class);
        map.put(ReceivedGuaranteeClaimsResource.class, ReceivedGuaranteeClaimsResourceImpl.class);
        map.put(ReceivedGuaranteesResource.class, ReceivedGuaranteesResourceImpl.class);
        map.put(ReceivedGuaranteeAmendmentsResource.class, ReceivedGuaranteeAmendmentsResourceImpl.class);
        map.put(InwardCollectionsResource.class, InwardCollectionsResourceImpl.class);
        map.put(InwardCollectionAmendmentsResource.class, InwardCollectionAmendmentsResourceImpl.class);
        map.put(IssuedGuaranteeClaimsResource.class, IssuedGuaranteeClaimsResourceImpl.class);
        map.put(OutwardCollectionsResource.class, OutwardCollectionsResourceImpl.class);
        map.put(OutwardCollectionAmendmentsResource.class, OutwardCollectionAmendmentsResourceImpl.class);
        map.put(DashboardResource.class, DashboardResourceImpl.class);

        return map;
    }
}
