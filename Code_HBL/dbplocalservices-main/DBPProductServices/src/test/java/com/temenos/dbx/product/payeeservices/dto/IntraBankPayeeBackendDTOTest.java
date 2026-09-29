package com.temenos.dbx.product.payeeservices.dto;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Test;

import com.dbp.core.util.JSONUtils;

public class IntraBankPayeeBackendDTOTest {

	@Test
	public void testInvoke() throws Exception {
		String IntraBankPayeeBackendDTOJsonPath = new File(getClass().getClassLoader().getResource("IntraBankPayeeBackendDTOJson.json").getFile()).getPath();
		JSONObject IntraBankPayeeBackendDTOJson = new JSONObject(new String(Files.readAllBytes(Paths.get(IntraBankPayeeBackendDTOJsonPath))));
		IntraBankPayeeBackendDTO IntraBankPayeeBackendDTOTest = JSONUtils
				.parse(IntraBankPayeeBackendDTOJson.toString(), IntraBankPayeeBackendDTO.class);
		Boolean actualRes = IntraBankPayeeBackendDTOTest.isValidInput();
		assertEquals(actualRes, true);
		assertEquals("612319", IntraBankPayeeBackendDTOTest.getId());
		assertEquals("1", IntraBankPayeeBackendDTOTest.getIsApproved());
		assertEquals("612319", IntraBankPayeeBackendDTOTest.getPayeeId());
		assertEquals("612319", IntraBankPayeeBackendDTOTest.getUserId());
		assertEquals("191277", IntraBankPayeeBackendDTOTest.getCif());
		assertEquals("1", IntraBankPayeeBackendDTOTest.getNoOfCustomersLinked());
		assertEquals("21067", IntraBankPayeeBackendDTOTest.getAccountNumber());
		assertEquals("savings", IntraBankPayeeBackendDTOTest.getAccountType());
		assertEquals("Infinity", IntraBankPayeeBackendDTOTest.getBankName());
		assertEquals("morgan", IntraBankPayeeBackendDTOTest.getBeneficiaryName());
		assertEquals("IN", IntraBankPayeeBackendDTOTest.getCountryName());
		assertEquals("", IntraBankPayeeBackendDTOTest.getCreatedOn());
		assertEquals("Morgan", IntraBankPayeeBackendDTOTest.getFirstName());
		assertEquals("0", IntraBankPayeeBackendDTOTest.getIsInternationalAccount());
		assertEquals("1", IntraBankPayeeBackendDTOTest.getIsSameBankAccount());
		assertEquals("1", IntraBankPayeeBackendDTOTest.getIsVerified());
		assertEquals("stanley", IntraBankPayeeBackendDTOTest.getLastName());
		assertEquals("morg", IntraBankPayeeBackendDTOTest.getNickName());
		assertEquals("for expenses", IntraBankPayeeBackendDTOTest.getNotes());
		assertEquals("", IntraBankPayeeBackendDTOTest.getRoutingNumber());
		assertEquals("BARCGB22", IntraBankPayeeBackendDTOTest.getSwiftCode());
		assertEquals("1", IntraBankPayeeBackendDTOTest.getSoftDelete());
		assertEquals("123123", IntraBankPayeeBackendDTOTest.getExternalAccount());
		assertEquals("123123", IntraBankPayeeBackendDTOTest.getIban());
		assertEquals("", IntraBankPayeeBackendDTOTest.getSortCode());
		assertEquals("91", IntraBankPayeeBackendDTOTest.getPhoneCountryCode());
		assertEquals("123456789", IntraBankPayeeBackendDTOTest.getPhoneNumber());
		assertEquals("91", IntraBankPayeeBackendDTOTest.getPhoneExtension());
		assertEquals("Home", IntraBankPayeeBackendDTOTest.getAddressNickName());
		assertEquals("ADDR1", IntraBankPayeeBackendDTOTest.getAddressLine1());
		assertEquals("CB", IntraBankPayeeBackendDTOTest.getCity());
		assertEquals("641019", IntraBankPayeeBackendDTOTest.getZipcode());
		assertEquals("IN", IntraBankPayeeBackendDTOTest.getCountry());
		assertEquals("GB0010001", IntraBankPayeeBackendDTOTest.getCompanyId());
		assertEquals("addr2", IntraBankPayeeBackendDTOTest.getAddressLine2());
		assertEquals("sample@temenos.com", IntraBankPayeeBackendDTOTest.getEmail());
		assertEquals("123456789", IntraBankPayeeBackendDTOTest.getPhone());
		assertEquals("21000", IntraBankPayeeBackendDTOTest.getDbpErrCode());
		assertEquals("invalid input", IntraBankPayeeBackendDTOTest.getDbpErrMsg());
		assertEquals("Success", IntraBankPayeeBackendDTOTest.getPayeeVerification());
	}

}
