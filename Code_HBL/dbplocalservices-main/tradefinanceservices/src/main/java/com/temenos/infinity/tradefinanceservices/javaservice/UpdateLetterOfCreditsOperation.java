package com.temenos.infinity.tradefinanceservices.javaservice;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import  com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
//import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.CreateLetterOfCreditsResource;

public class UpdateLetterOfCreditsOperation implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
            CreateLetterOfCreditsResource letterOfCreditsResource = DBPAPIAbstractFactoryImpl
                    .getResource(CreateLetterOfCreditsResource.class);
            LetterOfCreditsDTO letterOfCredit = constructPayload(request);                  
            Result result = letterOfCreditsResource.createLetterOfCredits(letterOfCredit, request);
            return result;
        } catch (Exception e) { 
            alert.prepareError("Unable to update Letter Of Credtis from OMS: "+e).log();
            return ErrorCodeEnum.ERRTF_29047.setErrorCode(new Result()); 
        }

	}
	
	public static LetterOfCreditsDTO constructPayload(DataControllerRequest request){
		LetterOfCreditsDTO letterOfCredit = new LetterOfCreditsDTO();
	    //Get input params from request object
	    String lcReferenceNo = request.getParameter("lcReferenceNo") != null ? request.getParameter("lcReferenceNo") : "";
	    String flowType = request.getParameter("flowType") != null ? request.getParameter("flowType") : "";	    
	    String lcAmount= StringUtils.isNotBlank(request.getParameter("lcAmount")) ? request.getParameter("lcAmount") : "";  
	    String lcCurrency= request.getParameter("lcCurrency") != null ? request.getParameter("lcCurrency") : "";
	    String tolerancePercentage = request.getParameter("tolerancePercentage") != null ? request.getParameter("tolerancePercentage") : "";
	    String paymentTerms= request.getParameter("paymentTerms") != null ? request.getParameter("paymentTerms") : "";  
	    String availableWith1= request.getParameter("availableWith1") != null ? request.getParameter("availableWith1") : "";  
	    String issueDate= request.getParameter("issueDate") != null ? request.getParameter("issueDate") : "";  
	    String expiryDate= request.getParameter("expiryDate") != null ? request.getParameter("expiryDate") : "";  
	    String expiryPlace= request.getParameter("expiryPlace") != null ? request.getParameter("expiryPlace") : "";  
	    String beneficiaryName= request.getParameter("beneficiaryName") != null ? request.getParameter("beneficiaryName") : "";  
	    String beneficiaryAddressLine1= request.getParameter("beneficiaryAddressLine1") != null ? request.getParameter("beneficiaryAddressLine1") : "";  
	    String beneficiaryPostCode= request.getParameter("beneficiaryPostCode") != null ? request.getParameter("beneficiaryPostCode") : "";  
	    String beneficiaryCountry= request.getParameter("beneficiaryCountry") != null ? request.getParameter("beneficiaryCountry") : "";  
	    String beneficiaryCity= request.getParameter("beneficiaryCity") != null ? request.getParameter("beneficiaryCity") : "";  
	    String beneficiaryState= request.getParameter("beneficiaryState") != null ? request.getParameter("beneficiaryState") : "";  
	    String beneficiaryBank= request.getParameter("beneficiaryBank") != null ? request.getParameter("beneficiaryBank") : "";  
	    String beneficiaryBankAdressLine1= request.getParameter("beneficiaryBankAdressLine1") != null ? request.getParameter("beneficiaryBankAdressLine1") : "";  
	    String beneficiaryBankPostCode= request.getParameter("beneficiaryBankPostCode") != null ? request.getParameter("beneficiaryBankPostCode") : "";  
	    String beneficiaryBankCountry= request.getParameter("beneficiaryBankCountry") != null ? request.getParameter("beneficiaryBankCountry") : "";  
	    String beneficiaryBankCity= request.getParameter("beneficiaryBankCity") != null ? request.getParameter("beneficiaryBankCity") : "";  
	    String beneficiaryBankState= request.getParameter("beneficiaryBankState") != null ? request.getParameter("beneficiaryBankState") : "";  
	    String incoTerms= request.getParameter("incoTerms") != null ? request.getParameter("incoTerms") : "";  
	    String documentCharges= request.getParameter("documentCharges") != null ? request.getParameter("documentCharges") : "";  
	    String confirmationInstruction= request.getParameter("confirmationInstruction") != null ? request.getParameter("confirmationInstruction") : "";  
	    String transferable= request.getParameter("transferable") != null ? request.getParameter("confirmationInstruction") : "";   
	    String standByLC= request.getParameter("standByLC") != null ? request.getParameter("confirmationInstruction") : "";   
	    
	    //non mandatory fields
	    String maximumCreditAmount = StringUtils.isNotBlank(request.getParameter("maximumCreditAmount")) ? request.getParameter("maximumCreditAmount") : "";
	    String additionalAmountPayable = request.getParameter("additionalAmountPayable") != null ? request.getParameter("additionalAmountPayable") : "";
	    String availableWith2 = request.getParameter("availableWith2") != null ? request.getParameter("availableWith2") : "";
	    String availableWith3 = request.getParameter("availableWith3") != null ? request.getParameter("availableWith3") : "";
	    String availableWith4 = request.getParameter("availableWith4") != null ? request.getParameter("availableWith4") : "";
	    String chargesAccount = request.getParameter("chargesAccount") != null ? request.getParameter("chargesAccount") : "";
	    String commisionAccount = request.getParameter("commisionAccount") != null ? request.getParameter("commisionAccount") : "";
	    String marginAccount = request.getParameter("marginAccount") != null ? request.getParameter("marginAccount") : "";
	    String messageToBank = request.getParameter("messageToBank") != null ? request.getParameter("messageToBank") : "";
	    String beneficiaryAddressLine2 = request.getParameter("beneficiaryAddressLine2") != null ? request.getParameter("beneficiaryAddressLine2") : "";
        String beneficiaryBankAdressLine2 = request.getParameter("beneficiaryBankAdressLine2") != null ? request.getParameter("beneficiaryBankAdressLine2") : "";
        String placeOfTakingIncharge = request.getParameter("placeOfTakingIncharge") != null ? request.getParameter("placeOfTakingIncharge") : "";
        String portOfLoading = request.getParameter("portOfLoading") != null ? request.getParameter("portOfLoading") : "";
        String portOfDischarge = request.getParameter("portOfDischarge") != null ? request.getParameter("portOfDischarge") : "";
        String placeOfFinalDelivery = request.getParameter("placeOfFinalDelivery") != null ? request.getParameter("placeOfFinalDelivery") : "";
        String latestShippingDate = request.getParameter("latestShippingDate") != null ? request.getParameter("latestShippingDate") : "";
        String presentationPeriod = request.getParameter("presentationPeriod") != null ? request.getParameter("presentationPeriod") : "";
        String transshipment = request.getParameter("transshipment") != null ? request.getParameter("transshipment") : "";
        String partialShipments = request.getParameter("partialShipments") != null ? request.getParameter("partialShipments") : "";
        String modeOfShipment = request.getParameter("modeOfShipment") != null ? request.getParameter("modeOfShipment") : "";
        String descriptionOfGoods = request.getParameter("descriptionOfGoods") != null ? request.getParameter("descriptionOfGoods") : "";
        String documentsRequired = request.getParameter("documentsRequired") != null ? request.getParameter("documentsRequired") : "";
        String additionalConditionsCode = request.getParameter("additionalConditionsCode") != null ? request.getParameter("additionalConditionsCode") : "";
        String otherAdditionalConditions = request.getParameter("otherAdditionalConditions") != null ? request.getParameter("otherAdditionalConditions") : "";
        String supportDocuments = request.getParameter("supportDocuments") != null ? request.getParameter("supportDocuments") : "";
        String fileToUpload = request.getParameter("fileToUpload") != null ? request.getParameter("fileToUpload") : "";
        String isDraft = request.getParameter("isDraft") != null ? request.getParameter("isDraft") : "";
        String additionalPayableCurrency = request.getParameter("additionalPayableCurrency") != null ? request.getParameter("additionalPayableCurrency") : "";
        String srmsReqOrderID = request.getParameter("srmsReqOrderID") != null ? request.getParameter("srmsReqOrderID") : "";
        String screenNumber = request.getParameter("screenNumber") != null ? request.getParameter("screenNumber") : "";
        letterOfCredit.setScreenNumber(screenNumber);
        letterOfCredit.setSrmsReqOrderID(srmsReqOrderID);
	    letterOfCredit.setLcReferenceNo(lcReferenceNo);
	    letterOfCredit.setFlowType(flowType);
	    letterOfCredit.setLcAmount(Double.parseDouble(lcAmount));
	    if(lcAmount!= null && lcAmount.trim()!="")
	    letterOfCredit.setLcCurrency(lcCurrency);
	    letterOfCredit.setTolerancePercentage(tolerancePercentage);
	    letterOfCredit.setPaymentTerms(paymentTerms);
	    letterOfCredit.setAvailableWith1(availableWith1);
	    letterOfCredit.setAvailableWith2(availableWith2);
	    letterOfCredit.setAvailableWith3(availableWith3);
	    letterOfCredit.setAvailableWith4(availableWith4);
	    letterOfCredit.setIssueDate(issueDate);
	    letterOfCredit.setExpiryDate(expiryDate);
	    letterOfCredit.setExpiryPlace(expiryPlace);
	    letterOfCredit.setBeneficiaryName(beneficiaryName);
	    letterOfCredit.setBeneficiaryAddressLine1(beneficiaryAddressLine1);
	    letterOfCredit.setBeneficiaryAddressLine2(beneficiaryAddressLine2);
	    letterOfCredit.setBeneficiaryPostCode(beneficiaryPostCode);
	    letterOfCredit.setBeneficiaryCountry(beneficiaryCountry);
	    letterOfCredit.setBeneficiaryCity(beneficiaryCity);
	    letterOfCredit.setBeneficiaryState(beneficiaryState);
	    letterOfCredit.setBeneficiaryBank(beneficiaryBank);
	    letterOfCredit.setBeneficiaryBankAdressLine1(beneficiaryBankAdressLine1);
	    letterOfCredit.setBeneficiaryBankAdressLine2(beneficiaryBankAdressLine2);
	    letterOfCredit.setBeneficiaryBankPostCode(beneficiaryBankPostCode);
	    letterOfCredit.setBeneficiaryBankCountry(beneficiaryBankCountry);
	    letterOfCredit.setBeneficiaryBankCity(beneficiaryBankCity);
	    letterOfCredit.setBeneficiaryBankState(beneficiaryBankState);
	    letterOfCredit.setIncoTerms(incoTerms);
	    letterOfCredit.setDocumentCharges(documentCharges);
	    letterOfCredit.setConfirmationInstruction(confirmationInstruction);
	    letterOfCredit.setTransferable(transferable);
	    letterOfCredit.setStandByLC(standByLC);
	    
		if (maximumCreditAmount != null && maximumCreditAmount.trim() != "")
			letterOfCredit.setMaximumCreditAmount(Double.parseDouble(maximumCreditAmount));
	    letterOfCredit.setAdditionalAmountPayable(additionalAmountPayable);
	    letterOfCredit.setChargesAccount(chargesAccount);
	    letterOfCredit.setCommisionAccount(commisionAccount);
	    letterOfCredit.setMarginAccount(marginAccount);
	    letterOfCredit.setMessageToBank(messageToBank);
        letterOfCredit.setPlaceOfTakingIncharge(placeOfTakingIncharge);
        letterOfCredit.setPortOfLoading(portOfLoading);
        letterOfCredit.setPortOfDischarge(portOfDischarge);
        letterOfCredit.setPlaceOfFinalDelivery(placeOfFinalDelivery);
        letterOfCredit.setLatestShippingDate(latestShippingDate);
        letterOfCredit.setPresentationPeriod(presentationPeriod);
        letterOfCredit.setTransshipment(transshipment);
        letterOfCredit.setPartialShipments(partialShipments);
        letterOfCredit.setModeOfShipment(modeOfShipment);
        letterOfCredit.setDescriptionOfGoods(descriptionOfGoods);
        letterOfCredit.setDocumentsRequired(documentsRequired);
        letterOfCredit.setAdditionalConditionsCode(additionalConditionsCode);
        letterOfCredit.setOtherAdditionalConditions(otherAdditionalConditions);
        letterOfCredit.setSupportDocuments(supportDocuments);
        letterOfCredit.setFileToUpload(fileToUpload);
        letterOfCredit.setIsDraft(isDraft);
        letterOfCredit.setAdditionalPayableCurrency(additionalPayableCurrency);
	    
	    return letterOfCredit;
	}
}
