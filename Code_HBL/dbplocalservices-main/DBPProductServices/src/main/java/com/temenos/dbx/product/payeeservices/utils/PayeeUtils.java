package com.temenos.dbx.product.payeeservices.utils;

import com.dbp.core.api.DBPDTO;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalsframework.approvalrequest.backenddelegate.api.ApprovalRequestBackendDelegate;
import com.temenos.dbx.product.commons.backenddelegate.api.UserBackendDelegate;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.ExternalPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.constants.PayeeConstants;
import com.temenos.dbx.product.payeeservices.dto.InterBankPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.InterBankPayeeDTO;
import com.temenos.dbx.product.payeeservices.dto.InternationalPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.InternationalPayeeDTO;
import com.temenos.dbx.product.payeeservices.dto.IntraBankPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.IntraBankPayeeDTO;
import com.temenos.dbx.product.utils.ThreadExecutor;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public class PayeeUtils {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
    private static JSONObject getAdditionalMetaDataForBeneficiary( DBPDTO payeeDTO ) {
        JSONObject jsonObject = new JSONObject(payeeDTO);
        jsonObject.put(PayeeConstants.OLD_INFO_KEY, "");
        return jsonObject;
    }
    public static JSONArray prepareDeletePayeeContractCifMap( List<? extends DBPDTO> dbpdtoList ) {
        JSONArray arr = new JSONArray();
        Map<String, JSONObject> contractMap = new HashMap<>();
        String contractId;
        String cif;
        for ( DBPDTO payeeDTO : dbpdtoList ) {
            if(payeeDTO instanceof InterBankPayeeDTO) {
                contractId = ( (InterBankPayeeDTO) payeeDTO ).getContractId();
                cif = ( (InterBankPayeeDTO) payeeDTO ).getCif();
            } else if(payeeDTO instanceof IntraBankPayeeDTO) {
                contractId = ( (IntraBankPayeeDTO) payeeDTO ).getContractId();
                cif = ( (IntraBankPayeeDTO) payeeDTO ).getCif();
            } else {
                contractId = ( (InternationalPayeeDTO) payeeDTO ).getContractId();
                cif = ( (InternationalPayeeDTO) payeeDTO ).getCif();
            }
            if(contractMap.containsKey(contractId)) {
                JSONObject object = contractMap.get(contractId);
                JSONArray cifsArray = object.getJSONArray(PayeeConstants.CIFS_KEY);
                cifsArray.put(getDeletedCifObject(cif));
            } else {
                JSONObject object = new JSONObject();
                object.put(PayeeConstants.CONTRACT_ID_KEY, contractId);
                object.put(PayeeConstants.CIFS_KEY, getDeletedCifsArray(cif));
                contractMap.put(contractId, object);
            }
        }
        for ( String key : contractMap.keySet() ) {
            arr.put(contractMap.get(key));
        }
        return arr;
    }
    private static JSONArray getDeletedCifsArray( String cifs ) {
        JSONArray cifArray = new JSONArray();
        String[] cifList = cifs.split(",");
        for ( String cif : cifList ) {
            JSONObject object = getDeletedCifObject(cif);
            cifArray.put(object);
        }
        return cifArray;
    }
    private static JSONObject getDeletedCifObject( String cifId ) {
        return getCifObject(cifId, false);
    }
    private static JSONObject getAddedCifObject( String cifId ) {
        return getCifObject(cifId, true);
    }
    private static JSONObject getCifObject( String cifId, boolean isLinked ) {
        JSONObject cifObj = new JSONObject();
        cifObj.put("id", cifId);
        cifObj.put("isLinked", isLinked);
        return cifObj;
    }
    private static JSONArray getCifsArray( String cifs ) {
        JSONArray cifArray = new JSONArray();
        String[] cifList = cifs.split(",");
        for ( String cif : cifList ) {
            JSONObject object = getAddedCifObject(cif);
            cifArray.put(object);
        }
        return cifArray;
    }
    public static JSONArray parseContractCifMap(String cifMap) {
        JSONArray contractCifList = new JSONArray();
        JSONObject contractIdAndCifMapping = new JSONObject();
        JSONArray arr = new JSONArray(cifMap);
        if(arr != null) {
            for(int i=0; i<arr.length(); ++i) {
                JSONObject currObj = arr.optJSONObject(i);
                String contractId = currObj.optString(PayeeConstants.CONTRACT_ID_KEY);
                String cifId = currObj.optString(PayeeConstants.CORE_CUSTOMER_ID);
                if(contractIdAndCifMapping.has(contractId)) {
                    JSONObject contractObj = contractIdAndCifMapping.getJSONObject(contractId);
                    JSONArray cifs = contractObj.getJSONArray(PayeeConstants.CIFS_KEY);
                    JSONObject cifObj = getAddedCifObject(cifId);
                    cifs.put(cifObj);
                } else {
                    JSONArray cifs = getCifsArray(cifId);
                    JSONObject contractObj = new JSONObject();
                    contractObj.put(PayeeConstants.CONTRACT_ID_KEY, contractId);
                    contractObj.put(PayeeConstants.CIFS_KEY, cifs);
                    contractIdAndCifMapping.put(contractId, contractObj);
                }
            }
            for ( String key : contractIdAndCifMapping.keySet() ) {
                contractCifList.put(contractIdAndCifMapping.get(key));
            }
        }
        return contractCifList;
    }
    public static TransactionStatusDTO prepareEditLinkagePayload( DBPDTO payeeDTO, Map<String, List<String>> addedCifsMap, Map<String, List<String>> removedCifsMap, List<? extends DBPDTO> interBankPayeeDTOList ) {
        Map<String, Map<String, Boolean>> newContractCifLinkUnlinkMap = computeEffectiveContractCifMap(addedCifsMap, removedCifsMap);
        String cifs;
        if(payeeDTO instanceof InterBankPayeeBackendDTO) {
            cifs = ( (InterBankPayeeBackendDTO) payeeDTO ).getCif();
        } else if(payeeDTO instanceof IntraBankPayeeBackendDTO) {
            cifs = ( (IntraBankPayeeBackendDTO) payeeDTO ).getCif();
        } else {
            cifs = ( (InternationalPayeeBackendDTO) payeeDTO ).getCif();
        }
        JSONArray currentCifLinkageArray = new JSONArray(cifs);
        for ( int i = 0; i < currentCifLinkageArray.length(); i++ ) {
            JSONObject jsonObject = currentCifLinkageArray.getJSONObject(i);
            String contractId = jsonObject.getString(PayeeConstants.CONTRACT_ID_KEY);
            Map<String, Boolean> cifMap;
            if(newContractCifLinkUnlinkMap.containsKey(contractId)) {
                cifMap = newContractCifLinkUnlinkMap.get(contractId);
            } else {
                cifMap = new HashMap<>();
                newContractCifLinkUnlinkMap.put(contractId, cifMap);
            }
            String coreCustomerId = jsonObject.getString(PayeeConstants.CORE_CUSTOMER_ID);
            for ( String cif : coreCustomerId.split(",") ) {
                if(!cifMap.containsKey(cif)) {
                    cifMap.put(cif, true);
                }
            }
        }

        Map<String, Map<String, Boolean>> oldContractCifLinkUnlinkMap = new HashMap<>();
        for ( DBPDTO currentPayeeDTO: interBankPayeeDTOList ) {
            String contractId;
            String cif;
            if(currentPayeeDTO instanceof InterBankPayeeDTO) {
                contractId = ( (InterBankPayeeDTO) currentPayeeDTO ).getContractId();
                cif = ( (InterBankPayeeDTO) currentPayeeDTO ).getCif();
            } else if(currentPayeeDTO instanceof IntraBankPayeeDTO) {
                contractId = ( (IntraBankPayeeDTO) currentPayeeDTO ).getContractId();
                cif = ( (IntraBankPayeeDTO) currentPayeeDTO ).getCif();
            } else {
                contractId = ( (InternationalPayeeDTO) currentPayeeDTO ).getContractId();
                cif = ( (InternationalPayeeDTO) currentPayeeDTO ).getCif();
            }
            Map<String, Boolean> contractCifMap;
            if(oldContractCifLinkUnlinkMap.containsKey(contractId)) {
                contractCifMap = oldContractCifLinkUnlinkMap.get(contractId);
            } else {
                contractCifMap = new HashMap<>();
                oldContractCifLinkUnlinkMap.put(contractId, contractCifMap);
            }
            contractCifMap.put(cif, true);
        }

        JSONArray newContractCifLinkUnlinkDetails = new JSONArray();
        getContractsArrayFromContractMap(newContractCifLinkUnlinkMap, newContractCifLinkUnlinkDetails);

        JSONArray oldContractCifLinkUnlinkDetails = new JSONArray();
        getContractsArrayFromContractMap(oldContractCifLinkUnlinkMap, oldContractCifLinkUnlinkDetails);

        TransactionStatusDTO transactionStatusDTO;
        if(payeeDTO instanceof InterBankPayeeBackendDTO) {
            ((InterBankPayeeBackendDTO) payeeDTO).setCif(newContractCifLinkUnlinkDetails.toString());
            transactionStatusDTO = getPayloadForValidateForApprovals(payeeDTO);

            ((InterBankPayeeBackendDTO) payeeDTO).setCif(oldContractCifLinkUnlinkDetails.toString());
            transactionStatusDTO.getAdditionalMetaInfo().put(PayeeConstants.OLD_INFO_KEY, new JSONObject(payeeDTO));
            transactionStatusDTO.setFeatureActionID(FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE);
        } else if(payeeDTO instanceof IntraBankPayeeBackendDTO) {
            ((IntraBankPayeeBackendDTO) payeeDTO).setCif(newContractCifLinkUnlinkDetails.toString());
            transactionStatusDTO = getPayloadForValidateForApprovals(payeeDTO);

            ((IntraBankPayeeBackendDTO) payeeDTO).setCif(oldContractCifLinkUnlinkDetails.toString());
            transactionStatusDTO.getAdditionalMetaInfo().put(PayeeConstants.OLD_INFO_KEY, new JSONObject(payeeDTO));
            transactionStatusDTO.setFeatureActionID(FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE);
        } else {
            ((InternationalPayeeBackendDTO) payeeDTO).setCif(newContractCifLinkUnlinkDetails.toString());
            transactionStatusDTO = getPayloadForValidateForApprovals(payeeDTO);

            ((InternationalPayeeBackendDTO) payeeDTO).setCif(oldContractCifLinkUnlinkDetails.toString());
            transactionStatusDTO.getAdditionalMetaInfo().put(PayeeConstants.OLD_INFO_KEY, new JSONObject(payeeDTO));
            transactionStatusDTO.setFeatureActionID(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE);
        }

        Map<String, Map<String, Boolean>> effectiveContractCifMap = computeEffectiveContractCifMap(addedCifsMap, removedCifsMap);

        JSONArray effectiveContractCifDetails = new JSONArray();
        getContractsArrayFromContractMap(effectiveContractCifMap, effectiveContractCifDetails);
        transactionStatusDTO.setContractCifMap(effectiveContractCifDetails);
        return transactionStatusDTO;
    }
    private static Map<String, Map<String, Boolean>> computeEffectiveContractCifMap( Map<String, List<String>> addedCifMaps, Map<String, List<String>> removedCifMaps ) {
        Map<String, Map<String, Boolean>> newlyLinkedOrUnlinkedContractCifMap = new HashMap<>();
        if(!removedCifMaps.isEmpty()) {
            extractContractCifLinkUnlink(removedCifMaps, newlyLinkedOrUnlinkedContractCifMap, false);
        }
        if(!addedCifMaps.isEmpty()) {
            extractContractCifLinkUnlink(addedCifMaps, newlyLinkedOrUnlinkedContractCifMap, true);
        }
        return newlyLinkedOrUnlinkedContractCifMap;
    }
    private static void getContractsArrayFromContractMap( Map<String, Map<String, Boolean>> contractCifLinkUnlinkMap, JSONArray newContractCifLinkUnlinkDetails ) {
        for ( Map.Entry<String, Map<String, Boolean>> entry : contractCifLinkUnlinkMap.entrySet() ) {
            String contractId = entry.getKey();
            Map<String, Boolean> cifMap = entry.getValue();
            JSONObject contractObject = new JSONObject();
            contractObject.put(PayeeConstants.CONTRACT_ID_KEY, contractId);
            JSONArray cifsArray = new JSONArray();
            for ( Map.Entry<String, Boolean> cifEntry: cifMap.entrySet() ) {
                String cifId = cifEntry.getKey();
                boolean isLinked = cifEntry.getValue();
                JSONObject cifObject;
                if(isLinked) {
                    cifObject = getAddedCifObject(cifId);
                } else {
                    cifObject = getDeletedCifObject(cifId);
                }
                cifsArray.put(cifObject);
            }
            contractObject.put(PayeeConstants.CIFS_KEY, cifsArray);
            newContractCifLinkUnlinkDetails.put(contractObject);
        }
    }
    private static void extractContractCifLinkUnlink( Map<String, List<String>> cifMaps, Map<String, Map<String, Boolean>> contractCifLinkUnlinkMap, boolean isLinked ) {
        for ( Map.Entry<String, List<String>> entry : cifMaps.entrySet() ) {
            String contractId = entry.getKey();
            List<String> cifs = entry.getValue();
            Map<String, Boolean> cifMap;
            if(contractCifLinkUnlinkMap.containsKey(contractId)) {
                cifMap = contractCifLinkUnlinkMap.get(contractId);
            } else {
                cifMap = new HashMap<>();
            }
            for ( String cif : cifs ) {
                cifMap.put(cif, isLinked);
            }
            contractCifLinkUnlinkMap.put(contractId, cifMap);
        }
    }
    public static TransactionStatusDTO prepareEditOptionalPayload( DBPDTO dto, DBPDTO oldPayeeDTO ) {
        TransactionStatusDTO transactionStatusDTO = getPayloadForValidateForApprovals(dto);
        if(dto instanceof InterBankPayeeBackendDTO && oldPayeeDTO instanceof InterBankPayeeBackendDTO) {
            JSONArray contractCifMap = parseContractCifMap(( (InterBankPayeeBackendDTO) dto ).getCif());
            ((InterBankPayeeBackendDTO) dto).setCif(contractCifMap.toString());
            transactionStatusDTO.setFeatureActionID(FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL);
            transactionStatusDTO.setContractCifMap(contractCifMap);
            ( (InterBankPayeeBackendDTO) oldPayeeDTO ).setCif(contractCifMap.toString());
        } else if(dto instanceof IntraBankPayeeBackendDTO && oldPayeeDTO instanceof IntraBankPayeeBackendDTO) {
            JSONArray contractCifMap = parseContractCifMap(( (IntraBankPayeeBackendDTO) dto ).getCif());
            ((IntraBankPayeeBackendDTO) dto).setCif(contractCifMap.toString());
            transactionStatusDTO.setFeatureActionID(FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL);
            transactionStatusDTO.setContractCifMap(contractCifMap);
            ( (IntraBankPayeeBackendDTO) oldPayeeDTO ).setCif(contractCifMap.toString());
        } else if(dto instanceof InternationalPayeeBackendDTO && oldPayeeDTO instanceof InternationalPayeeBackendDTO) {
            JSONArray contractCifMap = parseContractCifMap(( (InternationalPayeeBackendDTO) dto ).getCif());
            ((InternationalPayeeBackendDTO) dto).setCif(contractCifMap.toString());
            transactionStatusDTO.setFeatureActionID(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL);
            transactionStatusDTO.setContractCifMap(contractCifMap);
            ( (InternationalPayeeBackendDTO) oldPayeeDTO ).setCif(contractCifMap.toString());
        } else {
            return transactionStatusDTO;
        }
        JSONObject oldInfo = new JSONObject(oldPayeeDTO);
        JSONObject additionalMetaInfo = transactionStatusDTO.getAdditionalMetaInfo();
        additionalMetaInfo.put(PayeeConstants.CIF_KEY, parseContractCifMap(additionalMetaInfo.getString(PayeeConstants.CIF_KEY)));
        additionalMetaInfo.put(PayeeConstants.OLD_INFO_KEY, oldInfo.toString());
        transactionStatusDTO.setAdditionalMetaData(additionalMetaInfo);
        return transactionStatusDTO;
    }
    public static TransactionStatusDTO getPayloadForValidateForApprovalsForCreatePayee(DBPDTO dto) {
        TransactionStatusDTO transactionStatusDTO = getPayloadForValidateForApprovals(dto);
        if(dto instanceof InterBankPayeeBackendDTO) {
            transactionStatusDTO.setFeatureActionID(FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT);
            transactionStatusDTO.setContractCifMap(parseContractCifMap(((InterBankPayeeBackendDTO) dto).getCif()));
        } else if(dto instanceof IntraBankPayeeBackendDTO) {
            transactionStatusDTO.setFeatureActionID(FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE_RECEPIENT);
            transactionStatusDTO.setContractCifMap(parseContractCifMap(((IntraBankPayeeBackendDTO) dto).getCif()));
        } else {
            transactionStatusDTO.setFeatureActionID(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT);
            transactionStatusDTO.setContractCifMap(parseContractCifMap(((InternationalPayeeBackendDTO) dto).getCif()));
        }

        return transactionStatusDTO;
    }
    public static TransactionStatusDTO getPayloadForValidateForApprovalsForDeletePayee( DBPDTO newDTO, DBPDTO oldDTO, List<? extends DBPDTO> dbpdtoList ) {
        TransactionStatusDTO transactionStatusDTO;
        transactionStatusDTO = getPayloadForValidateForApprovals(newDTO);
        if(newDTO instanceof InterBankPayeeBackendDTO) {
            transactionStatusDTO.setFeatureActionID(FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_DELETE_RECEPIENT);
        } else if(newDTO instanceof IntraBankPayeeBackendDTO) {
            transactionStatusDTO.setFeatureActionID(FeatureAction.INTRA_BANK_FUND_TRANSFER_DELETE_RECEPIENT);
        } else {
            transactionStatusDTO.setFeatureActionID(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_DELETE_RECEPIENT);
        }
        transactionStatusDTO.setContractCifMap(prepareDeletePayeeContractCifMap(dbpdtoList));
        JSONObject additionalMetaInfo = transactionStatusDTO.getAdditionalMetaInfo();
        JSONObject oldInfo = new JSONObject(oldDTO);
        oldInfo.put(PayeeConstants.CIF_KEY, prepareDeletePayeeOldContractCifMap(dbpdtoList));
        additionalMetaInfo.put(PayeeConstants.OLD_INFO_KEY, oldInfo.toString());
        transactionStatusDTO.getAdditionalMetaInfo().put(PayeeConstants.CIF_KEY, prepareDeletePayeeContractCifMap(dbpdtoList).toString());
        return transactionStatusDTO;
    }

    private static JSONArray prepareDeletePayeeOldContractCifMap( List<? extends DBPDTO> dbpdtoList ) {
        JSONArray arr = new JSONArray();
        Map<String, JSONObject> contractMap = new HashMap<>();
        String contractId;
        String cif;
        for ( DBPDTO payeeDTO : dbpdtoList ) {
            if(payeeDTO instanceof InterBankPayeeDTO) {
                contractId = ( (InterBankPayeeDTO) payeeDTO ).getContractId();
                cif = ( (InterBankPayeeDTO) payeeDTO ).getCif();
            } else if(payeeDTO instanceof IntraBankPayeeDTO) {
                contractId = ( (IntraBankPayeeDTO) payeeDTO ).getContractId();
                cif = ( (IntraBankPayeeDTO) payeeDTO ).getCif();
            } else {
                contractId = ( (InternationalPayeeDTO) payeeDTO ).getContractId();
                cif = ( (InternationalPayeeDTO) payeeDTO ).getCif();
            }
            if(contractMap.containsKey(contractId)) {
                JSONObject object = contractMap.get(contractId);
                JSONArray cifsArray = object.getJSONArray(PayeeConstants.CIFS_KEY);
                cifsArray.put(getAddedCifObject(cif));
            } else {
                JSONObject object = new JSONObject();
                object.put(PayeeConstants.CONTRACT_ID_KEY, contractId);
                object.put(PayeeConstants.CIFS_KEY, getCifsArray(cif));
                contractMap.put(contractId, object);
            }
        }
        for ( String key : contractMap.keySet() ) {
            arr.put(contractMap.get(key));
        }
        return arr;
    }

    public static TransactionStatusDTO getPayloadForValidateForApprovals( DBPDTO dto ) {
        TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
        transactionStatusDTO.setStatus(TransactionStatusEnum.NEW);
        transactionStatusDTO.setAdditionalMetaData(getAdditionalMetaDataForBeneficiary(dto));

        if(dto instanceof InterBankPayeeBackendDTO) {
            transactionStatusDTO.setConfirmationNumber(((InterBankPayeeBackendDTO) dto).getPayeeId());
            transactionStatusDTO.setCustomerId(((InterBankPayeeBackendDTO) dto).getUserId());
        } else if(dto instanceof IntraBankPayeeBackendDTO) {
            transactionStatusDTO.setConfirmationNumber(((IntraBankPayeeBackendDTO) dto).getPayeeId());
            transactionStatusDTO.setCustomerId(((IntraBankPayeeBackendDTO) dto).getUserId());
        } else {
            transactionStatusDTO.setConfirmationNumber(((InternationalPayeeBackendDTO) dto).getPayeeId());
            transactionStatusDTO.setCustomerId(((InternationalPayeeBackendDTO) dto).getUserId());
        }
        return transactionStatusDTO;
    }

    public static Map<String, Map<String, Boolean>> parseInputContractCifMap( JSONArray newContractCifArray ) {
        Map<String, Map<String, Boolean>> newContractCifMapping = new HashMap<>();
        for ( int i = 0; i < newContractCifArray.length(); i++ ) {
            JSONObject jsonObject = newContractCifArray.getJSONObject(i);
            String contractId = jsonObject.getString(PayeeConstants.CONTRACT_ID_KEY);
            Map<String, Boolean> cifMapping;
            if(newContractCifMapping.containsKey(contractId)) {
                cifMapping = newContractCifMapping.get(contractId);
            } else {
                cifMapping = new HashMap<>();
                newContractCifMapping.put(contractId, cifMapping);
            }
            JSONArray cifsArray = jsonObject.getJSONArray(PayeeConstants.CIFS_KEY);
            for ( int j = 0; j < cifsArray.length(); j++ ) {
                JSONObject cifObject = cifsArray.getJSONObject(j);
                String cifId = cifObject.getString(PayeeConstants.ID_KEY);
                boolean isLinked = cifObject.getBoolean(PayeeConstants.IS_LINKED_KEY);
                cifMapping.put(cifId, isLinked);
            }
        }
        return newContractCifMapping;
    }

    public static Map<String, Map<String, List<String>>> getAddedAndRemovedContractCifMaps( JSONObject inputObject ) {
        Map<String, Map<String, List<String>>> addedAndRemovedContractCifMaps = new HashMap<>();
        String newCif = inputObject.getString(PayeeConstants.CIF_KEY);
        String oldCif = inputObject.getJSONObject(PayeeConstants.OLD_INFO_KEY).getString(PayeeConstants.CIF_KEY);

        JSONArray newContractCifArray = new JSONArray(newCif);
        JSONArray oldContractCifArray = new JSONArray(oldCif);

        Map<String, Map<String, Boolean>> newContractCifMapping = PayeeUtils.parseInputContractCifMap(newContractCifArray);

        Map<String, Map<String, Boolean>> oldContractCifMapping = PayeeUtils.parseInputContractCifMap(oldContractCifArray);

        Map<String, List<String>> addedContractCifMap = new HashMap<>();
        Map<String, List<String>> removedContractCifMap = new HashMap<>();

        for ( String contractId : newContractCifMapping.keySet() ) {
            Map<String, Boolean> oldCifMap = oldContractCifMapping.getOrDefault(contractId, new HashMap());
            Map<String, Boolean> newCifMap = newContractCifMapping.getOrDefault(contractId, new HashMap());
            for ( String cif: newCifMap.keySet()) {
                Boolean isLinkedInNewCifMap = newCifMap.getOrDefault(cif, false);
                Boolean isLinkedInOldCifMap = oldCifMap.getOrDefault(cif, false);
                if(isLinkedInOldCifMap && !isLinkedInNewCifMap) {
                    List<String> cifList;
                    if(removedContractCifMap.containsKey(contractId)) {
                        cifList = removedContractCifMap.get(contractId);
                    } else {
                        cifList = new ArrayList<>();
                        removedContractCifMap.put(contractId, cifList);
                    }
                    cifList.add(cif);
                } else if(!isLinkedInOldCifMap && isLinkedInNewCifMap) {
                    List<String> cifList;
                    if(addedContractCifMap.containsKey(contractId)) {
                        cifList = addedContractCifMap.get(contractId);
                    } else {
                        cifList = new ArrayList<>();
                        addedContractCifMap.put(contractId, cifList);
                    }
                    cifList.add(cif);
                }
            }
        }

        addedAndRemovedContractCifMaps.put(PayeeConstants.ADDED_CONTRACT_CIF_MAP_KEY, addedContractCifMap);
        addedAndRemovedContractCifMaps.put(PayeeConstants.REMOVED_CONTRACT_CIF_MAP_KEY, removedContractCifMap);
        return addedAndRemovedContractCifMaps;
    }

    public static String checkIfOptionalOrLinkageEdit( DBPDTO oldInterBankPayeeBackendDTO, DBPDTO newInterBankPayeeBackendDTO, Map<String, List<String>> addedCifsMap, Map<String, List<String>> removedCifsMap ) throws ApplicationException {
        boolean hasOptionalFieldsChanged = checkIfOptionalFieldsHaveChanged(oldInterBankPayeeBackendDTO, newInterBankPayeeBackendDTO);
        boolean hasLinkageChanged = checkIfLinkageHasChanged(addedCifsMap, removedCifsMap);
        if(hasOptionalFieldsChanged && hasLinkageChanged) {
            throw new ApplicationException(ErrorCodeEnum.ERR_12614);
        }
        if(hasLinkageChanged) {
            return PayeeConstants.LINKAGE_EDIT;
        }
        if(hasOptionalFieldsChanged) {
            return PayeeConstants.OPTIONAL_FIELD_EDIT;
        }
        return null;
    }

    private static boolean checkIfLinkageHasChanged( Map<String, List<String>> addedCifsMap, Map<String, List<String>> removedCifsMap ) {
        return !addedCifsMap.isEmpty() || !removedCifsMap.isEmpty();
    }

    private static boolean checkIfOptionalFieldsHaveChanged( DBPDTO dbpdtoOld, DBPDTO dbpdtoNew ) {
        boolean hasChanged = false;
        if(dbpdtoOld instanceof InterBankPayeeBackendDTO && dbpdtoNew instanceof InterBankPayeeBackendDTO) {
            hasChanged = checkIfInterBankPayeeOptionalFieldsHaveChanged((InterBankPayeeBackendDTO) dbpdtoOld, (InterBankPayeeBackendDTO) dbpdtoNew);
        } else if(dbpdtoOld instanceof IntraBankPayeeBackendDTO && dbpdtoNew instanceof IntraBankPayeeBackendDTO) {
            hasChanged = checkIfIntraBankPayeeOptionalFieldsHaveChanged((IntraBankPayeeBackendDTO) dbpdtoOld, (IntraBankPayeeBackendDTO) dbpdtoNew);
        } else if(dbpdtoOld instanceof InternationalPayeeBackendDTO && dbpdtoNew instanceof InternationalPayeeBackendDTO){
            hasChanged = checkIfInternationalPayeeOptionalFieldsHaveChanged((InternationalPayeeBackendDTO) dbpdtoOld, (InternationalPayeeBackendDTO) dbpdtoNew);
        }
        return hasChanged;
    }

    /*private static boolean checkIfInterBankPayeeOptionalFieldsHaveChanged(InterBankPayeeBackendDTO oldPayeeDTO, InterBankPayeeBackendDTO newPayeeDTO) {
        if((StringUtils.isNotBlank(oldPayeeDTO.getAddressLine1()) && StringUtils.isBlank(newPayeeDTO.getAddressLine1())) || (StringUtils.isBlank(oldPayeeDTO.getAddressLine1()) && StringUtils.isNotBlank(newPayeeDTO.getAddressLine1()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getAddressLine1()) && StringUtils.isNotBlank(newPayeeDTO.getAddressLine1()) && !oldPayeeDTO.getAddressLine1().equals(newPayeeDTO.getAddressLine1())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getAddressLine2()) && StringUtils.isBlank(newPayeeDTO.getAddressLine2())) || (StringUtils.isBlank(oldPayeeDTO.getAddressLine2()) && StringUtils.isNotBlank(newPayeeDTO.getAddressLine2()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getAddressLine2()) && StringUtils.isNotBlank(newPayeeDTO.getAddressLine2()) && !oldPayeeDTO.getAddressLine2().equals(newPayeeDTO.getAddressLine2())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getCity()) && StringUtils.isBlank(newPayeeDTO.getCity())) || (StringUtils.isBlank(oldPayeeDTO.getCity()) && StringUtils.isNotBlank(newPayeeDTO.getCity()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getCity()) && StringUtils.isNotBlank(newPayeeDTO.getCity()) && !oldPayeeDTO.getCity().equals(newPayeeDTO.getCity())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getCountry()) && StringUtils.isBlank(newPayeeDTO.getCountry())) || (StringUtils.isBlank(oldPayeeDTO.getCountry()) && StringUtils.isNotBlank(newPayeeDTO.getCountry()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getCountry()) && StringUtils.isNotBlank(newPayeeDTO.getCountry()) && !oldPayeeDTO.getCountry().equals(newPayeeDTO.getCountry())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getZipcode()) && StringUtils.isBlank(newPayeeDTO.getZipcode())) || (StringUtils.isBlank(oldPayeeDTO.getZipcode()) && StringUtils.isNotBlank(newPayeeDTO.getZipcode()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getZipcode()) && StringUtils.isNotBlank(newPayeeDTO.getZipcode()) && !oldPayeeDTO.getZipcode().equals(newPayeeDTO.getZipcode())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getNickName()) && StringUtils.isBlank(newPayeeDTO.getNickName())) || (StringUtils.isBlank(oldPayeeDTO.getNickName()) && StringUtils.isNotBlank(newPayeeDTO.getNickName()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getNickName()) && StringUtils.isNotBlank(newPayeeDTO.getNickName()) && !oldPayeeDTO.getNickName().equals(newPayeeDTO.getNickName())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getPhoneNumber()) && StringUtils.isBlank(newPayeeDTO.getPhoneNumber())) || (StringUtils.isBlank(oldPayeeDTO.getPhoneNumber()) && StringUtils.isNotBlank(newPayeeDTO.getPhoneNumber()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getPhoneNumber()) && StringUtils.isNotBlank(newPayeeDTO.getPhoneNumber()) && !oldPayeeDTO.getPhoneNumber().equals(newPayeeDTO.getPhoneNumber())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getEmail()) && StringUtils.isBlank(newPayeeDTO.getEmail())) || (StringUtils.isBlank(oldPayeeDTO.getEmail()) && StringUtils.isNotBlank(newPayeeDTO.getEmail()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getEmail()) && StringUtils.isNotBlank(newPayeeDTO.getEmail()) && !oldPayeeDTO.getEmail().equals(newPayeeDTO.getEmail())) {
            return true;
        }
        return false;
    }*/

    /*private static boolean checkIfIntraBankPayeeOptionalFieldsHaveChanged(IntraBankPayeeBackendDTO oldPayeeDTO, IntraBankPayeeBackendDTO newPayeeDTO) {
        if((StringUtils.isNotBlank(oldPayeeDTO.getAddressLine1()) && StringUtils.isBlank(newPayeeDTO.getAddressLine1())) || (StringUtils.isBlank(oldPayeeDTO.getAddressLine1()) && StringUtils.isNotBlank(newPayeeDTO.getAddressLine1()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getAddressLine1()) && StringUtils.isNotBlank(newPayeeDTO.getAddressLine1()) && !oldPayeeDTO.getAddressLine1().equals(newPayeeDTO.getAddressLine1())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getAddressLine2()) && StringUtils.isBlank(newPayeeDTO.getAddressLine2())) || (StringUtils.isBlank(oldPayeeDTO.getAddressLine2()) && StringUtils.isNotBlank(newPayeeDTO.getAddressLine2()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getAddressLine2()) && StringUtils.isNotBlank(newPayeeDTO.getAddressLine2()) && !oldPayeeDTO.getAddressLine2().equals(newPayeeDTO.getAddressLine2())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getCity()) && StringUtils.isBlank(newPayeeDTO.getCity())) || (StringUtils.isBlank(oldPayeeDTO.getCity()) && StringUtils.isNotBlank(newPayeeDTO.getCity()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getCity()) && StringUtils.isNotBlank(newPayeeDTO.getCity()) && !oldPayeeDTO.getCity().equals(newPayeeDTO.getCity())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getCountry()) && StringUtils.isBlank(newPayeeDTO.getCountry())) || (StringUtils.isBlank(oldPayeeDTO.getCountry()) && StringUtils.isNotBlank(newPayeeDTO.getCountry()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getCountry()) && StringUtils.isNotBlank(newPayeeDTO.getCountry()) && !oldPayeeDTO.getCountry().equals(newPayeeDTO.getCountry())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getZipcode()) && StringUtils.isBlank(newPayeeDTO.getZipcode())) || (StringUtils.isBlank(oldPayeeDTO.getZipcode()) && StringUtils.isNotBlank(newPayeeDTO.getZipcode()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getZipcode()) && StringUtils.isNotBlank(newPayeeDTO.getZipcode()) && !oldPayeeDTO.getZipcode().equals(newPayeeDTO.getZipcode())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getNickName()) && StringUtils.isBlank(newPayeeDTO.getNickName())) || (StringUtils.isBlank(oldPayeeDTO.getNickName()) && StringUtils.isNotBlank(newPayeeDTO.getNickName()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getNickName()) && StringUtils.isNotBlank(newPayeeDTO.getNickName()) && !oldPayeeDTO.getNickName().equals(newPayeeDTO.getNickName())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getPhoneNumber()) && StringUtils.isBlank(newPayeeDTO.getPhoneNumber())) || (StringUtils.isBlank(oldPayeeDTO.getPhoneNumber()) && StringUtils.isNotBlank(newPayeeDTO.getPhoneNumber()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getPhoneNumber()) && StringUtils.isNotBlank(newPayeeDTO.getPhoneNumber()) && !oldPayeeDTO.getPhoneNumber().equals(newPayeeDTO.getPhoneNumber())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getEmail()) && StringUtils.isBlank(newPayeeDTO.getEmail())) || (StringUtils.isBlank(oldPayeeDTO.getEmail()) && StringUtils.isNotBlank(newPayeeDTO.getEmail()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getEmail()) && StringUtils.isNotBlank(newPayeeDTO.getEmail()) && !oldPayeeDTO.getEmail().equals(newPayeeDTO.getEmail())) {
            return true;
        }
        return false;
    }*/
    
	private static boolean checkIfIntraBankPayeeOptionalFieldsHaveChanged(IntraBankPayeeBackendDTO oldPayeeDTO,
			IntraBankPayeeBackendDTO newPayeeDTO) {

		if (hasAttributeChanged(oldPayeeDTO.getAddressLine1(), newPayeeDTO.getAddressLine1())) {
			return true;
		}
		if (hasAttributeChanged(oldPayeeDTO.getAddressLine2(), newPayeeDTO.getAddressLine2())) {
			return true;
		}
		if (hasAttributeChanged(oldPayeeDTO.getCity(), newPayeeDTO.getCity())) {
			return true;
		}
		if (hasAttributeChanged(oldPayeeDTO.getCountry(), newPayeeDTO.getCountry())) {
			return true;
		}
		if (hasAttributeChanged(oldPayeeDTO.getZipcode(), newPayeeDTO.getZipcode())) {
			return true;
		}
		if (hasAttributeChanged(oldPayeeDTO.getNickName(), newPayeeDTO.getNickName())) {
			return true;
		}
		if (hasAttributeChanged(oldPayeeDTO.getPhoneNumber(), newPayeeDTO.getPhoneNumber())) {
			return true;
		}
		if (hasAttributeChanged(oldPayeeDTO.getEmail(), newPayeeDTO.getEmail())) {
			return true;
		}
		return false;
	}
	
	private static boolean checkIfInterBankPayeeOptionalFieldsHaveChanged(InterBankPayeeBackendDTO oldPayeeDTO,
			InterBankPayeeBackendDTO newPayeeDTO) {
		if (hasAttributeChanged(oldPayeeDTO.getAddressLine1(), newPayeeDTO.getAddressLine1())) {
			return true;
		}

		if (hasAttributeChanged(oldPayeeDTO.getAddressLine2(), newPayeeDTO.getAddressLine2())) {
			return true;
		}

		if (hasAttributeChanged(oldPayeeDTO.getCity(), newPayeeDTO.getCity())) {
			return true;
		}

		if (hasAttributeChanged(oldPayeeDTO.getCountry(), newPayeeDTO.getCountry())) {
			return true;
		}

		if (hasAttributeChanged(oldPayeeDTO.getZipcode(), newPayeeDTO.getZipcode())) {
			return true;
		}

		if (hasAttributeChanged(oldPayeeDTO.getNickName(), newPayeeDTO.getNickName())) {
			return true;
		}
		if (hasAttributeChanged(oldPayeeDTO.getPhoneNumber(), newPayeeDTO.getPhoneNumber())) {
			return true;
		}

		if (hasAttributeChanged(oldPayeeDTO.getEmail(), newPayeeDTO.getEmail())) {
			return true;
		}
		return false;
	}
	
	private static boolean checkIfInternationalPayeeOptionalFieldsHaveChanged(InternationalPayeeBackendDTO oldPayeeDTO, InternationalPayeeBackendDTO newPayeeDTO) {
        
		if (hasAttributeChanged(oldPayeeDTO.getAddressLine1(), newPayeeDTO.getAddressLine1())) {
			return true;
		}
			
		if (hasAttributeChanged(oldPayeeDTO.getAddressLine2(), newPayeeDTO.getAddressLine2())) {
			return true;
		}
		
		if (hasAttributeChanged(oldPayeeDTO.getCity(), newPayeeDTO.getCity())) {
			return true;
		}
		
		if (hasAttributeChanged(oldPayeeDTO.getCountry(), newPayeeDTO.getCountry())) {
			return true;
		}
		
		if (hasAttributeChanged(oldPayeeDTO.getZipcode(), newPayeeDTO.getZipcode())) {
			return true;
		}
		
		if (hasAttributeChanged(oldPayeeDTO.getNickName(), newPayeeDTO.getNickName())) {
			return true;
		}
		
		if (hasAttributeChanged(oldPayeeDTO.getPhoneNumber(), newPayeeDTO.getPhoneNumber())) {
			return true;
		}
		
		if (hasAttributeChanged(oldPayeeDTO.getEmail(), newPayeeDTO.getEmail())) {
			return true;
		}
        return false;
    }

    /*private static boolean checkIfInternationalPayeeOptionalFieldsHaveChanged(InternationalPayeeBackendDTO oldPayeeDTO, InternationalPayeeBackendDTO newPayeeDTO) {
        if((StringUtils.isNotBlank(oldPayeeDTO.getAddressLine1()) && StringUtils.isBlank(newPayeeDTO.getAddressLine1())) || (StringUtils.isNotBlank(oldPayeeDTO.getAddressLine1()) && StringUtils.isNotBlank(newPayeeDTO.getAddressLine1()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getAddressLine1()) && StringUtils.isNotBlank(newPayeeDTO.getAddressLine1()) && !oldPayeeDTO.getAddressLine1().equals(newPayeeDTO.getAddressLine1())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getAddressLine2()) && StringUtils.isBlank(newPayeeDTO.getAddressLine2())) || (StringUtils.isBlank(oldPayeeDTO.getAddressLine2()) && StringUtils.isNotBlank(newPayeeDTO.getAddressLine2()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getAddressLine2()) && StringUtils.isNotBlank(newPayeeDTO.getAddressLine2()) && !oldPayeeDTO.getAddressLine2().equals(newPayeeDTO.getAddressLine2())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getCity()) && StringUtils.isBlank(newPayeeDTO.getCity())) || (StringUtils.isBlank(oldPayeeDTO.getCity()) && StringUtils.isNotBlank(newPayeeDTO.getCity()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getCity()) && StringUtils.isNotBlank(newPayeeDTO.getCity()) && !oldPayeeDTO.getCity().equals(newPayeeDTO.getCity())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getCountry()) && StringUtils.isBlank(newPayeeDTO.getCountry())) || (StringUtils.isBlank(oldPayeeDTO.getCountry()) && StringUtils.isNotBlank(newPayeeDTO.getCountry()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getCountry()) && StringUtils.isNotBlank(newPayeeDTO.getCountry()) && !oldPayeeDTO.getCountry().equals(newPayeeDTO.getCountry())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getZipcode()) && StringUtils.isBlank(newPayeeDTO.getZipcode())) || (StringUtils.isBlank(oldPayeeDTO.getZipcode()) && StringUtils.isNotBlank(newPayeeDTO.getZipcode()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getZipcode()) && StringUtils.isNotBlank(newPayeeDTO.getZipcode()) && !oldPayeeDTO.getZipcode().equals(newPayeeDTO.getZipcode())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getNickName()) && StringUtils.isBlank(newPayeeDTO.getNickName())) || (StringUtils.isBlank(oldPayeeDTO.getNickName()) && StringUtils.isNotBlank(newPayeeDTO.getNickName()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getNickName()) && StringUtils.isNotBlank(newPayeeDTO.getNickName()) && !oldPayeeDTO.getNickName().equals(newPayeeDTO.getNickName())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getPhoneNumber()) && StringUtils.isBlank(newPayeeDTO.getPhoneNumber())) || (StringUtils.isBlank(oldPayeeDTO.getPhoneNumber()) && StringUtils.isNotBlank(newPayeeDTO.getPhoneNumber()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getPhoneNumber()) && StringUtils.isNotBlank(newPayeeDTO.getPhoneNumber()) && !oldPayeeDTO.getPhoneNumber().equals(newPayeeDTO.getPhoneNumber())) {
            return true;
        }
        if((StringUtils.isNotBlank(oldPayeeDTO.getEmail()) && StringUtils.isBlank(newPayeeDTO.getEmail())) || (StringUtils.isBlank(oldPayeeDTO.getEmail()) && StringUtils.isNotBlank(newPayeeDTO.getEmail()))) {
            return true;
        }
        if(StringUtils.isNotBlank(oldPayeeDTO.getEmail()) && StringUtils.isNotBlank(newPayeeDTO.getEmail()) && !oldPayeeDTO.getEmail().equals(newPayeeDTO.getEmail())) {
            return true;
        }
        return false;
    }*/

    public static void prepareAndAddAuthorizedCifsInPayload( Map<String, Object> inputParams, Map<String, List<String>> sharedCifMap ) {
        JSONArray contractCifsArray = new JSONArray();
        for ( Map.Entry<String, List<String>> contractObject : sharedCifMap.entrySet() ) {
            JSONObject contractCifObject = new JSONObject();
            // get the list of cif ids
            List<String> cifsArray = contractObject.getValue();
            // Convert to Comma Separated cif Ids
            String cifs = String.join(",", cifsArray);
            // store in contractCifObject with key as contractId and value as comma separated CIFs
            contractCifObject.put(PayeeConstants.CONTRACT_ID_KEY ,contractObject.getKey());
            contractCifObject.put(PayeeConstants.CORE_CUSTOMER_ID, cifs);
            contractCifsArray.put(contractCifObject);
        }
        inputParams.put(PayeeConstants.CIF_KEY, contractCifsArray.toString());
    }

	public static void updateLinkedUnlinkedContractCifs(JSONObject transactionResponse,
			Map<String, List<String>> addedCifs, Map<String, List<String>> removedCifs) {
		Map<String, Map<String, Boolean>> linkedOrUnlinkedContractCifMap = new HashMap<>();
		if (!addedCifs.isEmpty()) {
			for (Map.Entry<String, List<String>> entry : addedCifs.entrySet()) {
				String contractId = entry.getKey();
				List<String> cifs = entry.getValue();
				Map<String, Boolean> cifMap = new HashMap<>();
				for (String cif : cifs) {
					cifMap.put(cif, true);
				}
				linkedOrUnlinkedContractCifMap.put(contractId, cifMap);
			}
		}
		if (!removedCifs.isEmpty()) {
			for (Map.Entry<String, List<String>> entry : removedCifs.entrySet()) {
				String contractId = entry.getKey();
				List<String> cifs = entry.getValue();
				Map<String, Boolean> cifMap = new HashMap<>();
				for (String cif : cifs) {
					cifMap.put(cif, false);
				}
				linkedOrUnlinkedContractCifMap.put(contractId, cifMap);
			}
		}
		transactionResponse.put("linkedOrUnlinkedContractCifMap", linkedOrUnlinkedContractCifMap);
	}

	private static void updateLinkageActionType(Map<String, String> paramMap, JSONObject reqInfo,
			JSONObject transactionResponse) throws Exception {
		String contractId = reqInfo.optString("contractId", null);
		String cifId = reqInfo.optString("coreCustomerId", null);
		boolean linkedUnlinkedFlag = true;
		JSONObject contractcifObj = transactionResponse.has("linkedOrUnlinkedContractCifMap")
				? transactionResponse.optJSONObject("linkedOrUnlinkedContractCifMap")
				: null;
		if (contractcifObj != null) {
			Map<String, Map<String, Boolean>> assocContractCifMap = new HashMap<>();
			assocContractCifMap = new ObjectMapper().readValue(contractcifObj.toString(), HashMap.class);
			Map<String, Boolean> cifsMap = new HashMap<>();
			if (assocContractCifMap.containsKey(contractId)) {
				cifsMap = assocContractCifMap.get(contractId);
			}
			if (cifsMap.containsKey(cifId)) {
				linkedUnlinkedFlag = cifsMap.get(cifId);
			}
			paramMap.put("action", linkedUnlinkedFlag ? "Linking" : "Delinking");
		}
	}

	public static void callPushEvent(Result result, DataControllerRequest request, DataControllerResponse response,
			String actionType, JSONObject transactionResponse) {
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			Map<String, String> custIdUserNameMap = new HashMap<String, String>();
			Map<String, String> initiatorParamMap = null;
			Map<String, String> approverParamMap = null;
			transactionResponse.put("actionType", actionType);
			JSONArray requestsInfoArray = prepareMapForEventDispatcher(paramMap, transactionResponse, request);
			String transactionStatus =paramMap.get("transactionStatus");
			if(transactionStatus.equalsIgnoreCase("Sent") || transactionStatus.equalsIgnoreCase("Approved") 
					|| (transactionStatus.equalsIgnoreCase("Pending") && !actionType.equals("EDIT_LINKAGE"))) {
				// calling event dispatcher for sending notification to Initiator and their associated cif's
				if(paramMap.get("coreCustomerIds") != null)
					custIdUserNameMap = fetchUserNameByCoreCustomerId(paramMap.get("coreCustomerIds"),request);
				for(String key : custIdUserNameMap.keySet()) {
					initiatorParamMap = new HashMap<String, String>(paramMap);
					initiatorParamMap.put("userName", custIdUserNameMap.get(key));
					AsyncPushEvent(result, request, response, initiatorParamMap);
				}
			}
			if (paramMap.get("transactionStatus").equalsIgnoreCase("Pending")) {
				UserBackendDelegate userBackendDelegate = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(UserBackendDelegate.class);
				if (!requestsInfoArray.isEmpty()) {
					for (Object req : requestsInfoArray) {
						initiatorParamMap = new HashMap<String, String>(paramMap);
						JSONObject requestInfoObj = (JSONObject) req;
						String cifId = requestInfoObj.optString("coreCustomerId", null);
						String approverIds = requestInfoObj.optString("approverIds", null);
						if (actionType.equals("EDIT_LINKAGE")) {
							updateLinkageActionType(initiatorParamMap, requestInfoObj, transactionResponse);
							initiatorParamMap.put("userType", "INITIATOR");
							if(custIdUserNameMap.containsKey(cifId))
								initiatorParamMap.put("userName", custIdUserNameMap.get(cifId));
							// calling event dispatcher to send notification to initiator with link/delink actions opted
							AsyncPushEvent(result, request, response, initiatorParamMap);
						}
						if (approverIds != null) {
							String[] approverIdArr = approverIds.split(",");
							String userName = "";
							for (String approverId : approverIdArr) {
								approverParamMap = new HashMap<String, String>(initiatorParamMap);
								approverParamMap.put("userType", "APPROVER");
								approverParamMap.put("approverCustId", approverId);
								userName = userBackendDelegate.fetchUsernameFromCustomerId(approverId, null);
								userName = userName.replace("\"", "");
								if (!custIdUserNameMap.containsValue(userName)) {
									approverParamMap.put("userName", userName);
									// calling event dispatcher for sending notification to list of Approvers
									AsyncPushEvent(result, request, response, approverParamMap);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("callPushEvent: Error occured while processing payload for Eventdispatcher: ", e).log();
		}
	}
	
	public static Map<String, String> fetchUserNameByCoreCustomerId(String cifsList, DataControllerRequest dcRequest) {
		
		ExternalPayeeBackendDelegate externalPayeeBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ExternalPayeeBackendDelegate.class);
		Map<String, String> custIdUserNameMap = new HashMap<String, String>();
		Map<String, Object> serviceReqMap = new HashMap<String, Object>();
		String[] cifsArr = cifsList.split(",");
		List<String> cifList = new ArrayList<String>();
		for(int i = 0;i<cifsArr.length;i++) {
			cifList.add("BackendId" + DBPUtilitiesConstants.EQUAL + cifsArr[i]);
		}
		String cifIds = null;
		if(cifList == null || cifList.isEmpty()) {
			custIdUserNameMap = null;
		}else {
			cifIds = String.join(",", cifList);
		if (cifIds != null) {
			serviceReqMap.put(DBPUtilitiesConstants.FILTER, cifIds);
			serviceReqMap.put("loop_count", Integer.toString(cifList.size()));
			custIdUserNameMap = externalPayeeBackendDelegate.fetchUserNameByCoreCustomerId(serviceReqMap, null, dcRequest);
		}
		}
		return custIdUserNameMap;
	}
	
	private static JSONArray prepareMapForEventDispatcher(Map<String, String> paramMap, JSONObject transactionResponse,
			DataControllerRequest request) throws Exception{
		
		JSONArray requestsInfoArr = new JSONArray();
		String customerId = "", transactionStatus = "", className = "", initiatorUserName = "", actionType = "";
		Map<String, List<String>> assocCifMap = new HashMap<String, List<String>>();
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		customerId = CustomerSession.getCustomerId(customer);
		initiatorUserName = CustomerSession.getCustomerCompleteName(customer);
		transactionStatus = transactionResponse.has("status") ? transactionResponse.optString("status") : "";
		className = transactionResponse.has("className") ? transactionResponse.optString("className") : "";
		actionType = transactionResponse.has("actionType") ? transactionResponse.optString("actionType") : "";
		
		paramMap.put("transactionStatus", transactionStatus);
		paramMap.put("className", className);
		paramMap.put("userType", "INITIATOR");
		paramMap.put("initiatorCustId", customerId);
		paramMap.put("initiatorUserName", initiatorUserName);
		paramMap.put("actionType", actionType);
		
		Object objt = transactionResponse.has("associatedCifMap") ? transactionResponse.opt("associatedCifMap") : null;
		if(actionType.equalsIgnoreCase("DELETE")) {
			Set<String> cifs = null;
			cifs = new ObjectMapper().readValue(objt.toString(), Set.class);
			if(!cifs.isEmpty())
				paramMap.put("coreCustomerIds", String.join(",", cifs));
			else
				paramMap.put("coreCustomerIds", null);
		} else {		
		assocCifMap = new ObjectMapper().readValue(objt.toString(), HashMap.class);
		List<String> custIDList = new ArrayList<String>();
		for (Map.Entry<String, List<String>> contractCif : assocCifMap.entrySet()) {
			List<String> coreCustomerIds = contractCif.getValue();
			custIDList.addAll(coreCustomerIds);
		}
		if(!custIDList.isEmpty())
			paramMap.put("coreCustomerIds", String.join(",", custIDList));
		else
			paramMap.put("coreCustomerIds", null);
		}
		Set<String> requestIds = new HashSet<>();
		JSONArray requestsArray = transactionResponse.has("Requests") ? transactionResponse.optJSONArray("Requests")
				: null;
		if (!requestsArray.isEmpty()) {
			for (Object obj : requestsArray) {
				JSONObject requestObj = (JSONObject) obj;
				String status = requestObj.optString("status", null);
				if (status.equalsIgnoreCase("Pending")) {
					String requestId = requestObj.optString("requestId", null);
					requestIds.add(requestId);
				}
			}
			ApprovalRequestBackendDelegate approvalRequestBackendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(ApprovalRequestBackendDelegate.class);
			requestsInfoArr = approvalRequestBackendDelegate.fetchRequestsWithApprovalMatrixInfoFromDBXBD(requestIds,
					false, null, false);
		}
		return requestsInfoArr;
	}

	public static void AsyncPushEvent(Result result, DataControllerRequest request, DataControllerResponse response,
			Map<String, String> paramMap) {

		Callable<Result> callable = new Callable<Result>() {
			@Override
			public Result call() {
				String enableEvents = EnvironmentConfigurationsHandler.getValue("ENABLE_EVENTS", request);
				if (enableEvents == null) {
					enableEvents = "true";
				}
				if (enableEvents.equals("true")) {
					PushEvent(result, request, response, paramMap);
				}
				return new Result();
			}
		};
		try {
			ThreadExecutor.getExecutor(request).execute(callable);
		} catch (InterruptedException e) {
			alert.prepareError("Caught exception while Executing Thread :", e.getMessage()).log();
			Thread.currentThread().interrupt();
		}
	}

	private static void PushEvent(Result result, DataControllerRequest request, DataControllerResponse response,
			Map<String, String> paramMap) {

		String actionType = "", transactionStatus = "", className = "", userType = "", userName = "",
				actionId = "", initiatorUserName = "";
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		if (!paramMap.isEmpty()) {
			actionType = paramMap.containsKey("actionType") ? paramMap.get("actionType") : "";
			transactionStatus = paramMap.containsKey("transactionStatus") ? paramMap.get("transactionStatus") : "";
			className = paramMap.containsKey("className") ? paramMap.get("className") : "";
			userType = paramMap.containsKey("userType") ? paramMap.get("userType") : "";
			if (!paramMap.containsKey("userName") && customer != null) {
				userName = CustomerSession.getCustomerName(customer);
			} else
				userName = paramMap.get("userName");
			initiatorUserName = paramMap.containsKey("initiatorUserName") ? paramMap.get("initiatorUserName") : "";
			actionId = paramMap.containsKey("action") ? paramMap.get("action") : "";
		}
		String eventType = "BENEFICIARY";
		String eventSubType = "";
		String StatusId = "SID_EVENT_SUCCESS";
		JsonObject customParams = new JsonObject();
		customParams.addProperty("initiatorUsername", initiatorUserName);
		if (transactionStatus.equalsIgnoreCase("Pending")) {
			if (userType.equalsIgnoreCase("INITIATOR")) {
				switch (actionType) {
				case "CREATE":
					eventSubType = "BENEFICIARY_CREATE_PENDING_TO_USER";
					break;
				case "EDIT_OPTIONAL":
					eventSubType = "BENEFICIARY_EDIT_PENDING_TO_USER";
					break;
				case "EDIT_LINKAGE":
					customParams.addProperty("action", actionId);
					eventSubType = "BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER";
					break;
				case "DELETE":
					eventSubType = "BENEFICIARY_DELETE_PENDING_TO_USER";
					break;
				}
			} else if (userType.equalsIgnoreCase("APPROVER")) {
				switch (actionType) {
				case "CREATE":
					eventSubType = "BENEFICIARY_CREATE_PENDING_TO_APPROVER";
					break;
				case "EDIT_OPTIONAL":
					eventSubType = "BENEFICIARY_EDIT_PENDING_TO_APPROVER";
					break;
				case "EDIT_LINKAGE":
					customParams.addProperty("action", actionId);
					eventSubType = "BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER";
					break;
				case "DELETE":
					eventSubType = "BENEFICIARY_DELETE_PENDING_TO_APPROVER";
					break;
				}
			}
		} else if (transactionStatus.equalsIgnoreCase("Sent") || transactionStatus.equalsIgnoreCase("Approved")) {
			switch (actionType) {
			case "CREATE":
				eventSubType = "BENEFICIARY_CREATED";
				break;
			case "EDIT_OPTIONAL":
				eventSubType = "BENEFICIARY_EDITED";
				break;
			case "EDIT_LINKAGE":
				eventSubType = "BENEFICIARY_LINKAGE_EDITED";
				break;
			case "DELETE":
				eventSubType = "BENEFICIARY_DELETED";
				break;
			}
		} else if (transactionStatus.equalsIgnoreCase("Rejected")) {
			eventSubType = "BENEFICIARY_REQUEST_REJECTED_TO_USER";
			customParams.addProperty("action", actionId);
			customParams.addProperty("approverUsername", paramMap.get("approverUsername"));
		} else if (transactionStatus.equalsIgnoreCase("Withdrawn")) {
			eventSubType = "BENEFICIARY_REQUEST_WITHDRAWN_TO_USER";
			customParams.addProperty("action", actionId);
			customParams.addProperty("comment", paramMap.containsKey("comment") ? paramMap.get("comment") : "");
		}
		customParams.addProperty("BankName", "Infinity Bank");
		EventsDispatcher.dispatch(request, response, eventType, eventSubType, className, StatusId, null, userName,
				customParams);
	}
	
	    private static boolean hasAttributeChanged(String oldValue, String newValue) {
	        // Check if the old and new addressLine1 are both null or empty
	        if ((newValue == null || newValue.isEmpty()) && (oldValue == null || oldValue.isEmpty())) {
	            return false; // No change in addressLine1
	        }
	        if (!(newValue == null || newValue.isEmpty())) {
	        	if (oldValue == null || oldValue.isEmpty())
	        		return true;
	        	else
	        		return !oldValue.equals(newValue);
	        }      
	        return false;
	        
	    }
}