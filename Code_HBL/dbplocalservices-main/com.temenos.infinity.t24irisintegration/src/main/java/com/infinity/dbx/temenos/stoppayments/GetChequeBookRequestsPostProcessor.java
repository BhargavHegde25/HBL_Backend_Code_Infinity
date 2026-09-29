package com.infinity.dbx.temenos.stoppayments;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetChequeBookRequestsPostProcessor extends BasePostProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    String sortKey = null;

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        Dataset ChequeBookRequest = result.getDatasetById(StopPaymentConstants.PARAM_CHEQUE_BOOK_RESULT);
        List<Record> ChequeBookRequests = ChequeBookRequest != null ? ChequeBookRequest.getAllRecords() : null;
        if (ChequeBookRequests.isEmpty()) {
            alert.prepareError("ChequeBookRequests empty return result " + result.getAllParams()).log();
            return TemenosUtils.getEmptyResult(StopPaymentConstants.PARAM_CHEQUE_BOOK_RESULT);
        }

        for (Record ChequeBook : ChequeBookRequests) {
            Dataset chargeDetail = ChequeBook.getDatasetById(StopPaymentConstants.PARAM_CHARGE_DETAILS);
            List<Record> chargeDetails = chargeDetail != null ? chargeDetail.getAllRecords() : null;
            Dataset Notes = ChequeBook.getDatasetById(StopPaymentConstants.PARAM_NOTES);
            List<Record> NoteDetails = Notes != null ? Notes.getAllRecords() : null;
            String charges = ChequeBook.getParamValueByName(StopPaymentConstants.PARAM_CHARGES);
            Double Amount = 0.0;
            if (chargeDetails != null && !chargeDetails.isEmpty()) {
                for (Record chargeRecord : chargeDetails) {
                    String chargeAmount = CommonUtils.getParamValue(chargeRecord,
                            StopPaymentConstants.PARAM_CHARGE_AMOUNT);
                    Amount = Amount + Double.parseDouble(chargeAmount);
                }
                if (StringUtils.isNotBlank(charges)) {
                    Amount = Amount + Double.parseDouble(charges);
                }
                ChequeBook.addStringParam(StopPaymentConstants.PARAM_FEE, Double.toString(Amount));
            } else {
                ChequeBook.addStringParam(StopPaymentConstants.PARAM_FEE, Double.toString(Amount));
            }
            if (NoteDetails != null && !NoteDetails.isEmpty()) {
                for (Record noteRecord : NoteDetails) {
                    String note = CommonUtils.getParamValue(noteRecord, StopPaymentConstants.PARAM_NOTE);
                    if (StringUtils.isNotBlank(note))
                        ChequeBook.addStringParam(StopPaymentConstants.PARAM_NOTE, note);
                }

            }
        }
        return result;

    }
}
