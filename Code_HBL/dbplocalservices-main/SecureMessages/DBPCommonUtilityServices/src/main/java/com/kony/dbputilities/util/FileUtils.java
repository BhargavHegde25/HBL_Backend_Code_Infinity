package com.kony.dbputilities.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class FileUtils {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    /**
     * @param fileObject
     * @return base64 encoded format of the file
     * @description reads a file input stream and converts to base64 code
     */
    public static String encodeFile(File fileObject) {
    	
    	if (!isValidFileName(fileObject) && !fileObject.exists() && !fileObject.isFile() && !(fileObject.length() > 0)) {
    		alert.prepareError("Invalid File").log();
    		return null;
        }
        
        try (FileInputStream fileInputStream = new FileInputStream(fileObject)) {
            String encode = "";
            byte bt[] = new byte[(int) fileObject.length()];
            fileInputStream.read(bt);
            encode = new String(Base64.encodeBase64(bt));
            fileInputStream.close();
            return encode;
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return null;
    }
    
    private static boolean isValidFileName(File f) {
	    try {
	       f.getCanonicalPath();
	       return true;
	    }
	    catch (IOException e) {
	       return false;
	    }
    }

}