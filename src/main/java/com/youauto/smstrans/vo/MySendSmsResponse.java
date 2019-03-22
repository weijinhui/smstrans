package com.youauto.smstrans.vo;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;

public class MySendSmsResponse {
	private String secretSignal;
	private SendSmsResponse sendSmsResponse;
	public String getSecretSignal() {
		return secretSignal;
	}
	public void setSecretSignal(String secretSignal) {
		this.secretSignal = secretSignal;
	}
	public SendSmsResponse getSendSmsResponse() {
		return sendSmsResponse;
	}
	public void setSendSmsResponse(SendSmsResponse sendSmsResponse) {
		this.sendSmsResponse = sendSmsResponse;
	}

	
}
