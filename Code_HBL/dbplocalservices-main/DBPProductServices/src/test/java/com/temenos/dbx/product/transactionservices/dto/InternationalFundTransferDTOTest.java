package com.temenos.dbx.product.transactionservices.dto;
 
import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Test;

import com.dbp.core.util.JSONUtils;

public class InternationalFundTransferDTOTest {

	@Test
	public void testInvoke() throws Exception {
		String internationalFTDTOJsonPath = new File(getClass().getClassLoader().getResource("InternationalFTDTOJson.json").getFile()).getPath();
		JSONObject internationalFTDTOJson = new JSONObject(new String(Files.readAllBytes(Paths.get(internationalFTDTOJsonPath))));
		InternationalFundTransferDTO internationalFTDTO = JSONUtils
				.parse(internationalFTDTOJson.toString(), InternationalFundTransferDTO.class);
		assertEquals("ATBLZ", internationalFTDTO.getPurposeCode());
        assertEquals("1199", internationalFTDTO.getClearingIdentifierCode());
        assertEquals("STREET UKILOP", internationalFTDTO.getStreetName());
        assertEquals("TT town", internationalFTDTO.getTownName());
        assertEquals("IN", internationalFTDTO.getCountryName());
	}
	
	@Test
	public void testInvokeUpdate() throws Exception {
		String internationalFTDTOJsonPath = new File(getClass().getClassLoader().getResource("InternationalFTDTOJson.json").getFile()).getPath();
		JSONObject internationalFTDTOJson = new JSONObject(new String(Files.readAllBytes(Paths.get(internationalFTDTOJsonPath))));
		InternationalFundTransferDTO TransferDTO = JSONUtils.parse(internationalFTDTOJson.toString(), InternationalFundTransferDTO.class);
		TransferDTO.setPurposeCode("AQWS");
		TransferDTO.setClearingIdentifierCode("123");
		InternationalFundTransferDTO internationalFundTransferDTO = JSONUtils.parse(internationalFTDTOJson.toString(), InternationalFundTransferDTO.class);
		internationalFundTransferDTO = TransferDTO.updateValues(internationalFundTransferDTO);
		assertEquals("AQWS", internationalFundTransferDTO.getPurposeCode());
        assertEquals("123", internationalFundTransferDTO.getClearingIdentifierCode());
	}
}
