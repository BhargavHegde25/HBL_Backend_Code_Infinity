package com.temenos.dbx.product.transactionservices.dto;
 
import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Test;

import com.dbp.core.util.JSONUtils;

public class InterBankFundTransferBackendDTOTest {

	@Test
	public void testInvoke() throws Exception {
		String interBankFTBackendDTOJsonPath = new File(getClass().getClassLoader().getResource("InterBankFTBackendDTOJson.json").getFile()).getPath();
		JSONObject interBankFTBackendDTOJson = new JSONObject(new String(Files.readAllBytes(Paths.get(interBankFTBackendDTOJsonPath))));
		InterBankFundTransferBackendDTO interBankFTBackendDTO = JSONUtils
				.parse(interBankFTBackendDTOJson.toString(), InterBankFundTransferBackendDTO.class);
		assertEquals("ATBLZ", interBankFTBackendDTO.getPurposeCode());
        assertEquals("CIC", interBankFTBackendDTO.getClearingIdentifierCode());
	}
	
	@Test
	public void testInvokeUpdate() throws Exception {
		String interBankFTDTOJsonPath = new File(getClass().getClassLoader().getResource("InterBankFTBackendDTOJson.json").getFile()).getPath();
		JSONObject interBankFTDTOJson = new JSONObject(new String(Files.readAllBytes(Paths.get(interBankFTDTOJsonPath))));
		InterBankFundTransferBackendDTO InterBankFundTransferBackendDTO = JSONUtils.parse(interBankFTDTOJson.toString(), InterBankFundTransferBackendDTO.class);
		InterBankFundTransferDTO InterBankFundTransferDTO = JSONUtils.parse(interBankFTDTOJson.toString(), InterBankFundTransferDTO.class);
		InterBankFundTransferDTO.setPurposeCode("AQWS");
		InterBankFundTransferDTO.setClearingIdentifierCode("123");
		InterBankFundTransferBackendDTO = InterBankFundTransferBackendDTO.convert(InterBankFundTransferDTO);
        assertEquals("AT088888800000334456", InterBankFundTransferBackendDTO.getIban());
        assertEquals("BARCGB22", InterBankFundTransferBackendDTO.getBicCode());
        assertEquals("BANK OF CALIFORNIA", InterBankFundTransferBackendDTO.getBankName());
        assertEquals("601613", InterBankFundTransferBackendDTO.getBankId());
        assertEquals("USD", InterBankFundTransferBackendDTO.getFeeCurrency());
        assertEquals("Chris", InterBankFundTransferBackendDTO.getBeneficiaryName());
        assertEquals("Instant", InterBankFundTransferBackendDTO.getPaymentType());
        assertEquals("5", InterBankFundTransferBackendDTO.getFeeAmount());
        assertEquals("Home", InterBankFundTransferBackendDTO.getBeneficiaryAddressNickName());
        assertEquals("No5 Fernandes", InterBankFundTransferBackendDTO.getBeneficiaryAddressLine1());
        assertEquals("City", InterBankFundTransferBackendDTO.getBeneficiaryCity());
        assertEquals("112233", InterBankFundTransferBackendDTO.getBeneficiaryZipcode());
        assertEquals("IN", InterBankFundTransferBackendDTO.getBeneficiarycountry());
        assertEquals("WONAUS44", InterBankFundTransferBackendDTO.getIntermediaryBicCode());
        assertEquals("123123", InterBankFundTransferBackendDTO.getClearingCode());
        assertEquals("BANK OF AMERICA NEW YORK", InterBankFundTransferBackendDTO.getBeneficiaryBankName());
        assertEquals("Addr line2", InterBankFundTransferBackendDTO.getBeneficiaryAddressLine2());
        assertEquals("123456789", InterBankFundTransferBackendDTO.getBeneficiaryPhone());
        assertEquals("sample@temenos.com", InterBankFundTransferBackendDTO.getBeneficiaryEmail());
        assertEquals("TN", InterBankFundTransferBackendDTO.getBeneficiaryState());
        assertEquals("E2E", InterBankFundTransferBackendDTO.getE2eReference());
        assertEquals("INST", InterBankFundTransferBackendDTO.getLocalInstrumentProprietary());
        assertEquals("III", InterBankFundTransferBackendDTO.getServiceLevelProprietary());
        assertEquals("AQWS", InterBankFundTransferBackendDTO.getPurposeCode());
        assertEquals("123", InterBankFundTransferBackendDTO.getClearingIdentifierCode());
        assertEquals("Failure", InterBankFundTransferBackendDTO.getPayeeVerificationStatus());
        assertEquals("Opted out COP", InterBankFundTransferBackendDTO.getPayeeVerificationErrMsg());
	}
}
