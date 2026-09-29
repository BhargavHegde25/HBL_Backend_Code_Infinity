package com.kony.adminconsole.commons.utils;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.session.Session;

public class SessionMemoryManager {

	private SessionMemoryManager() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	public static void saveIntoSession(DataControllerRequest request, String key, String value) {

		Session session = null;
		try {
			session = request.getSession();
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching session from DataControllerRequest", e).log();
		}

		if (session != null && StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {

			session.setAttribute(key, value);
		}

	}

	public static Object getFromSession(DataControllerRequest request, String key) {
		Session session = null;
		try {
			session = request.getSession();
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching session from DataControllerRequest", e).log();
		}

		if (session != null && StringUtils.isNotBlank(key)) {
			return session.getAttribute(key);
		}

		return new Object();

	}

	public static void removeFromSession(DataControllerRequest request, String key) {
		Session session = null;
		try {
			session = request.getSession();
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching session from DataControllerRequest", e).log();
		}

		if (session != null && StringUtils.isNotBlank(key)) {

			session.removeAttribute(key);
		}

	}

}
