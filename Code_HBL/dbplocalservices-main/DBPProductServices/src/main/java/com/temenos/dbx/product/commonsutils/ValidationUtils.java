package com.temenos.dbx.product.commonsutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Contains methods to validate different input fields
 */
public class ValidationUtils {
	public static boolean isValidEmail(String email) {
		Pattern VALID_EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
		Matcher matcher = VALID_EMAIL_REGEX.matcher(email);
		return matcher.find();
	}
	
	public static boolean isValidPhoneNumber(String phoneNumber) {
		Pattern VALID_PHONE_NUMBER_REGEX = Pattern.compile("^[()+-]*([0-9][()+-.]*){6,20}$");
		phoneNumber = phoneNumber.replaceAll("\\s", ""); 
		Matcher matcher = VALID_PHONE_NUMBER_REGEX.matcher(phoneNumber);
		return matcher.find();
	}
	
	public static boolean isValidName(String name) {
		Pattern INVALID_TEXT_REGEX = Pattern.compile("[+=\\|<>^*%]");
		Matcher invalidMatcher = INVALID_TEXT_REGEX.matcher(name);
		if(invalidMatcher.find()) {
			return false;
		}
		Pattern VALID_NAME_REGEX = Pattern.compile("^[a-zA-Z0-9àáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-@#&*_]{1,50}$");
		Matcher matcher = VALID_NAME_REGEX.matcher(name);
		return matcher.find();
	}
	
	public static boolean isValidAccountNumber(String accNumber) {
		Pattern VALID_ACCOUNT_NUMBER_REGEX = Pattern.compile("^[a-zA-Z0-9]{1,50}$");
		Matcher matcher = VALID_ACCOUNT_NUMBER_REGEX.matcher(accNumber);
		return matcher.find();
	}
	
	public static boolean isValidText(String text) {
		if(text.length() < 1 && text.length() > 150) {
			return false;
		}
		Pattern INVALID_TEXT_REGEX = Pattern.compile("[+=\\|<>^*%]");
		Matcher matcher = INVALID_TEXT_REGEX.matcher(text);
		return !matcher.find();
	}
	
	public static boolean isValidSwiftCode(String swiftCode) {
		Pattern VALID_SWIFT_CODE_REGEX = Pattern.compile("^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$");
		Matcher matcher = VALID_SWIFT_CODE_REGEX.matcher(swiftCode.toUpperCase());
		return matcher.find();
	}
	
	public static boolean isValidRoutingNumber(String routingNumber) {
		Pattern VALID_ROUTING_NUMBER_REGEX = Pattern.compile("^[0-9]{6,11}$");
		Matcher matcher = VALID_ROUTING_NUMBER_REGEX.matcher(routingNumber);
		return matcher.find();
	}
}
