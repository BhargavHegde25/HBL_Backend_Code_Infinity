package com.temenos.dbx.product.transactionservices.dto;
 
import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Test;

import com.dbp.core.util.JSONUtils;

public class InterBankFundTransferDTOTest {

	@Test
	public void testInvoke() throws Exception {
		String interBankFTDTOJsonPath = new File(getClass().getClassLoader().getResource("InterBankFTDTOJson.json").getFile()).getPath();
		JSONObject interBankFTDTOJson = new JSONObject(new String(Files.readAllBytes(Paths.get(interBankFTDTOJsonPath))));
		InterBankFundTransferDTO interBankFundTransferDTO = JSONUtils
				.parse(interBankFTDTOJson.toString(), InterBankFundTransferDTO.class);
		assertEquals("ATBLZ", interBankFundTransferDTO.getPurposeCode());
        assertEquals("CIC", interBankFundTransferDTO.getClearingIdentifierCode());
        assertEquals("INST", interBankFundTransferDTO.getLocalInstrumentProprietary());
        assertEquals("III", interBankFundTransferDTO.getServiceLevelProprietary());
	}
	
	@Test
	public void testInvokeUpdate() throws Exception {
		String interBankFTDTOJsonPath = new File(getClass().getClassLoader().getResource("InterBankFTDTOJson.json").getFile()).getPath();
		JSONObject interBankFTDTOJson = new JSONObject(new String(Files.readAllBytes(Paths.get(interBankFTDTOJsonPath))));
		InterBankFundTransferDTO TransferDTO = JSONUtils.parse(interBankFTDTOJson.toString(), InterBankFundTransferDTO.class);
		TransferDTO.setPurposeCode("AQWS");
		TransferDTO.setClearingIdentifierCode("123");
		InterBankFundTransferDTO interBankFundTransferDTO = JSONUtils.parse(interBankFTDTOJson.toString(), InterBankFundTransferDTO.class);
		interBankFundTransferDTO = TransferDTO.updateValues(interBankFundTransferDTO);
		assertEquals("AQWS", interBankFundTransferDTO.getPurposeCode());
        assertEquals("123", interBankFundTransferDTO.getClearingIdentifierCode());
	}
}
