package com.youauto.smstrans.vo;/**
 * @Auther: Administrator
 * @Date: 2019/3/22 0022 09:00
 * @Description:
 */

/**
 * @program: smstrans
 *
 * @description:
 *
 * @author: Carson Wei
 *
 * @create: 2019-03-22 09:00
 **/
public class Sms {
    private String phoneNum;
    private String content;
    private String lsh;
    private String startWaitTime;
    private String sendSatus;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLsh() {
        return lsh;
    }

    public void setLsh(String lsh) {
        this.lsh = lsh;
    }

    public String getStartWaitTime() {
        return startWaitTime;
    }

    public void setStartWaitTime(String startWaitTime) {
        this.startWaitTime = startWaitTime;
    }

    public String getSendSatus() {
        return sendSatus;
    }

    public void setSendSatus(String sendSatus) {
        this.sendSatus = sendSatus;
    }
}
