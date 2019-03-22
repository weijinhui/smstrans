package com.youauto.smstrans.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**发送https请求的工具类
 * Created by Administrator on 2016/9/27.
 */
@Component
public class HttpTool {


    @Autowired
    private IoTool ioTool;

    private static final Logger logger =  LoggerFactory.getLogger(HttpTool.class);

    /**
     * 忽视证书HostName
     */
    private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String s, SSLSession sslsession) {
            System.out.println("WARNING: Hostname is not matched for cert.");
            return true;
        }
    };

    /**
     * Ignore Certification
     */
    private static TrustManager ignoreCertificationTrustManger = new X509TrustManager(){
        private X509Certificate[] certificates;
        public void checkClientTrusted(X509Certificate certificates[],
                                       String authType) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = certificates;
            }
        }
        public void checkServerTrusted(X509Certificate[] ax509certificate,
                                       String s) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = ax509certificate;
            }
        }
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return new X509Certificate[0];
        }
    };

    public String sendSSLGetMethod(String urlString) throws Exception {
        String repString = null;
        InputStream is = null;
        HttpsURLConnection connection = null;
        try {

            URL url = new URL(urlString);
            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            connection = (HttpsURLConnection) url.openConnection();
            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);
            if(connection.getResponseCode() != 200){

            }
            is = connection.getInputStream();
            repString = ioTool.getStringFromInputStream(is);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if(null != is){
                is.close();
                is = null;
            }
            if(null != connection){
                connection.disconnect();
            }
        }
        return repString;
    }

    public String sendSSLPostMethod(String urlString, String postData) throws Exception {
        String repString = null;
        InputStream is = null;
        HttpsURLConnection connection = null;
        try {

            URL url = new URL(urlString);
            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type","text/json");
            connection.setRequestProperty("content-length",String.valueOf(postData.getBytes().length));
            connection.getOutputStream().write(postData.getBytes("utf-8"));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);
            if(connection.getResponseCode() != 200){

            }
            is = connection.getInputStream();
            repString = ioTool.getStringFromInputStream(is);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if(null != is){
                is.close();
                is = null;
            }
            if(null != connection){
                connection.disconnect();
            }
        }
        return repString;
    }


    /**
     * 上传文件到微信服务器
     * @param urlString 上传的目标url
     * @param filePath 文件路路径
     * @Param formDataName 表单id
     * @return
     * @throws Exception
     */
    public String sendSSLMutiPartFormData(String urlString, String filePath, String formDataName) throws Exception {
        String repString = null;
        InputStream is = null;
        OutputStream out = null;
        HttpsURLConnection connection = null;
        final String BOUNDARYSTR = ""+System.currentTimeMillis();
        final String BOUNDARY = "--"+BOUNDARYSTR+"\r\n";
        try{
            File file = new File(filePath);
            if(!file.exists() || !file.isFile()){
                String errorMsg = "文件["+filePath+"]不存在。无法上传。";
                logger.error(errorMsg);
                throw new Exception(errorMsg);
            }
            URL url = new URL(urlString);
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            // 设置请求头信息
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");

            connection.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARYSTR);
            StringBuilder sb = new StringBuilder();
            sb.append(BOUNDARY);
            sb.append("Content-Disposition: form-data;name=\""+formDataName+"\";filename=\""
                    + file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes("utf-8");
            // 获得输出流
            out = new DataOutputStream(connection.getOutputStream());
            // 输出表头
            out.write(head);
            // 文件正文部分
            // 把文件已流文件的方式 推入到url中
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = bis.read(bufferOut,0,1024)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            bis.close();
            byte[] foot = ("\r\n--" + BOUNDARYSTR + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
            out.write(foot);
            out.flush();
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);
            if(connection.getResponseCode() != 200){

            }
            is = connection.getInputStream();
            repString = ioTool.getStringFromInputStream(is);
        }catch(Exception ex){
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }finally {
            if(null != is){
                is.close();
                is = null;
            }
            if(null != connection){
                connection.disconnect();
            }
        }
        return repString;
    }


    /**
     *
     * @param urlString
     * @return
     */
    public Map<String,Object> sendSSLGetDownloadMedia(String urlString){
        String fileName = null;
        byte[] repData = null;
        InputStream is = null;
        Map<String,Object> resultInfo = null;
        HttpsURLConnection connection = null;
        try {

            URL url = new URL(urlString);
            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            connection = (HttpsURLConnection) url.openConnection();
            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);

            /**从以下头部数据解析出文件名
             * Content-disposition: attachment; filename="MEDIA_ID.jpg"
             */
            String contentDisposition = connection.getHeaderField("Content-disposition");
            if(contentDisposition != null){
                String[] contentDispositionArray = contentDisposition.split(";");
                for(String content:contentDispositionArray){
                    if(content.contains("filename")){
                        String[] contentArry = content.split("=");
                        fileName = contentArry[1];
                        fileName = fileName.replaceAll("\"","");
                    }
                }
            }
            if(connection.getResponseCode() != 200){

            }
            is = connection.getInputStream();
            repData = this.ioTool.getByteArrayFromInputStream(is);
            resultInfo = new HashMap<String,Object>();
            resultInfo.put("fileName",fileName);
            resultInfo.put("data",repData);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if(null != is){
                try{
                    is.close();
                    is = null;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            if(null != connection){
                connection.disconnect();
            }
        }
        return resultInfo;
    }

    /**
     *
     * @param urlString
     * @param postData
     * @return
     */
    public Map<String,Object> sendSSLPostDownloadMedia(String urlString, String postData){
        String fileName = null;
        byte[] repData = null;
        InputStream is = null;
        Map<String,Object> resultInfo = null;
        HttpsURLConnection connection = null;
        try{
            URL url = new URL(urlString);
            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type","text/json");
            connection.setRequestProperty("content-length",String.valueOf(postData.getBytes().length));
            connection.getOutputStream().write(postData.getBytes("utf-8"));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);

            /**从以下头部数据解析出文件名
             * Content-disposition: attachment; filename="MEDIA_ID.jpg"
             */
            String contentDisposition = connection.getHeaderField("Content-disposition");
            String[] contentDispositionArray = contentDisposition.split(";");
            for(String content:contentDispositionArray){
                if(content.contains("filename")){
                    String[] contentArry = content.split("=");
                    fileName = contentArry[1];
                    fileName = fileName.replaceAll("\"","");
                }
            }
            if(connection.getResponseCode() != 200){

            }
            is = connection.getInputStream();
            repData = this.ioTool.getByteArrayFromInputStream(is);
            resultInfo = new HashMap<String,Object>();
            resultInfo.put("fileName",fileName);
            resultInfo.put("data",repData);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }finally {
            if(null != is){
                try{
                    is.close();
                    is = null;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(null != connection){
                connection.disconnect();
            }
        }
        return resultInfo;

    }


    /**
     *
     * @param urlString
     * @return
     */
    public String sendGetMethod(String urlString){
        String repString = null;
        InputStream is = null;
        URLConnection connection = null;
        try {

            URL url = new URL(urlString);
            connection = url.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            is = connection.getInputStream();
            repString = ioTool.getStringFromInputStream(is);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if(null != is){
                try{
                    is.close();
                    is = null;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        return repString;
    }


    /**
     *
     * @param urlString
     * @param postData
     * @return
     */
    public String sendPostMethod(String urlString, String postData){
        String repString = null;
        InputStream is = null;
        URLConnection connection = null;
        try {

            URL url = new URL(urlString);
            connection = url.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("content-type","text/json");
            connection.getOutputStream().write(postData.getBytes("utf-8"));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            connection.getOutputStream().close();
            is = connection.getInputStream();
            repString = ioTool.getStringFromInputStream(is);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if(null != is){
                try{
                    is.close();
                    is = null;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        return repString;
    }

    /**
     *
     * @param urlString
     * @return
     */
    public byte[] sendGetMethodGetImg(String urlString){
        byte[] rep = null;
        InputStream is = null;
        URLConnection connection = null;
        try {

            URL url = new URL(urlString);
            connection = url.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            is = connection.getInputStream();
            rep = ioTool.getByteArrayFromInputStream(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(null != is){
                try{
                    is.close();
                    is = null;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        return rep;
    }

    /**
     * 制作多参数多文件消息并进行请求
     * @param urlString 目标url
     * @param mutiFileList 含有文件信息的List<map>,最简单的情况是包含文件名称和路径。
     * @param params 普通参数map，key作为参数名称,value作为参数值
     * @return
     */
    public String httpUploadMutiFile(String urlString, List<Map<String, Object>> mutiFileList, Map<String, String> params){
        String repString = null;
        InputStream is = null;
        OutputStream out = null;
        //定义boundarystr
        final String BOUNDARYSTR = Long.toHexString(System.currentTimeMillis());
        //定义回车换行
        final String CRLF = "\r\n";
        final String BOUNDARY = "--"+BOUNDARYSTR+CRLF;
        StringBuilder paramsText = new StringBuilder();
        byte[] paraTextByte;
        try{
            //首先制作普通参数信息
            if(params != null && params.size() != 0){

                for(String key:params.keySet()){
                    paramsText.append(BOUNDARY);
                    paramsText.append("Content-Disposition: form-data;name=\""+key+"\""+CRLF);
                    paramsText.append("Content-type: text/plain"+CRLF+CRLF);
                    paramsText.append(params.get(key));
                    paramsText.append(CRLF);
                }
                paraTextByte =  paramsText.toString().getBytes("UTF-8");
            }else{
                //
                paraTextByte = null;
            }
        }catch (Exception e){
            e.printStackTrace();
            paraTextByte = null;
        }
        //写入参数部分信息
        try{
            //先制作请求头
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARYSTR);
            // 获得输出流
            out = new DataOutputStream(con.getOutputStream());
            // 输出参数部分
            if(paraTextByte != null && paraTextByte.length > 0){
                out.write(paraTextByte);
            }
            //写入文件信息
            Map<String, Object> fileInfoMap;
            for(int i = 0; i < mutiFileList.size(); i++){
                fileInfoMap = mutiFileList.get(i);
                //当前文件map存有文件名称(uuid型)和文件的中文名称，以及文件的字节数据
                // 如果文件过大，不建议使用该方式。
                String fileName = (String)fileInfoMap.get("fileName");
                String suffix = fileName.substring(fileName.indexOf("."));
                String chFileName = fileInfoMap.get("chName")+suffix;
                StringBuilder sb = new StringBuilder();
                sb.append(BOUNDARY);
                sb.append("Content-Disposition: form-data;name=\""+chFileName+"\";filename=\""
                        + chFileName + "\""+CRLF);
                //sb.append("Content-Type:application/octet-stream"+CRLF+CRLF);
                //文件均是jpg图片类型
                sb.append("Content-Type:image/jpg"+CRLF+CRLF);
                out.write(sb.toString().getBytes());

                //写入输出流
                byte[] fileData = (byte[])fileInfoMap.get("data");
                /**
                 * 如果这里存储的是文件路径，那么可以使用FileInputStream和ByteArrayOutputStream转换成byte[]。
                 * 具体就不写了
                 */
                out.write(fileData);
                out.write(CRLF.getBytes("UTF-8"));
            }
            byte[] foot = ("--" + BOUNDARYSTR + "--"+CRLF).getBytes("UTF-8");// 定义最后数据分隔线
            out.write(foot);
            out.flush();
            is = con.getInputStream();
            repString = ioTool.getStringFromInputStream(is);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("往业务系统写入文件失败:"+e.getMessage());
        }
        return repString;

    }

    /**
     * 判断某个请求是否是异步的
     * @param request
     * @return
     */
    public boolean isAsynchronousRequest(HttpServletRequest request){

        //Jquery的ajax请求默认会加上这个头部
        String jQueryAjaxHeader = request.getHeader("x-requested-with");
        //原生js使用ajax请加上一个头部参数请求头部:XMLHttpRequest.setRequestHeader("RequestType","AJAX");
        String customAjaxHeader = request.getHeader("RequestType");
        if((!StringUtils.isEmpty(jQueryAjaxHeader) && jQueryAjaxHeader.equals("XMLHttpRequest"))
                || (!StringUtils.isEmpty(customAjaxHeader) && customAjaxHeader.equals("AJAX")) ){
            return  true;
        }
        return false;
    }

    public String sendPostByRequestPropertyMap(String url, String param, Map<String,String> RequestPropertyMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //设置通用的请求属性
            for (Map.Entry<String, String> entry : RequestPropertyMap.entrySet()) {

                conn.setRequestProperty(entry.getKey(), entry.getValue());

            }
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
//	            conn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
//	            conn.setRequestProperty("cookie", cookie);
//	            conn.setRequestProperty("Connection", " keep-alive");
//	            conn.setRequestProperty("Content-Length", "82");
//	            conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
//	            conn.setRequestProperty("Origin", "http://gx.122.gov.cn");
//	            conn.setRequestProperty("Content-Type",  "application/x-www-form-urlencoded; charset=UTF-8");
//	            conn.setRequestProperty("X-Requested-With",  "XMLHttpRequest");
//	            conn.setRequestProperty("Referer",  "http://gx.122.gov.cn/views/inquiry.html?q=j");
//	            conn.setRequestProperty("Accept-Encoding",  "gzip, deflate");
//	            conn.setRequestProperty("Accept-Language",  "zh-CN,zh;q=0.8");
//	            conn.setRequestProperty("HTTP_X_FORWARDED_FOR", randomIp);
//	            conn.setRequestProperty("HTTP_CLIENT_IP", randomIp);
//	            conn.setRequestProperty("REMOTE_ADDR", randomIp);
//	            conn.setConnectTimeout(5000);
//	            conn.setReadTimeout(5000);
            // conn.setReadTimeout(1000);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            OutputStreamWriter outWriter = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            out = new PrintWriter(outWriter);
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));result += line;
            }
            String cookie = conn.getHeaderField("Set-Cookie");
            // System.out.println("cookiepost:" + cookie);
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
