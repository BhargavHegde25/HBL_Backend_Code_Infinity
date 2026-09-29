package com.bct.eSewa;

import java.util.List;

public class ESewaStatusCheckResponse {

	private List<EsewaLoadStatusResponse> esewa_load_status_responses;

	public List<EsewaLoadStatusResponse> getEsewa_load_status_responses() {
		return esewa_load_status_responses;
	}

	public void setEsewa_load_status_responses(List<EsewaLoadStatusResponse> esewa_load_status_responses) {
		this.esewa_load_status_responses = esewa_load_status_responses;
	}

	public static class EsewaLoadStatusResponse {
		private String transactionStatus;
		private String originatingUniqueId;
		private String transactionId;
		private String message; // Optional - only present for NOT_FOUND
		private String response_code;

		public String getTransactionStatus() {
			return transactionStatus;
		}

		public void setTransactionStatus(String transactionStatus) {
			this.transactionStatus = transactionStatus;
		}

		public String getOriginatingUniqueId() {
			return originatingUniqueId;
		}

		public void setOriginatingUniqueId(String originatingUniqueId) {
			this.originatingUniqueId = originatingUniqueId;
		}

		public String getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getResponse_code() {
			return response_code;
		}

		public void setResponse_code(String response_code) {
			this.response_code = response_code;
		}

		@Override
		public String toString() {
			return "EsewaLoadStatusResponse [transactionStatus=" + transactionStatus + ", originatingUniqueId="
					+ originatingUniqueId + ", transactionId=" + transactionId + ", message=" + message
					+ ", response_code=" + response_code + "]";
		}

	}

}
