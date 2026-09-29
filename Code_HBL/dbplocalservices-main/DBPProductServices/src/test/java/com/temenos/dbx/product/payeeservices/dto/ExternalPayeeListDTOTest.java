package com.temenos.dbx.product.payeeservices.dto;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Test;

import com.dbp.core.util.JSONUtils;

public class ExternalPayeeListDTOTest {

	@Test
	public void testInvoke() throws Exception {
		String externalPayeeListJsonPath = new File(getClass().getClassLoader().getResource("ExternalPayeeListJson.json").getFile()).getPath();
		JSONObject externalPayeeListJson = new JSONObject(new String(Files.readAllBytes(Paths.get(externalPayeeListJsonPath))));
		ExternalPayeeListDTO externalPayeeListDTOTest = JSONUtils
				.parse(externalPayeeListJson.toString(), ExternalPayeeListDTO.class);
		assertEquals("612319", externalPayeeListDTOTest.getId());
		assertEquals("Interbank", externalPayeeListDTOTest.getTypeId());
		assertEquals("612319", externalPayeeListDTOTest.getPayeeId());
		assertEquals("Infinity", externalPayeeListDTOTest.getCreatedBy());
		assertEquals("9588758667", externalPayeeListDTOTest.getContractId());
		assertEquals("191277", externalPayeeListDTOTest.getCif());
		assertEquals("1", externalPayeeListDTOTest.getNoOfCustomersLinked());
		assertEquals("21067", externalPayeeListDTOTest.getAccountNumber());
		assertEquals("savings", externalPayeeListDTOTest.getAccountType());
		assertEquals("Infinity", externalPayeeListDTOTest.getBankName());
		assertEquals("morgan", externalPayeeListDTOTest.getBeneficiaryName());
		assertEquals("IN", externalPayeeListDTOTest.getCountryName());
		assertEquals("", externalPayeeListDTOTest.getCreatedOn());
		assertEquals("Morgan", externalPayeeListDTOTest.getFirstName());
		assertEquals("0", externalPayeeListDTOTest.getIsInternationalAccount());
		assertEquals("0", externalPayeeListDTOTest.getIsSameBankAccount());
		assertEquals("1", externalPayeeListDTOTest.getIsVerified());
		assertEquals("stanley", externalPayeeListDTOTest.getLastName());
		assertEquals("morg", externalPayeeListDTOTest.getNickName());
		assertEquals("for expenses", externalPayeeListDTOTest.getNotes());
		assertEquals("", externalPayeeListDTOTest.getRoutingNumber());
		assertEquals("BARCGB22", externalPayeeListDTOTest.getSwiftCode());
		assertEquals("1", externalPayeeListDTOTest.getSoftDelete());
		assertEquals("123123", externalPayeeListDTOTest.getExternalAccount());
		assertEquals("123123", externalPayeeListDTOTest.getIban());
		assertEquals("", externalPayeeListDTOTest.getSortCode());
		assertEquals("91", externalPayeeListDTOTest.getPhoneCountryCode());
		assertEquals("123456789", externalPayeeListDTOTest.getPhoneNumber());
		assertEquals("91", externalPayeeListDTOTest.getPhoneExtension());
		assertEquals("Home", externalPayeeListDTOTest.getAddressNickName());
		assertEquals("ADDR1", externalPayeeListDTOTest.getAddressLine1());
		assertEquals("CB", externalPayeeListDTOTest.getCity());
		assertEquals("641019", externalPayeeListDTOTest.getZipcode());
		assertEquals("IN", externalPayeeListDTOTest.getCountry());
		assertEquals("GB0010001", externalPayeeListDTOTest.getCompanyId());
		assertEquals("addr2", externalPayeeListDTOTest.getAddressLine2());
		assertEquals("sample@temenos.com", externalPayeeListDTOTest.getEmail());
		assertEquals("123456789", externalPayeeListDTOTest.getPhone());
		assertEquals("1122", externalPayeeListDTOTest.getClearingCode());
		assertEquals("123123", externalPayeeListDTOTest.getClearingIdentifierCode());
		assertEquals("street", externalPayeeListDTOTest.getStreetName());
		assertEquals("IN", externalPayeeListDTOTest.getBankCountryName());
		assertEquals("town", externalPayeeListDTOTest.getTownName());
		assertEquals("BARCGB22", externalPayeeListDTOTest.getIntermediaryBIC());
		assertEquals("Success", externalPayeeListDTOTest.getPayeeVerification());
	}

}
