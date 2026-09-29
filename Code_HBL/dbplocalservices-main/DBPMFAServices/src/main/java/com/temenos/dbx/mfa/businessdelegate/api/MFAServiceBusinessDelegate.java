package com.temenos.dbx.mfa.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;

/**
 * 
 * @version 1.0
 *
 */
public interface MFAServiceBusinessDelegate extends BusinessDelegate {

    /**
     * 
     * @param serviceName
     * @param phone
     * @param email
     * @param requestJson
     * @param headerMap
     * @return MFAServiceDTO
     * @throws ApplicationException
     */
    public MFAServiceDTO createMfaService(String serviceName, String phone, String email, String requestJson,
            Map<String, Object> headerMap) throws ApplicationException;

    /**
     * 
     * @param mfaserviceDTO
     * @param inputParams
     *            if masterServiceKey present
     * @param headersMap
     * @return List<MFAServiceDTO>
     * @throws ApplicationException
     */

    public List<MFAServiceDTO> getMfaService(MFAServiceDTO mfaserviceDTO, Map<String, Object> inputParams,
            Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param mfaserviceDTO
     * @param headersMap
     * @return update status flag
     * @throws ApplicationException
     */
    public boolean updateMfaService(MFAServiceDTO mfaserviceDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    public boolean deleteMfaService(String serviceKey, Map<String, Object> headersMap) throws ApplicationException;

    public boolean validateMasterServiceKeyAndServiceKey(JsonObject masterServiceKeyPayloadJson,
            JsonObject serviceKeyPayloadJson);

    public boolean updateGivenServiceKeyRecord(JsonObject payloadJson, String serviceKey,
            Map<String, Object> headersMap) throws ApplicationException;

    /**
     * Creates entry in MFAService table
     * @param mfaDTO
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public MFAServiceDTO create(MFAServiceDTO mfaDTO, Map<String, Object> headersMap) throws ApplicationException;

}
