package com.temenos.dbx.product.payeeservices.dto;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Test;

import com.dbp.core.util.JSONUtils;

public class InternationalPayeeBackendDTOTest {

	@Test
	public void testInvoke() throws Exception {
		String InternationalPayeeBackendDTOJsonPath = new File(getClass().getClassLoader().getResource("InternationalPayeeBackendDTOJson.json").getFile()).getPath();
		JSONObject InternationalPayeeBackendDTOJson = new JSONObject(new String(Files.readAllBytes(Paths.get(InternationalPayeeBackendDTOJsonPath))));
		InternationalPayeeBackendDTO internationalPayeeBackendDTOTest = JSONUtils
				.parse(InternationalPayeeBackendDTOJson.toString(), InternationalPayeeBackendDTO.class);
		Boolean actualRes = internationalPayeeBackendDTOTest.isValidInput();
		assertEquals(actualRes, true);
		assertEquals("612319", internationalPayeeBackendDTOTest.getId());
		assertEquals("1", internationalPayeeBackendDTOTest.getIsApproved());
		assertEquals("612319", internationalPayeeBackendDTOTest.getPayeeId());
		assertEquals("612319", internationalPayeeBackendDTOTest.getUserId());
		assertEquals("191277", internationalPayeeBackendDTOTest.getCif());
		assertEquals("1", internationalPayeeBackendDTOTest.getNoOfCustomersLinked());
		assertEquals("21067", internationalPayeeBackendDTOTest.getAccountNumber());
		assertEquals("savings", internationalPayeeBackendDTOTest.getAccountType());
		assertEquals("Infinity", internationalPayeeBackendDTOTest.getBankName());
		assertEquals("morgan", internationalPayeeBackendDTOTest.getBeneficiaryName());
		assertEquals("IN", internationalPayeeBackendDTOTest.getCountryName());
		assertEquals("", internationalPayeeBackendDTOTest.getCreatedOn());
		assertEquals("Morgan", internationalPayeeBackendDTOTest.getFirstName());
		assertEquals("0", internationalPayeeBackendDTOTest.getIsInternationalAccount());
		assertEquals("0", internationalPayeeBackendDTOTest.getIsSameBankAccount());
		assertEquals("1", internationalPayeeBackendDTOTest.getIsVerified());
		assertEquals("stanley", internationalPayeeBackendDTOTest.getLastName());
		assertEquals("morg", internationalPayeeBackendDTOTest.getNickName());
		assertEquals("for expenses", internationalPayeeBackendDTOTest.getNotes());
		assertEquals("", internationalPayeeBackendDTOTest.getRoutingNumber());
		assertEquals("BARCGB22", internationalPayeeBackendDTOTest.getSwiftCode());
		assertEquals("1", internationalPayeeBackendDTOTest.getSoftDelete());
		assertEquals("123123", internationalPayeeBackendDTOTest.getExternalAccount());
		assertEquals("123123", internationalPayeeBackendDTOTest.getIban());
		assertEquals("", internationalPayeeBackendDTOTest.getSortCode());
		assertEquals("91", internationalPayeeBackendDTOTest.getPhoneCountryCode());
		assertEquals("123456789", internationalPayeeBackendDTOTest.getPhoneNumber());
		assertEquals("91", internationalPayeeBackendDTOTest.getPhoneExtension());
		assertEquals("Home", internationalPayeeBackendDTOTest.getAddressNickName());
		assertEquals("ADDR1", internationalPayeeBackendDTOTest.getAddressLine1());
		assertEquals("CB", internationalPayeeBackendDTOTest.getCity());
		assertEquals("641019", internationalPayeeBackendDTOTest.getZipcode());
		assertEquals("IN", internationalPayeeBackendDTOTest.getCountry());
		assertEquals("GB0010001", internationalPayeeBackendDTOTest.getCompanyId());
		assertEquals("addr2", internationalPayeeBackendDTOTest.getAddressLine2());
		assertEquals("sample@temenos.com", internationalPayeeBackendDTOTest.getEmail());
		assertEquals("123456789", internationalPayeeBackendDTOTest.getPhone());
		assertEquals("1122", internationalPayeeBackendDTOTest.getClearingCode());
		assertEquals("123123", internationalPayeeBackendDTOTest.getClearingIdentifierCode());
		assertEquals("street", internationalPayeeBackendDTOTest.getStreetName());
		assertEquals("IN", internationalPayeeBackendDTOTest.getBankCountryName());
		assertEquals("town", internationalPayeeBackendDTOTest.getTownName());
		assertEquals("BARCGB22", internationalPayeeBackendDTOTest.getIntermediaryBIC());
		assertEquals("Success", internationalPayeeBackendDTOTest.getPayeeVerification());
		assertEquals("21000", internationalPayeeBackendDTOTest.getDbpErrCode());
		assertEquals("invalid input", internationalPayeeBackendDTOTest.getDbpErrMsg());
		
	}

}
