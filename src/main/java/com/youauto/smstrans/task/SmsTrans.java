package com.youauto.smstrans.task;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.youauto.smstrans.tool.*;
import com.youauto.smstrans.utils.BaseResponse;
import com.youauto.smstrans.vo.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: smstrans
 * @description:
 * @author: Carson Wei
 * @create: 2019-03-20 16:30
 **/
@Component
public class SmsTrans {
    @Autowired
    private AlibabaSMSTool alibabaSMSTool;
    @Autowired
    private JsonTool jsonTool;
    @Autowired
    private HttpTool httpTool;
    @Autowired
    private AppProperties appProperties;

    Logger logger = LoggerFactory.getLogger(SmsTrans.class);

    /**
    * @Description: 定时执行发送短信任务 
    * @Param: [] 
    * @return: void 
    * @Author: Carson Wei 
    * @Date: 2019/3/21 0021 10:00 
    */ 
    @Scheduled(fixedDelay = 15000)
    public void smsTransTask() throws Exception {
        //获取待发送列表
        List<Sms> smsList = getWaitSendList();
        List<String> smsSuccessList = new ArrayList();
        if(null!=smsList){
            for (Sms sms:smsList) {
                //开始发送短信
                BaseResponse baseResponse = sendSms(sms);
                if(baseResponse.getCode().equals("200")){
                    smsSuccessList.add(sms.getLsh());
               }
            }
        }
        //发送完成后，回调通知发送结果。
        if(smsSuccessList.size()>0){
            sendCallBack(smsSuccessList);
        }
    }
    
    /** 
    * @Description: 调用阿里云接口发送短信 
    * @Param: [sms] 
    * @return: com.youauto.smstrans.utils.BaseResponse
    * @Author: Carson Wei 
    * @Date: 2019/3/22 0022 10:31 
    */ 
    public BaseResponse sendSms(Sms sms){
        BaseResponse baseResponse = new BaseResponse();
        //调用接口，发送短信
        String smsTplCode = appProperties.getSmsCode();
        String signName = appProperties.getSignName();
        String phoneNum = sms.getPhoneNum();

        Map<String, Object> resultMap = new HashMap();
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("code",sms.getContent());
        MySendSmsResponse mySendSmsResponse;
        String secretSignal = "twgdh";///
        try {
            mySendSmsResponse = alibabaSMSTool.sendSms(phoneNum, signName, smsTplCode, paramMap, "", secretSignal);
            SendSmsResponse sendSmsResponse = mySendSmsResponse.getSendSmsResponse();
            if (sendSmsResponse.getMessage().toUpperCase().contains("ok".toUpperCase())) {
                resultMap.put("mySendSmsResponse", mySendSmsResponse);
                baseResponse.setCode("200");
                baseResponse.setMessage("发送短信成功，请查收！");
            } else if (sendSmsResponse.getMessage().contains("触发分钟级流控") && sendSmsResponse.getMessage().toUpperCase().contains("Permits".toUpperCase())) {
                baseResponse.setCode("401");
                baseResponse.setMessage("发送短信过于频繁，请稍后再试");
            } else if (sendSmsResponse.getMessage().contains("触发天级流控") && sendSmsResponse.getMessage().toUpperCase().contains("Permits".toUpperCase())) {
                baseResponse.setCode("401");
                baseResponse.setMessage("发送短信过于频繁，请明天再试");
            } else if (sendSmsResponse.getMessage().toUpperCase().contains("Permits".toUpperCase())) {
                baseResponse.setCode("401");
                baseResponse.setMessage("发送短信过于频繁，请稍后再试");
            } else {
                baseResponse.setCode("401");
                baseResponse.setMessage("发送短信失败，请重试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("发送短信失败！{}", e.getMessage());
            baseResponse.setCode("500");
            baseResponse.setMessage("发送短信失败，请重试！");
        }
        return baseResponse;
    }

    /**
    * @Description:获取待发送短信列表
    * @Param:
    * @return:
    * @Author: Carson Wei
    * @Date: 2019/3/21 0021 9:59
    */
    public List<Sms> getWaitSendList(){
        Map<String,String> apiMapData = new HashMap();
        apiMapData.put("key",getKey());
        apiMapData.put("method","obtainInfo");
        String jsonStr = this.jsonTool.mapToJsonString(apiMapData);
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("http");
        uriBuilder.setHost(appProperties.getApiHost());
        uriBuilder.setPath("/servlet/ReadMessageToSend");
        String url = null;
        try {
            url = uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String responseJson = httpTool.sendPostMethod(url, jsonStr);
        logger.info(responseJson);
        SmsResponse s = jsonTool.getGson().fromJson(responseJson,SmsResponse.class);
        List<Sms> smsList = s.getSmsList();

        return smsList;
    }
    
    /** 
    * @Description: 发送成功后回调，通知已发送 
    * @Param: [] 
    * @return: void 
    * @Author: Carson Wei 
    * @Date: 2019/3/22 0022 9:57
    */ 
    public void sendCallBack(List<String> list){

        Map<String,Object> apiMapData = new HashMap();
        apiMapData.put("key",getKey());
        apiMapData.put("method","sendCallBack");
        apiMapData.put("lsh",list);
        String jsonStr = this.jsonTool.objectMapToJsonString(apiMapData);
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("http");
        uriBuilder.setHost(appProperties.getApiHost());
        uriBuilder.setPath("/servlet/ReadMessageToSend");
        String url = null;
        try {
            url = uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String responseJson = httpTool.sendPostMethod(url, jsonStr);
        logger.debug(responseJson);
//        Map<String,Object> responseMap = jsonTool.jsonToObjectMap(responseJson);
//        if(String.valueOf(responseMap.get("code")).equals("0")){
//        }
    }

    public String getKey(){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd" );
        Date date = new Date();
        String dateStr = sdf.format(date);
        String key =  DigestUtils.md5Hex(dateStr+appProperties.getApiKeySecret());
        return key;
    }
}
