package com.kony.alertslogservices.core;

import java.io.Serializable;
import java.util.UUID;

/**
 * 
 * Base class for all types of logs

 * @author Sridhar Reddy
 *
 */

public abstract class BaseActivity implements Serializable {

	private static final long serialVersionUID = -5651395066551481017L;

	private String id;

	public BaseActivity() {
		this.id = UUID.randomUUID().toString();

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
