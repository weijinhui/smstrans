package com.youauto.smstrans.tool;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.youauto.smstrans.vo.AppProperties;
import com.youauto.smstrans.vo.MySendSmsRequest;
import com.youauto.smstrans.vo.MySendSmsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Component
public class AlibabaSMSTool {
    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    @Autowired
    private JsonTool jsonTool;

    @Autowired
    private AppProperties appProperties;

    Logger logger = LoggerFactory.getLogger(AlibabaSMSTool.class);

    public MySendSmsResponse sendSms(String phoneNumbers, String signName, String templateCode, Map<String,String> paramMap, String outId, String secretSignal) throws ClientException {
     //3、将json对象转化为json字符串
     String templateParam = jsonTool.mapToJsonString(paramMap);
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        String accessKeyId = appProperties.getAccessKeyId();
		String accessKeySecret = appProperties.getAccessKeySecret();
//		System.out.println(accessKeyId);
//		System.out.println(accessKeySecret);
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        //SendSmsRequest request = new SendSmsRequest();
        MySendSmsRequest request= new MySendSmsRequest();
        request.setSecretSignal(secretSignal);
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumbers);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
        request.setTemplateParam(templateParam);
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId(outId);

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        MySendSmsResponse mySendSmsResponse= new MySendSmsResponse();
        mySendSmsResponse.setSecretSignal(CryptTool.getSha1(secretSignal));
        mySendSmsResponse.setSendSmsResponse(sendSmsResponse);
        logger.info("发送短信信息，模板:{}，内容:{}",templateCode,  templateParam);
        return mySendSmsResponse;
    }


    public  QuerySendDetailsResponse querySendDetails(String bizId,String phoneNumber) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//        AlibabaMsgConf  a= new AlibabaMsgConf();
//        String accessKeyId=alibabaMsgConf.getAccessKeyId();
//        String accessKeySecret=alibabaMsgConf.getAccessKeySecret();
        String accessKeyId=appProperties.getAccessKeyId();
        String accessKeySecret=appProperties.getAccessKeySecret();
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(phoneNumber);
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

    public  String creatVerificationCode(){
    	String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    	StringBuilder sb=new StringBuilder(4);
    	for(int i=0;i<4;i++)
    	{
    	char ch=str.charAt(new Random().nextInt(str.length()));
    	sb.append(ch);
    	}
		return  sb.toString();
    	
    }
    
/*    public  int getRandNum(int min, int max) {
        int randNum = min + (int)(Math.random() * ((max - min) + 1));
        return randNum;
    }*/
    
    public String getRandNum(){
    	String sRand = "";
    	for (int i=0;i<6;i++){ 
    		Random random = new Random();
        	String rand=String.valueOf(random.nextInt(10));    
        	sRand+=rand;    
        }
		return sRand;	
    }
    

}
