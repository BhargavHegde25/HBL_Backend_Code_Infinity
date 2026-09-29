package com.kony.dbp.alertrouterservice.email;
import java.util.List;

public class Recipients {
	
	private List<Recipient> recipient;

	
	
	
	public List<Recipient> getRecipient() {
		return recipient;
	}
	public void setRecipient(List<Recipient> recipient) {
		this.recipient = recipient;
	}
	@Override
	public String toString() {
		return "Recipients [recipient=" + recipient + "]";
	}
	
	
	
	
	

}
