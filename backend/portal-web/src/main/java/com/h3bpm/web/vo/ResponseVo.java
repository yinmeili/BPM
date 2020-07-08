package com.h3bpm.web.vo;

import com.h3bpm.web.enumeration.ErrorCode;

public class ResponseVo {
	private int errorCode = 200;
	private String msg = "调用成功";

	private Object data = null;

	@Deprecated
	public ResponseVo() {

	}

	public ResponseVo(ErrorCode errorCode) {
		this.errorCode = errorCode.getValue();
		this.msg = errorCode.getDisplayName();
	}

	public ResponseVo(Object data) {
		this.data = data;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
