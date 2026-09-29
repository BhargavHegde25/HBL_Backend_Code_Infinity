package com.bct.javaservices;

import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public class QRErrorCode {
	public enum ErrorCode {
		ERR_1000("1000", "Invalid QR Input"),

		ERR_1010("1010", "Invalid QR Base64 String"),

		ERR_1014("1014", "Sorry! Beneficiary account does not exists."),

		ERR_1015("1015", "Sorry! Beneficiary account name mismatch."),

		ERR_1016("1016",
				"Some difference in beneficiary account name observed. Transaction once sent is irreversible, please reconfirm the beneficiary account number."),

		ERR_1017("1017", "Sorry! Beneficiary Bank not reachable at the moment. Please try again later."),

		ERR_1018("1018",
				"Sorry! Transaction not allowed on the beneficiary Account.Please check with beneficiary bank."),

		ERR_1019("1019", "Sorry! Bank not reachable."),

		ERR_1011("1011", "Sorry! Given QR is not generated properly. Please try again with updated QR Code."),

		ERR_1012("1012", "Account Number is not valid"),

		ERR_1020("1020", "Sorry! Account info didn't match. Please check account name and number."),

		ERR_1021("1021", "Sorry! Beneficiary Bank not reachable at the moment. Please try again later.");

		private final String code;
		private final String message;

		ErrorCode(String code, String message) {
			this.code = code;
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		public Result setErrorCode(Result result) {
			if (result == null) {
				result = new Result();
			}
			result.addParam(new Param("dbpErrCode", this.getCode(), FabricConstants.STRING));
			result.addParam(new Param("dbpErrMsg", this.getMessage(), FabricConstants.STRING));
			result.addParam(new Param("success", "false"));
			return result;
		}
	}

}
