package com.kony.utils;

import java.util.Base64;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

public class TokenUtils {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");
	private JSONObject tokenobj;
	private String token;

	public TokenUtils(String token) {
		this.token = token;
		tokenobj = new JSONObject();
		decodeToken();
	}

	public void decodeToken() {
		if (this.token != null) {
			String[] parts = token.split("\\.");
			if (parts.length < 3) {
				diagnostic.prepareDebug("Invalid token.").log();
				return;
			}
			String res = new String(Base64.getUrlDecoder().decode(parts[1]));
			tokenobj = new JSONObject(res);
		} else {
			diagnostic.prepareDebug("Invalid token.").log();
		}
	}

	public String getValue(String key) {
		if (tokenobj.has(key))
			return tokenobj.getString(key);
		return null;
	}

}