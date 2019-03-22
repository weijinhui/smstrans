package com.youauto.smstrans.vo;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;

public class MySendSmsRequest extends SendSmsRequest {
	private String secretSignal;

	public String getSecretSignal() {
		return secretSignal;
	}

	public void setSecretSignal(String secretSignal) {
		this.secretSignal = secretSignal;
	}

}
