package com.youauto.smstrans.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SmsResponse {

    private String code;

    private int size;

    private String message;

    @SerializedName("data")
    private List<Sms> smsList;

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

    public List<Sms> getSmsList() {
        return smsList;
    }

    public void setSmsList(List<Sms> smsList) {
        this.smsList = smsList;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
