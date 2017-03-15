/** 
 * Project Name:ytj-manage-common 
 * File Name:SharpCommon.java 
 * Package Name:com.ghit.common 
 * Date:2017年2月16日下午4:30:45  
*/

package com.ghit.framework.provider.sysmanager.supports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ghit.framework.commons.utils.bean.BeanUtils;
import com.ghit.framework.commons.utils.security.AuthenticationType;
import com.ghit.framework.commons.utils.security.RSATools;
import com.ghit.framework.commons.utils.security.model.IUser;
import com.ghit.framework.commons.utils.security.model.SecurityDepartment;
import com.ghit.framework.commons.utils.security.model.SecurityJurisdiction;
import com.ghit.framework.commons.utils.security.model.SecurityRole;
import com.ghit.framework.commons.utils.security.model.SecurityUser;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Jurisdiction;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Role;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.User;
import com.ghit.framework.provider.sysmanager.api.supports.security.context.ContextContainer;

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

    public static PS sharp() {
        if (sharp == null)
            sharp = new PS();
        return sharp;
    }

    public static IUser convertUserToIUser(User user) {
        SecurityUser secUser = new SecurityUser();
        secUser.setUserName(user.getFullname());
        secUser.setId(user.getId());
        secUser.setUserType(user.getUserType());
        if (user.getRoles() != null) {
            List<SecurityRole> secRoles = new ArrayList<SecurityRole>();
            for (Role role : user.getRoles()) {
                if (role.getJurisdictions() != null) {
                    SecurityRole secRole = new SecurityRole();
                    List<SecurityJurisdiction> secJurisList = new ArrayList<SecurityJurisdiction>();
                    for (Jurisdiction juris : role.getJurisdictions()) {
                        SecurityJurisdiction secJuris = new SecurityJurisdiction();
                        secJuris.setCode(juris.getJurisdictionCode());
                        secJuris.setRule(juris.getAuthenticationRule());
                        if (juris.getAuthenticationType() != null)
                            secJuris.setType(AuthenticationType.valueOf(juris.getAuthenticationType()));
                        secJurisList.add(secJuris);
                    }
                    secRole.setJurisdictions(secJurisList);
                    secRoles.add(secRole);
                }
            }
            secUser.setRoles(secRoles);
        }
        SecurityDepartment deparment = new SecurityDepartment();
        secUser.setDepartment(deparment);
        deparment.setId("-1");
        if (user.getOrganization() != null) {
            deparment.setCode(user.getOrganization().getCode());
            deparment.setId(user.getOrganization().getId());
            deparment.setName(user.getOrganization().getName());
        }
        secUser.setData(BeanUtils.map("loginName", user.getUserName()));
        return secUser;

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

}
