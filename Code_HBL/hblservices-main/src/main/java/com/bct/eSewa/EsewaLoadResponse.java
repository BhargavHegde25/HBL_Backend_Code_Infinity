package com.bct.eSewa;

public class EsewaLoadResponse {

	public String data;
	public String secret_key;
	public String signature;
	public String code;
	public String message;
	public String batch_id;
	public String status;
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getSecret_key() {
		return secret_key;
	}
	public void setSecret_key(String secret_key) {
		this.secret_key = secret_key;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "EsewaLoadResponse [data=" + data + ", secret_key=" + secret_key + ", signature=" + signature + ", code="
				+ code + ", message=" + message + ", batch_id=" + batch_id + ", status=" + status + "]";
	}
	
	
	
}
