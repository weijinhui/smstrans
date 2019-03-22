package com.youauto.smstrans.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class BaseResponse {

    private String code;

    private int count;

    private String message;

    @SerializedName("data")
    private Map<String,Object> responseData;

    @SerializedName("dataArray")
    private List<Map<String, Object>> responseListData;


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

    public Map<String, Object> getResponseData() {
        return responseData;
    }

    public void setResponseData(Map<String, Object> responseData) {
        this.responseData = responseData;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Map<String, Object>> getResponseListData() {
        return responseListData;
    }

    public void setResponseListData(List<Map<String, Object>> responseListData) {
        this.responseListData = responseListData;
    }
}
