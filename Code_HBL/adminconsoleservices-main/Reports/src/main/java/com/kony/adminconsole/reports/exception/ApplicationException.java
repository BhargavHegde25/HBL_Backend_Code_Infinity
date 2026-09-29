package com.kony.adminconsole.reports.exception;

import org.apache.commons.lang3.StringUtils;

import com.kony.adminconsole.reports.utilities.ErrorCodeEnum;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = -7060239598353504899L;
	private ErrorCodeEnum errorCodeEnum;

	public ApplicationException(ErrorCodeEnum errorCodeEnum) {
		this.errorCodeEnum = errorCodeEnum;
	}

	public ApplicationException(ErrorCodeEnum errorCodeEnum, Throwable cause) {
		super(cause);
		this.errorCodeEnum = errorCodeEnum;
	}

	public ErrorCodeEnum getErrorCodeEnum() {
		return errorCodeEnum;
	}

	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder();

		String message = super.getMessage();
		if (StringUtils.isNotBlank(message)) {
			builder.append(message);
		}

		builder.append(" [ErrorCode=").append(errorCodeEnum.getErrorCode()).append(", ").append(" Message=")
				.append(String.valueOf(errorCodeEnum.getMessage())).append("]");

		return builder.toString();
	}

}
