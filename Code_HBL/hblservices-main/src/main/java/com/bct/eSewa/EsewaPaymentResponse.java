package com.bct.eSewa;

import java.util.List;

public class EsewaPaymentResponse {
	private List<EsewaLoadTransactionDetail> esewa_load_transaction_detail;
	private String status;
	private String response_code;
	private String message;

	public List<EsewaLoadTransactionDetail> getEsewa_load_transaction_detail() {
		return esewa_load_transaction_detail;
	}

	public void setEsewa_load_transaction_detail(List<EsewaLoadTransactionDetail> esewa_load_transaction_detail) {
		this.esewa_load_transaction_detail = esewa_load_transaction_detail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponse_code() {
		return response_code;
	}

	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static class EsewaLoadTransactionDetail {
		private String transaction_status;
		private String originating_unique_id;
		private String transaction_id;

		public String getTransaction_status() {
			return transaction_status;
		}

		public void setTransaction_status(String transaction_status) {
			this.transaction_status = transaction_status;
		}

		public String getOriginating_unique_id() {
			return originating_unique_id;
		}

		public void setOriginating_unique_id(String originating_unique_id) {
			this.originating_unique_id = originating_unique_id;
		}

		public String getTransaction_id() {
			return transaction_id;
		}

		public void setTransaction_id(String transaction_id) {
			this.transaction_id = transaction_id;
		}
	}

	@Override
	public String toString() {
		return "EsewaPaymentResponse [esewa_load_transaction_detail=" + esewa_load_transaction_detail + ", status="
				+ status + ", response_code=" + response_code + ", message=" + message + "]";
	}
	
	
}
