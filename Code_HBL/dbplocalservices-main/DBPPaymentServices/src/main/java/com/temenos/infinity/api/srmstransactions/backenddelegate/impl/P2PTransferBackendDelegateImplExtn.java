package com.temenos.infinity.api.srmstransactions.backenddelegate.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.P2PTransactionBackendDelegate;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionDTO;
import com.temenos.infinity.api.srmstransactions.config.SRMSTransactionsHelper;
import com.temenos.infinity.api.srmstransactions.config.TransformSRMSRequest;
import com.temenos.infinity.api.srmstransactions.constants.SRMSTransactionsConstants;
import com.temenos.infinity.api.srmstransactions.dto.SRMSP2PTransferDTO;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class P2PTransferBackendDelegateImplExtn implements P2PTransactionBackendDelegate
        , SRMSTransactionsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public P2PTransactionDTO createTransactionWithoutApproval(P2PTransactionBackendDTO p2pTransactionBackendDTO,
            DataControllerRequest request) {

        P2PTransactionDTO p2pTransactionDTO = null;
        p2pTransactionBackendDTO.setStatus(TRANSACTION_STATUS_SUCCESSFUL);
        Map<String, Object> requestParameters;
        // Convert DBPDto to SRMS DTO to filter out the unwanted fields to be
        // sent to SRMS
        SRMSP2PTransferDTO P2PDTOInput = new SRMSP2PTransferDTO().convert(p2pTransactionBackendDTO);
        try {

            requestParameters = JSONUtils.parseAsMap(new JSONObject(P2PDTOInput).toString(), String.class,
                    Object.class);
        } catch (IOException e) {
            alert.prepareError("Error occured while fetching the input params: ", e).log();
            return null;
        }

        TransformSRMSRequest transformObject = TransformSRMSRequest.getInstance();
        Map<String, Object> srmsInputParams = transformObject.P2PTransfer(requestParameters, request);

        try {
            diagnostic.prepareDebug("Inter bank Account Create Order Request :" + srmsInputParams.toString()).log();
            String response = SRMSTransactionsHelper.createOrder(srmsInputParams, request);
            diagnostic.prepareDebug("Inter bank Account Create Order Response :" + response).log();
            p2pTransactionDTO = JSONUtils.parse(response, P2PTransactionDTO.class);
        } catch (JSONException e) {
            alert.prepareError("Failed to create p2p transaction: ", e).log();
            return null;
        } catch (Exception e) {
            alert.prepareError("Caught exception at create p2p transaction: ", e).log();
            return null;
        }

        return p2pTransactionDTO;
    }
    
    @Override
    public P2PTransactionDTO validateTransaction(P2PTransactionBackendDTO input, DataControllerRequest request) {
        P2PTransactionDTO output = new P2PTransactionDTO();
        try {
            output = JSONUtils.parse(new JSONObject(input).toString(), P2PTransactionDTO.class);
        } catch (IOException e) {
            alert.prepareError("Caught exception at converting backenddto to dbxdto. ", e).log();
        }
        return output;
    }
    
    @Override
    public  P2PTransactionDTO createPendingTransaction(P2PTransactionBackendDTO backendObj, DataControllerRequest request) {

        P2PTransactionDTO p2pTransferObj = createTransactionWithoutApproval(backendObj, request);

        return p2pTransferObj;
    }

    @Override
    public P2PTransactionDTO approveTransaction(String arg0, DataControllerRequest arg1, String arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public P2PTransactionDTO cancelTransactionOccurrence(String arg0, DataControllerRequest arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public P2PTransactionDTO deleteTransaction(String arg0, String arg1, DataControllerRequest arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public P2PTransactionDTO editTransactionWithApproval(P2PTransactionBackendDTO arg0, DataControllerRequest arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public P2PTransactionDTO editTransactionWithoutApproval(P2PTransactionBackendDTO arg0, DataControllerRequest arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> arg0, DataControllerRequest arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public P2PTransactionDTO fetchTransactionById(String arg0, DataControllerRequest arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public P2PTransactionDTO rejectTransaction(String arg0, String arg1, DataControllerRequest arg2, String arg3) {
     // TODO Auto-generated method stub
        return null;
    }

    @Override
    public P2PTransactionDTO withdrawTransaction(String arg0, String arg1, DataControllerRequest arg2, String arg3) {
     // TODO Auto-generated method stub
        return null;
    }

}
