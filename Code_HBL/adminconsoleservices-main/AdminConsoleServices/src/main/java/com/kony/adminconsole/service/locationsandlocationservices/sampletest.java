package com.kony.adminconsole.service.locationsandlocationservices;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class sampletest {
	
	public static void main(String []args) {
		StringBuilder responseCsvBuilder = new StringBuilder();
		try {
			CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT
			        .withHeader("Name", "Code", "Description", "Phone Number", "Type", "Status")
			        .print(responseCsvBuilder);
			String result = responseCsvBuilder.toString();
			byte[] bytes = result.substring(0, result.length() - 2).getBytes(StandardCharsets.UTF_8);
			byte[] encoded = Base64.encodeBase64(bytes);
			String encodedString = new String(encoded);
			System.out.println(encodedString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	private static byte[] loadFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);

	    long length = file.length();
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }
	    byte[] bytes = new byte[(int)length];
	    
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }

	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }

	    is.close();
	    return bytes;
	}

}
