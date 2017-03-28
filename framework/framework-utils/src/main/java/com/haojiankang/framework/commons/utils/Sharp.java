/** 
 * Project Name:ytj-manage-common 
 * File Name:SharpCommon.java 
 * Package Name:com.ghit.common 
 * Date:2017年2月16日下午4:30:45  
*/

package com.haojiankang.framework.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
public class Sharp {
    protected static final Log LOGGER = LogFactory.getLog(Sharp.class);
    private static Sharp sharp;

    public static Sharp sharp() {
        if (sharp == null)
            sharp = new Sharp();
        return sharp;
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
     * serialize:序列化对象为byte数组
     *
     * @author ren7wei
     * @param obj
     * @return
     * @since JDK 1.7
     */
    public static byte[] serialize(Object obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
            try (ObjectOutputStream os = new ObjectOutputStream(bos);) {
                os.writeObject(obj);
                os.flush();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            byte[] b = bos.toByteArray();
            return b;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 
     * deserialize:反序列化byte为对象.
     *
     * @author ren7wei
     * @param bs
     * @return
     * @since JDK 1.7
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bs) {
        try (ByteArrayInputStream ins = new ByteArrayInputStream(bs);) {
            try (ObjectInputStream in = new ObjectInputStream(ins);) {
                return (T) in.readObject();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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

}
