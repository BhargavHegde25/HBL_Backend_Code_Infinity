package com.bct.utilities;

import java.util.HashMap;
import java.util.Map;

import com.emv.qrcode.core.model.mpm.TagLengthString;
import com.emv.qrcode.decoder.mpm.DecoderMpm;
import com.emv.qrcode.model.mpm.AdditionalDataFieldTemplate;
import com.emv.qrcode.model.mpm.MerchantAccountInformationTemplate;
import com.emv.qrcode.model.mpm.MerchantInformationLanguageTemplate;
import com.emv.qrcode.model.mpm.MerchantPresentedMode;
import com.emv.qrcode.validators.Crc16Validate;

import br.com.fluentvalidator.context.ValidationResult;

/**
 * Utility class for EMV QR code validation and parsing.
 */
public class QRCodeEMVUtil {

	// Mapping of EMV tag IDs to human-readable names
	private static final Map<String, String> EMV_TAG_NAMES = new HashMap<>();
	static {
		EMV_TAG_NAMES.put("00", "Payload Format Indicator");
		EMV_TAG_NAMES.put("01", "Point of Initiation Method");
		EMV_TAG_NAMES.put("29", "Merchant Account Information");
		EMV_TAG_NAMES.put("52", "Merchant Category Code");
		EMV_TAG_NAMES.put("53", "Transaction Currency");
		EMV_TAG_NAMES.put("54", "Transaction Amount");
		EMV_TAG_NAMES.put("56", "Tip or Convenience Indicator");
		EMV_TAG_NAMES.put("58", "Country Code");
		EMV_TAG_NAMES.put("59", "Merchant Name");
		EMV_TAG_NAMES.put("60", "Merchant City");
		EMV_TAG_NAMES.put("62", "Additional Data Field Template");
		EMV_TAG_NAMES.put("64", "Merchant Alternate Language");
		EMV_TAG_NAMES.put("63", "CRC");
	}

	private static final Map<String, String> NESTED_TAG_DESCRIPTIONS = new HashMap<>();
	static {
		NESTED_TAG_DESCRIPTIONS.put("00", "GUI");
		NESTED_TAG_DESCRIPTIONS.put("01", "Account ID");
		NESTED_TAG_DESCRIPTIONS.put("02", "Merchant ID");
		NESTED_TAG_DESCRIPTIONS.put("03", "Terminal ID");
		NESTED_TAG_DESCRIPTIONS.put("04", "Purpose");
	}

	/**
	 * Parses and validates an EMV QR Code string.
	 *
	 * @param qrCodeData The QR code string to be decoded.
	 * @return A map containing all parsed EMV fields with their ID, Name, and
	 *         Value.
	 */
	public static Map<String, String> parseEMVQRCode(String qrCodeData) {
		Map<String, String> parsedData = new HashMap<>();

		try {
			// Validate QR data before decoding
			ValidationResult validationResult = Crc16Validate.validate(qrCodeData);

			if (!validationResult.isValid()) {
				parsedData.put("ERROR", "CRC validation failed: " + validationResult.getErrors());
				return parsedData;
			}

			// Decode the QR code
			MerchantPresentedMode emvData = DecoderMpm.decode(qrCodeData, MerchantPresentedMode.class);

			// Extract relevant fields
			addTag(parsedData, "00", emvData.getPayloadFormatIndicator());
			addTag(parsedData, "01", emvData.getPointOfInitiationMethod());
			addTag(parsedData, "52", emvData.getMerchantCategoryCode());
			addTag(parsedData, "53", emvData.getTransactionCurrency());
			addTag(parsedData, "54", emvData.getTransactionAmount());
			addTag(parsedData, "56", emvData.getTipOrConvenienceIndicator());
			addTag(parsedData, "58", emvData.getCountryCode());
			addTag(parsedData, "59", emvData.getMerchantName());
			addTag(parsedData, "60", emvData.getMerchantCity());
			addAdditionalData(parsedData, "62", emvData.getAdditionalDataField());
			addMerchantLanguage(parsedData, "64", emvData.getMerchantInformationLanguage());
			addTag(parsedData, "63", emvData.getCRC());

			// Extract Merchant Account Information
			if (emvData.getMerchantAccountInformation() != null) {
				for (Map.Entry<String, MerchantAccountInformationTemplate> entry : emvData
						.getMerchantAccountInformation().entrySet()) {
					parsedData.put(entry.getKey(), "Merchant Account Information: " + entry.getValue().toString());
				}
			}
			// Append nested merchant info after base decoding
			addNestedMerchantAccountInfo(parsedData, emvData.getMerchantAccountInformation());
		} catch (Exception e) {
			parsedData.put("ERROR", "Error decoding QR Code: " + e.getMessage());
		}

		return parsedData;
	}

	private static void addNestedMerchantAccountInfo(Map<String, String> parsedData,
			Map<String, MerchantAccountInformationTemplate> merchantInfoMap) {
		if (merchantInfoMap == null)
			return;

		for (Map.Entry<String, MerchantAccountInformationTemplate> entry : merchantInfoMap.entrySet()) {
			String tagId = entry.getKey();
			MerchantAccountInformationTemplate info = entry.getValue();

			if (info != null) {
				String tlvString = info.toString(); // full TLV string (e.g., "2708A00000067701")
				if (tlvString != null && tlvString.length() > 4) {
					String nestedValue = tlvString.substring(4); // skip tag and length to get value
					Map<String, String> nested = parseNestedTLV(nestedValue);
					for (Map.Entry<String, String> nestedEntry : nested.entrySet()) {
						String nestedTag = nestedEntry.getKey();
						String nestedDesc = NESTED_TAG_DESCRIPTIONS.getOrDefault(nestedTag, "Unknown");
						String compoundKey = tagId + "." + nestedTag;
						String compoundValue = nestedDesc + ": " + nestedEntry.getValue();
						parsedData.put(compoundKey, compoundValue);
					}
				}
			}
		}
	}

	// Utility method to add simple Tag-Length-Value fields
	private static void addTag(Map<String, String> parsedData, String tagId, TagLengthString tag) {
		if (tag != null) {
			String tagName = EMV_TAG_NAMES.getOrDefault(tagId, "Unknown Tag");
			parsedData.put(tagId, tagName + ": " + tag.getValue());
		}
	}

	// Utility method to add Additional Data Field Template
	private static void addAdditionalData(Map<String, String> parsedData, String tagId,
			AdditionalDataFieldTemplate dataField) {
		if (dataField != null) {
			parsedData.put(tagId, "Additional Data Field Template: " + dataField.toString());
		}
	}

	// Utility method to add Merchant Information Language Template
	private static void addMerchantLanguage(Map<String, String> parsedData, String tagId,
			MerchantInformationLanguageTemplate merchantLanguage) {
		if (merchantLanguage != null) {
			parsedData.put(tagId, "Merchant Alternate Language: " + merchantLanguage.toString());
		}
	}

	private static Map<String, String> parseNestedTLV(String value) {
		Map<String, String> nestedMap = new HashMap<>();
		int index = 0;

		while (index + 4 <= value.length()) {
			try {
				String tag = value.substring(index, index + 2);
				int length = Integer.parseInt(value.substring(index + 2, index + 4));
				int valueStart = index + 4;
				int valueEnd = valueStart + length;

				if (valueEnd > value.length())
					break;

				String subValue = value.substring(valueStart, valueEnd);
				nestedMap.put(tag, subValue);
				index = valueEnd;
			} catch (Exception e) {
				break;
			}
		}

		return nestedMap;
	}

}
