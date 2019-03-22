package com.youauto.smstrans.tool;

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密加密类
 * Created by mominqiang on 2018/3/22.
 */
public class CryptTool {

    /**
     * 对targetString进行sha1加密
     * @param targetString
     * @return
     */
    public static String getSha1(String targetString){
        return DigestUtils.sha1Hex(targetString);
    }


    /**使用AES对字符串加密
     * @param str 待加密数据
     * @param key 密钥（16字节）
     * @return 加密结果
     * @throws Exception
     */
    public static String aesEncrypt(String str, String key) throws Exception {
        if (str == null || key == null) return null;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        byte[] bytes = cipher.doFinal(str.getBytes("utf-8"));
         return parseByte2HexStr(bytes);

    }
    /**使用AES对数据解密
     * @param str 待解密数据
     * @param key 密钥（16字节）
     * @return 解密结果
     * @throws Exception
     */
    public static String aesDecrypt(String str, String key) throws Exception {
        byte[] bytes = parseHexStr2Byte(str);
        if (bytes == null || key == null) return null;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        bytes = cipher.doFinal(bytes);
        return new String(bytes, "utf-8");
    }


    /**
     * 将二进制转换成16进制
     * @method parseByte2HexStr
     * @param buf
     * @return
     * @throws
     * @since v1.0
     */
    public static String parseByte2HexStr(byte buf[]){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < buf.length; i++){
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     * @method parseHexStr2Byte
     * @param hexStr
     * @return
     * @throws
     * @since v1.0
     */
    public static byte[] parseHexStr2Byte(String hexStr){
        if(hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}
