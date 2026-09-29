package com.temenos.dbx.product.transactionservices.dto;
 
import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Test;

import com.dbp.core.util.JSONUtils;

public class InternationalFundTransferBackendDTOTest {

	@Test
	public void testInvoke() throws Exception {
		String internationalFTDTOJsonPath = new File(getClass().getClassLoader().getResource("InternationalFTBackendDTOJson.json").getFile()).getPath();
		JSONObject internationalFTDTOJson = new JSONObject(new String(Files.readAllBytes(Paths.get(internationalFTDTOJsonPath))));
		InternationalFundTransferBackendDTO internationalFTDTO = JSONUtils
				.parse(internationalFTDTOJson.toString(), InternationalFundTransferBackendDTO.class);
		assertEquals("ATBLZ", internationalFTDTO.getPurposeCode());
        assertEquals("1199", internationalFTDTO.getClearingIdentifierCode());
        assertEquals("STREET UKILOP", internationalFTDTO.getStreetName());
        assertEquals("TT town", internationalFTDTO.getTownName());
        assertEquals("IN", internationalFTDTO.getCountryName());
	}
	
	@Test
	public void testInvokeUpdate() throws Exception {
		String internationalFTDTOJsonPath = new File(getClass().getClassLoader().getResource("InternationalFTBackendDTOJson.json").getFile()).getPath();
		JSONObject internationalFTDTOJson = new JSONObject(new String(Files.readAllBytes(Paths.get(internationalFTDTOJsonPath))));
		InternationalFundTransferBackendDTO InternationalFundTransferBackendDTO = JSONUtils.parse(internationalFTDTOJson.toString(), InternationalFundTransferBackendDTO.class);
		InternationalFundTransferDTO InternationalFundTransferDTO = JSONUtils.parse(internationalFTDTOJson.toString(), InternationalFundTransferDTO.class);
		InternationalFundTransferDTO.setPurposeCode("AQWS");
		InternationalFundTransferDTO.setClearingIdentifierCode("123");
		InternationalFundTransferBackendDTO = InternationalFundTransferBackendDTO.convert(InternationalFundTransferDTO);
        assertEquals("AT088888800000334456", InternationalFundTransferBackendDTO.getIban());
        assertEquals("BARCGB22", InternationalFundTransferBackendDTO.getBicCode());
        assertEquals("BANK OF CALIFORNIA", InternationalFundTransferBackendDTO.getBankName());
        assertEquals("601613", InternationalFundTransferBackendDTO.getBankId());
        assertEquals("USD", InternationalFundTransferBackendDTO.getFeeCurrency());
        assertEquals("Chris", InternationalFundTransferBackendDTO.getBeneficiaryName());
        assertEquals("INST", InternationalFundTransferBackendDTO.getPaymentType());
        assertEquals("1", InternationalFundTransferBackendDTO.getFeeAmount());
        assertEquals("HOME", InternationalFundTransferBackendDTO.getBeneficiaryAddressNickName());
        assertEquals("No5 Fernandes", InternationalFundTransferBackendDTO.getBeneficiaryAddressLine1());
        assertEquals("CITY", InternationalFundTransferBackendDTO.getBeneficiaryCity());
        assertEquals("1122", InternationalFundTransferBackendDTO.getBeneficiaryZipcode());
        assertEquals("IN", InternationalFundTransferBackendDTO.getBeneficiarycountry());
        assertEquals("WONAUS44", InternationalFundTransferBackendDTO.getIntermediaryBicCode());
        assertEquals("112233", InternationalFundTransferBackendDTO.getClearingCode());
        assertEquals("CALI", InternationalFundTransferBackendDTO.getBeneficiaryBankName());
        assertEquals("ADDR", InternationalFundTransferBackendDTO.getBeneficiaryAddressLine2());
        assertEquals("123456789", InternationalFundTransferBackendDTO.getBeneficiaryPhone());
        assertEquals("gangan@temenos.com", InternationalFundTransferBackendDTO.getBeneficiaryEmail());
        assertEquals("TN", InternationalFundTransferBackendDTO.getBeneficiaryState());
        assertEquals("AQWS", InternationalFundTransferBackendDTO.getPurposeCode());
        assertEquals("123", InternationalFundTransferBackendDTO.getClearingIdentifierCode());
        assertEquals("STREET UKILOP", InternationalFundTransferBackendDTO.getStreetName());
        assertEquals("TT town", InternationalFundTransferBackendDTO.getTownName());
        assertEquals("IN", InternationalFundTransferBackendDTO.getCountryName());
        assertEquals("Failure", InternationalFundTransferBackendDTO.getPayeeVerificationStatus());
        assertEquals("Opted out COP", InternationalFundTransferBackendDTO.getPayeeVerificationErrMsg());
	}
}
