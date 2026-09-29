package com.kony.dbputilities.fileutil;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

public class MimeTypeValidator {

	private String mimeType;
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	public MimeTypeValidator(byte[] fileInBytes) {
		InputStream is = new BufferedInputStream(new ByteArrayInputStream(fileInBytes));
		try {
			mimeType = URLConnection.guessContentTypeFromStream(is);
		} catch (IOException e) {
			alert.prepareError("Error occurred while parsing content / invalid file").log();
			mimeType = null;
		}
	}
	
	public boolean hasValidImageMimeType() {
		Set<String> imageMimeTypeSet = new HashSet<>();
		imageMimeTypeSet.add("image/gif");
		imageMimeTypeSet.add("image/png");
		imageMimeTypeSet.add("image/jpeg");
		if(mimeType != null) {
			return imageMimeTypeSet.contains(mimeType);
		}
		return false;
	}
}
