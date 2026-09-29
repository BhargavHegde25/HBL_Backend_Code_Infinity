package com.temenos.infinity.api.visaintegrationapi.constants;

/**
 * TODO: Document me!
 *
 * @author Venkat
 *
 */
public interface VisaIntegrationConstants {
	String PARAM_GOOGLEPAY_RESOURCEPATH = "provisioning/cardData/googlePay";
	String PARAM_SAMSUNGPAY_RESOURCEPATH = "provisioning/cardData/samsungPay";
	String PARAM_APPLEPAY_RESOURCEPATH = "provisioning/cardData/applePay";
	String PARAM_ENROLLCARD_RESOURCEPATH = "core/cards";
	String PARAM_APPLEPAY = "applePay";
	String PARAM_GOOGLEPAY = "googlePay";
	String PARAM_SAMSUNGPAY = "samsungPay";
	String PARAM_OPERATION_ENROLLCARD = "enrollCard";
	String PARAM_OPERATION_LINKCARD = "linkCard";

	String PARAM_SHAREDSECRET = "ZpXU-70ujpQYL1P/OTYvq7/GKU46kGmEI-H3AE}u";

	String PARAM_GOOGLEPAY_URL = "https://sandbox.api.visa.com/inapp/provisioning/cardData/googlePay?apikey=DKLLKWTPHNOVZQZEK75T21gHDRdhnkUDdbq5I2NRHAmzrOTks";
	String PARAM_SAMSUNGPAY_URL = "https://sandbox.api.visa.com/inapp/provisioning/cardData/samsungPay?apikey=DKLLKWTPHNOVZQZEK75T21gHDRdhnkUDdbq5I2NRHAmzrOTks";
	String PARAM_APPLEPAY_URL = "https://sandbox.api.visa.com/inapp/provisioning/cardData/applePay?apikey=DKLLKWTPHNOVZQZEK75T21gHDRdhnkUDdbq5I2NRHAmzrOTks";
	String PARAM_ENROLLCARD_URL = "https://sandbox.api.visa.com/universal/core/cards?apikey=DKLLKWTPHNOVZQZEK75T21gHDRdhnkUDdbq5I2NRHAmzrOTks";

