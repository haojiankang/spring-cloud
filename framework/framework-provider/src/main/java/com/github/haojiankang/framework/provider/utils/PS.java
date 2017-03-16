/** 
 * Project Name:ytj-manage-common 
 * File Name:SharpCommon.java 
 * Package Name:com.ghit.common 
 * Date:2017年2月16日下午4:30:45  
*/

package com.github.haojiankang.framework.provider.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.haojiankang.framework.commons.utils.security.RSATools;
import com.github.haojiankang.framework.commons.utils.security.context.ContextContainer;
import com.github.haojiankang.framework.commons.utils.security.model.IUser;

/**
 * ClassName:SharpCommon <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年2月16日 下午4:30:45 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class PS {
    protected static final Log  LOGGER= LogFactory.getLog(PS.class);
    private static PS sharp;
    static ThreadLocal<IUser> currentUser;
    static {
        currentUser = new ThreadLocal<IUser>();
    }

    public static PS sharp() {
        if (sharp == null)
            sharp = new PS();
        return sharp;
    }

    

    /**
     * 
     * error:存放错误消息. 同一个线程环境下可以跨越层传递消息
     * 
     * @author ren7wei
     * @param message
     * @return 工具类本身，用来实现链式调用
     * @since JDK 1.8
     */
    public static PS error(String message) {
        ContextContainer.getContainer().getContext().appendBind(ProviderConstant.TCONTEXT_MESSAGE_ERROR, message);
        return sharp();
    }

    /**
     * 
     * error:获取错误消息.同一个线程环境下可以跨越层传递消息
     *
     * @author ren7wei
     * @return 调用之前存放的错误消息
     * @since JDK 1.8
     */
    public static String error() {
        return ContextContainer.getContainer().getContext().lookup(ProviderConstant.TCONTEXT_MESSAGE_ERROR);
    }

    public static void clearError() {
        ContextContainer.getContainer().getContext().unbind(ProviderConstant.TCONTEXT_MESSAGE_ERROR);
    }

    /**
     * 
     * message:获取提示消息.同一个线程环境下可以跨越层传递消息
     *
     * @author ren7wei
     * @return 调用之前存放的错误消息
     * @since JDK 1.8
     */
    public static String message() {
        return ContextContainer.getContainer().getContext().lookup(ProviderConstant.TCONTEXT_MESSAGE_INFO);
    }

    /**
     * 
     * error:存放错误消息. 同一个线程环境下可以跨越层传递消息
     * 
     * @author ren7wei
     * @param message
     * @return 工具类本身，用来实现链式调用
     * @since JDK 1.8
     */
    public static PS message(String message) {
        ContextContainer.getContainer().getContext().appendBind(ProviderConstant.TCONTEXT_MESSAGE_INFO, message);
        return sharp();
    }

    /**
     * 
     * jsonToT:将JSON格式的字符串转换为指定的类型的实体
     *
     * @author ren7wei
     * @param c
     *            指定的类型
     * @param jsonStr
     *            JSON格式的字符串
     * @return
     * @since JDK 1.8
     */
    public static <T extends Object> T jsonToT(Class<T> c, String jsonStr) {
        ObjectMapper ojbmapper = new ObjectMapper();
        ojbmapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        ojbmapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        try {
            return ojbmapper.readValue(jsonStr, c);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }

    public static Object strToJsonObj(String jsonStr) {
        ObjectMapper ojbmapper = new ObjectMapper();
        ojbmapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        ojbmapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        try {
            return ojbmapper.readTree(jsonStr);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }

    public static String objToJson(Object obj) {
        ObjectMapper ojbmapper = new ObjectMapper();
        ojbmapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        ojbmapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        try {
            return ojbmapper.writeValueAsString(obj);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }


    /**
     * 
     * backAjaxMessage:返回ajax
     *
     * @author ren7wei
     * @param state
     * @param message
     * @return
     * @since JDK 1.8
     */
    public static Map<String, String> backAjaxMessage(boolean state, String message) {
        Map<String, String> map = new HashMap<>();
        map.put("state", String.valueOf(state));
        map.put("message", message);
        return map;
    }

    /**
     * 
     * rsaToHash:对字符串进行RSA解码并进行URL转码，并将转码后的字符串进行hash处理。 RSA私钥使用默认的私钥
     * 
     * @author ren7wei
     * @param str
     *            被解密的字符串
     * @return 解密后的hash格式
     * @since JDK 1.8
     */
    public static String UrlRSAToHash(String str, String append) {
        return md5(UrlRSADecrypt(str) + append);
    }

    /**
     * 
     * UrlRSADecrypt:对字符串进行RSA解密并进行URL转码 。 RSA私钥使用默认的私钥
     * 
     * @author ren7wei
     * @param str
     * @return
     * @since JDK 1.8
     */
    public static String UrlRSADecrypt(String str) {
        try {
            return URLDecoder.decode(RSATools.decryptStringByJs(str), "UTF-8");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            error("解密过程发生错误，请联系后台管理员!");
        }
        return null;
    }

    /**
     * 
     * md5:返回字符串的md5值.
     *
     * @author ren7wei
     * @param str
     * @return
     * @since JDK 1.8
     */
    public static String md5(String str) {
        // 实际返回的为MD5值
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    /**
     * 对输入流进行散列运算, 支持md5与sha1算法.
     * 
     * @param input
     *            待运算输入流
     * @param algorithm
     *            MD2/MD5/SHA-1/SHA-256/SHA-384/SHA-512
     * @return 散列运算得到的字节数组
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] digest(InputStream input, String algorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        int read = -1;
        int bufferLength = 1024 * 1024 * 10;
        byte[] buffer = new byte[bufferLength];
        do {
            read = input.read(buffer, 0, bufferLength);
            if (read > -1)
                messageDigest.update(buffer, 0, read);
        } while (read > -1);
        return messageDigest.digest();
    }
    public static final String JSESSIONID = "hsessionid";
    public static final String TOKEN = "htoken";
    public static IUser currentUser(){
        return currentUser.get();
    }
    public static void currentUser(IUser user){
        currentUser.set(user);
    }
}
