package com.youauto.smstrans.tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/27.
 */
@Component
public class JsonTool {

    @Autowired
    private Gson gson;
    Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
    Gson gsonDateFormat = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); //按照 yyyy-MM-dd HH:mm:ss格式化。

    JsonParser jp = new JsonParser();

    public Map<String,String> jsonToSimpleMap(String jsonData){
        try{
            Map<String,String> jsonMap = this.gson.fromJson(jsonData, new TypeToken<Map<String,String>>(){}.getType());
            return jsonMap;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public String mapToJsonString(Map<String,String> map){
        try{
            String resultString = this.gson.toJson(map);
            return resultString;
        }catch(Exception e){
            e.printStackTrace();
            return  null;

        }
    }

    public String objectMapToJsonString(Map<String,Object> map){
        try{
            String resultString = this.gson.toJson(map);
            return resultString;
        }catch(Exception e){
            e.printStackTrace();
            return  null;

        }
    }

    public String mapToJsonStringWithUrl(Map<String,String> map){
        String resultJson = null;
        try{
            Gson gson =new GsonBuilder()
                    .disableHtmlEscaping()
                    .create();
            resultJson =  gson.toJson(map);
        }catch (Exception e){
            e.printStackTrace();

        }
        return resultJson;
    }

    public String objectMapToJsonStringWithUrl(Map<String,Object> map){
        String resultJson = null;
        try{
            Gson gson =new GsonBuilder()
                    .disableHtmlEscaping()
                    .create();
            resultJson =  gson.toJson(map);
        }catch (Exception e){
            e.printStackTrace();

        }
        return resultJson;
    }

    public String getSimpleMsgJson(String msg, String code){
        Map<String,String> map = new HashMap<String, String>();
        map.put("msg",msg+"");
        map.put("code",code);
        return gson.toJson(map);
    }

    public String getSuccessDataJson(Map<String,String> map, String successMsg){
        if(null == map){
            return null;
        }
        map.put("code","0");
        map.put("msg",successMsg);
        return gson.toJson(map);
    }
    public String getSuccessDataJsonObj(Map<String,Object> map, String successMsg){
        if(null == map){
            return null;
        }
        map.put("code","0");
        map.put("msg",successMsg);
        return gson.toJson(map);
    }

    public String getSuccessDataJsonWithFormatDate(Map<String,String> map, String successMsg){
        if(null == map){
            return null;
        }
        map.put("code","0");
        map.put("msg",successMsg);
        return gsonDateFormat.toJson(map);

    }

    public String getSuccessDataJsonWithFormatDateObj(Map<String,Object> map, String successMsg){
        if(null == map){
            return null;
        }
        map.put("code","0");
        map.put("msg",successMsg);
        return gsonDateFormat.toJson(map);

    }



    public Gson getGson(){
        return gson;
    }
    public Gson getDateFormatGson(){return gsonDateFormat;}


    public String uglyJsonStringToPrettyJsonString(String uglyJsonString){
        JsonElement je = jp.parse(uglyJsonString);
        String prettyJsonString = gsonPretty.toJson(je);
        return prettyJsonString;
    }

    public String objectToJsonString(Object obj){

        return gson.toJson(obj);

    }

    public String objectToJsonStringNullToBlank(Object obj){

        String resultJson = null;
        try{
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .create();
            resultJson =  gson.toJson(obj);
        }catch (Exception e){
            e.printStackTrace();

        }
        return resultJson;
    }

    public Map<String,Object> jsonToObjectMap(String jsonData){
        try{
            Map<String,Object> jsonMap = this.gson.fromJson(jsonData, new TypeToken<Map<String,Object>>(){}.getType());
            return jsonMap;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Map<String, Object>> jsonToListMap(String jsonData)throws Exception {

        try{
            List<Map<String,Object>> jsonListMap = this.gson.fromJson(jsonData,
                    new TypeToken<List<Map<String, Object>>>(){}.getType());
            return jsonListMap;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("json数据格式不正确！");
        }
    }
}