	String PARAM_mleServerPublicCertificatePath = "PublicCertificate/VisaPublicKey_ForEncryption_Sbx_Cert.pem";
	String PARAM_mleServerPublicCertificate = "-----BEGIN CERTIFICATE-----\r\n"
			+ "MIIFEzCCA/ugAwIBAgIQCC611ih3Gb6s+yjr8xrqLDANBgkqhkiG9w0BAQsFADB9\r\n"
			+ "MQswCQYDVQQGEwJVUzENMAsGA1UEChMEVklTQTEvMC0GA1UECxMmVmlzYSBJbnRl\r\n"
			+ "cm5hdGlvbmFsIFNlcnZpY2UgQXNzb2NpYXRpb24xLjAsBgNVBAMTJVZpc2EgSW5m\r\n"
			+ "b3JtYXRpb24gRGVsaXZlcnkgRXh0ZXJuYWwgQ0EwHhcNMTkxMTA3MDY0OTU2WhcN\r\n"
			+ "MjIwMjA0MDY0OTU2WjCBkTEUMBIGA1UEBwwLRm9zdGVyIENpdHkxEzARBgNVBAgM\r\n"
			+ "CkNhbGlmb3JuaWExCzAJBgNVBAYTAlVTMREwDwYDVQQKDAhWaXNhIEluYzEYMBYG\r\n"
			+ "A1UECwwPT3V0Ym91bmQgQ2xpZW50MSowKAYDVQQDDCFlbmMtYjUxZGZhMjMuc2J4\r\n"
			+ "LmRpZ2l0YWwudmlzYS5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIB\r\n"
			+ "AQC519dPGBwj49rbMc8ErsXaCAjnexMRHSBLUtIjpr3CG9m1kgE5CdzkxDyBQ85H\r\n"
			+ "jYiMr6f222Rw3eOmLsqI8MavpQQvxmzTosEp6WiYUZ9CKIhi3+Vs0AL/ucGb5dux\r\n"
			+ "FBLTNoQJIdfcuT11Vnc2tUSh9f/xdCpXALdPfpGlQsgO2voWjhXuLD07zK+hC3eL\r\n"
			+ "VfBj5MqPq/94/Euzlfq3s4SGRJtY1deUqvZdNN+Jrnk5utGySU/0we9KB8pWCha2\r\n"
			+ "lWoKVCrmrRa+swZMjo7xL1YUxWpbTl0zVQyCIzquIxc2Mq3A6Rst0FX/H/5II+n4\r\n"
			+ "wbcfjumgyrN/etBKET4j+h21AgMBAAGjggF4MIIBdDBlBggrBgEFBQcBAQRZMFcw\r\n"
			+ "LgYIKwYBBQUHMAKGImh0dHA6Ly9lbnJvbGwudmlzYWNhLmNvbS92aWNhMy5jZXIw\r\n"
			+ "JQYIKwYBBQUHMAGGGWh0dHA6Ly9vY3NwLnZpc2EuY29tL29jc3AwHQYDVR0OBBYE\r\n"
			+ "FJwer6q0e5ZugnmvV0fWFsBDF8wmMAwGA1UdEwEB/wQCMAAwHwYDVR0jBBgwFoAU\r\n"
			+ "GTpSZs0pH+P6yzR9FnYhAtpPuRgwOQYDVR0gBDIwMDAuBgVngQMCATAlMCMGCCsG\r\n"
			+ "AQUFBwIBFhdodHRwOi8vd3d3LnZpc2EuY29tL3BraTBdBgNVHR8EVjBUMCigJqAk\r\n"
			+ "hiJodHRwOi8vRW5yb2xsLnZpc2FjYS5jb20vVklDQTMuY3JsMCigJqAkhiJodHRw\r\n"
			+ "Oi8vY3JsLmlub3YudmlzYS5uZXQvVklDQTMuY3JsMA4GA1UdDwEB/wQEAwIHgDAT\r\n"
			+ "BgNVHSUEDDAKBggrBgEFBQcDAjANBgkqhkiG9w0BAQsFAAOCAQEArBpfdd6wV2JY\r\n"
			+ "HwoSsJFuHhh4n+3v5sVoL4tkHJW2yRtlPYjpAwmw2ol1//m4evTXFDbzncDic32w\r\n"
			+ "ym2TqtgQwQ8OmcoWjRM7H8iIxfSEDxzdOAoZEIPVK+ZhwjL9wKQnuXevhAV7yz39\r\n"
			+ "QlbEBr6PC3UE+8bZykbLEPJHS7QvghFI9UBUAkhuF2nl5O9IwVPHSnntody7qPBr\r\n"
			+ "SB4YCHLDu1BoNNI1cTUk/KNsKcqz9+0lGtS1RPr9qxHt4RzQKPd6XNLm++udY4Ih\r\n"
			+ "vuEj4oB5Z2pxqiIFYS7YCmjUhl36MXyYdexmcZ85nnJtD4OMXrzDI2wMhuJU7DK7\r\n" + "tBNBHpQ2pg==\r\n"
			+ "-----END CERTIFICATE-----";

	String KEY_ID = "Q2AY3V5E3ICNBUU66D8K11hBmzqdXSvTiNzZ-YnpozWRXTo50";
	String ACCOUNTNUMBER = "4514236833852412";
	String CARD_VISA_STUB = "CARD_VISA_STUB";
	String VISA_API_KEY = "VISA_API_KEY";
	String CVV = "123";
	String EXPIRYYEAR = "2021";
	String EXPIRYMONTH = "01";
	String NAMEONCARD = "stubbedName";
}
