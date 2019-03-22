package com.youauto.smstrans.vo;/**
 * @Auther: Administrator
 * @Date: 2019/3/22 0022 12:02
 * @Description:
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: smstrans初始化资源
 *
 * @description:
 *
 * @author: Carson Wei
 *
 * @create: 2019-03-22 12:02
 **/
@Component
public class AppProperties {

    @Value("${smstrans.sms_sign_name}")
    private String signName;

    @Value("${smstrans.sms_accessKeyId}")
    private String accessKeyId;

    @Value("${smstrans.sms_accessKeySecret}")
    private String accessKeySecret;

    @Value("${smstrans.api_host}")
    private String apiHost;

    @Value("${smstrans.api_key_secret}")
    private String apiKeySecret;

    @Value("${smstrans.sms_code}")
    private String smsCode;

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getApiHost() {
        return apiHost;
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    public String getApiKeySecret() {
        return apiKeySecret;
    }

    public void setApiKeySecret(String apiKeySecret) {
        this.apiKeySecret = apiKeySecret;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
