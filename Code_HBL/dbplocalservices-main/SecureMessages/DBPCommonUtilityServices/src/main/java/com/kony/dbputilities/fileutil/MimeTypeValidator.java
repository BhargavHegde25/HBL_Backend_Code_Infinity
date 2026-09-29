package com.kony.dbputilities.fileutil;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

public class MimeTypeValidator {

	private MagicMatch magicMatch = null;
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	public MimeTypeValidator(byte[] fileInBytes) throws MagicParseException,
				MagicMatchNotFoundException, MagicException {
		magicMatch = Magic.getMagicMatch(fileInBytes);
	}
	
	public boolean hasValidImageMimeType() {
		String[] imageMimeTypeList = new String[] {"image/gif", "image/png",
				"image/tiff", "image/jpeg"};
		if(magicMatch != null) {
			for(String mimeType : imageMimeTypeList) {
				diagnostic.prepareDebug("comparing file mime type {} with list mime type {}", mimeType, magicMatch.getMimeType()).log();
				if(mimeType.equalsIgnoreCase(magicMatch.getMimeType())) {
					return true;
				}
			}
		}
		return false;
	}
}
