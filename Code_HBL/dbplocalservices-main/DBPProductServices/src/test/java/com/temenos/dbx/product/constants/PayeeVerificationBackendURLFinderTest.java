package com.temenos.dbx.product.constants;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PayeeVerificationBackendURLFinderTest {

	PayeeVerificationBackendURLFinder backendHelper = new PayeeVerificationBackendURLFinder();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@AfterClass
	public static void cleanup() throws Exception {
	}
	
	@Before //This will run before each test case
	public void setUp() throws Exception {
		backendHelper.loadTransactionBackendURLs();
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInvoke() throws Exception {
		String resultGB = PayeeVerificationBackendURLFinder.getCountryBackendURL("GB");
		assertEquals(resultGB, "PayeeVerificationJavaServices.countryGBPayeeVerification");
		String resultDD = PayeeVerificationBackendURLFinder.getCountryBackendURL("DD");
		assertEquals(resultDD, "PayeeVerificationJavaServices.countryGBPayeeVerification");
		
		String resultSB = PayeeVerificationBackendURLFinder.getPaymentTypeBackendURL("SameBank");
		assertEquals(resultSB, "PayeeVerificationJavaServices.paymentTypeSameBankPayeeVerification");
		String resultIB = PayeeVerificationBackendURLFinder.getPaymentTypeBackendURL("IntraBankTransfer");
		assertEquals(resultIB, "PayeeVerificationJavaServices.paymentTypePayeeVerification");
	}

}
