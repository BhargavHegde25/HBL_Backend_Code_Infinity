package com.temenos.dbx.product.payeeservices.dto;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Test;

import com.dbp.core.util.JSONUtils;

public class InterBankPayeeBackendDTOTest {

	@Test
	public void testInvoke() throws Exception {
		String InterBankPayeeBackendDTOJsonPath = new File(getClass().getClassLoader().getResource("InterBankPayeeBackendDTOJson.json").getFile()).getPath();
		JSONObject InterBankPayeeBackendDTOJson = new JSONObject(new String(Files.readAllBytes(Paths.get(InterBankPayeeBackendDTOJsonPath))));
		InterBankPayeeBackendDTO InterBankPayeeBackendDTOTest = JSONUtils
				.parse(InterBankPayeeBackendDTOJson.toString(), InterBankPayeeBackendDTO.class);
		Boolean actualRes = InterBankPayeeBackendDTOTest.isValidInput();
		assertEquals(actualRes, true);
		assertEquals("612319", InterBankPayeeBackendDTOTest.getId());
		assertEquals("1", InterBankPayeeBackendDTOTest.getIsApproved());
		assertEquals("612319", InterBankPayeeBackendDTOTest.getPayeeId());
		assertEquals("612319", InterBankPayeeBackendDTOTest.getUserId());
		assertEquals("191277", InterBankPayeeBackendDTOTest.getCif());
		assertEquals("1", InterBankPayeeBackendDTOTest.getNoOfCustomersLinked());
		assertEquals("21067", InterBankPayeeBackendDTOTest.getAccountNumber());
		assertEquals("savings", InterBankPayeeBackendDTOTest.getAccountType());
		assertEquals("Infinity", InterBankPayeeBackendDTOTest.getBankName());
		assertEquals("morgan", InterBankPayeeBackendDTOTest.getBeneficiaryName());
		assertEquals("IN", InterBankPayeeBackendDTOTest.getCountryName());
		assertEquals("", InterBankPayeeBackendDTOTest.getCreatedOn());
		assertEquals("Morgan", InterBankPayeeBackendDTOTest.getFirstName());
		assertEquals("0", InterBankPayeeBackendDTOTest.getIsInternationalAccount());
		assertEquals("0", InterBankPayeeBackendDTOTest.getIsSameBankAccount());
		assertEquals("1", InterBankPayeeBackendDTOTest.getIsVerified());
		assertEquals("stanley", InterBankPayeeBackendDTOTest.getLastName());
		assertEquals("morg", InterBankPayeeBackendDTOTest.getNickName());
		assertEquals("for expenses", InterBankPayeeBackendDTOTest.getNotes());
		assertEquals("", InterBankPayeeBackendDTOTest.getRoutingNumber());
		assertEquals("BARCGB22", InterBankPayeeBackendDTOTest.getSwiftCode());
		assertEquals("1", InterBankPayeeBackendDTOTest.getSoftDelete());
		assertEquals("123123", InterBankPayeeBackendDTOTest.getExternalAccount());
		assertEquals("123123", InterBankPayeeBackendDTOTest.getIban());
		assertEquals("", InterBankPayeeBackendDTOTest.getSortCode());
		assertEquals("91", InterBankPayeeBackendDTOTest.getPhoneCountryCode());
		assertEquals("123456789", InterBankPayeeBackendDTOTest.getPhoneNumber());
		assertEquals("91", InterBankPayeeBackendDTOTest.getPhoneExtension());
		assertEquals("Home", InterBankPayeeBackendDTOTest.getAddressNickName());
		assertEquals("ADDR1", InterBankPayeeBackendDTOTest.getAddressLine1());
		assertEquals("CB", InterBankPayeeBackendDTOTest.getCity());
		assertEquals("641019", InterBankPayeeBackendDTOTest.getZipcode());
		assertEquals("IN", InterBankPayeeBackendDTOTest.getCountry());
		assertEquals("GB0010001", InterBankPayeeBackendDTOTest.getCompanyId());
		assertEquals("addr2", InterBankPayeeBackendDTOTest.getAddressLine2());
		assertEquals("sample@temenos.com", InterBankPayeeBackendDTOTest.getEmail());
		assertEquals("123456789", InterBankPayeeBackendDTOTest.getPhone());
		assertEquals("1122", InterBankPayeeBackendDTOTest.getClearingCode());
		assertEquals("123123", InterBankPayeeBackendDTOTest.getClearingIdentifierCode());
		assertEquals("21000", InterBankPayeeBackendDTOTest.getDbpErrCode());
		assertEquals("invalid input", InterBankPayeeBackendDTOTest.getDbpErrMsg());
		assertEquals("GB0010001", InterBankPayeeBackendDTOTest.getLegalEntityId());
		assertEquals("1", InterBankPayeeBackendDTOTest.getDeletedRecords());
		assertEquals("612319", InterBankPayeeBackendDTOTest.getCustomerId());
		assertEquals("Success", InterBankPayeeBackendDTOTest.getPayeeVerification());
	}

}
