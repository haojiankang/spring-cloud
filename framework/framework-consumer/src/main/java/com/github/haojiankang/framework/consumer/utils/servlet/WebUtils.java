/** 
 * Project Name:EHealthData 
 * File Name:WebUtils.java 
 * Package Name:com.ghit.common.security.web.filter 
 * Date:2016年6月21日下午3:32:12 
 * 
*/

package com.github.haojiankang.framework.consumer.utils.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.haojiankang.framework.commons.utils.excel.ExcelUtils;
import com.github.haojiankang.framework.commons.utils.excel.IRow;
import com.github.haojiankang.framework.commons.utils.lang.StringUtil;

/**
 * ClassName:WebUtils <br/>
 * Function: web工具类 <br/>
 * Date: 2016年6月21日 下午3:32:12 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.6
 * @see
 */
public class WebUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(WebUtils.class);

    /**
     * 
     * getIpAddress:获取请求的真实IP地址
     *
     * @author ren7wei
     * @param request
     * @return
     * @since JDK 1.7
     */
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null)
            return "unknown";
        String strIP = StringUtil.trimToEmpty(request.getHeader("x-forwarded-for"));
        if (strIP.length() < 1 || "unknown".equalsIgnoreCase(strIP))
            strIP = StringUtil.trimToEmpty(request.getHeader("X-Forwarded-For"));
        if (strIP.length() < 1 || "unknown".equalsIgnoreCase(strIP))
            strIP = StringUtil.trimToEmpty(request.getHeader("Proxy-Client-IP"));
        if (strIP.length() < 1 || "unknown".equalsIgnoreCase(strIP))
            strIP = StringUtil.trimToEmpty(request.getHeader("WL-Proxy-Client-IP"));
        if (strIP.length() < 1 || "unknown".equalsIgnoreCase(strIP))
            strIP = StringUtil.trimToEmpty(request.getHeader("X-Real-IP"));
        if (strIP.length() < 1 || "unknown".equalsIgnoreCase(strIP))
            strIP = StringUtil.trimToEmpty(request.getHeader("http_client_ip"));
        if (strIP.length() < 1 || "unknown".equalsIgnoreCase(strIP))
            strIP = StringUtil.trimToEmpty(request.getHeader("HTTP_CLIENT_IP"));
        if (strIP.length() < 1 || "unknown".equalsIgnoreCase(strIP))
            strIP = StringUtil.trimToEmpty(request.getHeader("HTTP_X_FORWARDED_FOR"));
        if (strIP.length() < 1 || "unknown".equalsIgnoreCase(strIP)) {
            strIP = request.getRemoteAddr();
            if (strIP.equals("127.0.0.1")) {// 根据网卡取本机配置的IP
                try {
                    InetAddress inet = InetAddress.getLocalHost();
                    strIP = inet.getHostAddress();
                } catch (UnknownHostException e) {
                }
            }
        }
        if (strIP.length() < 1 || "unknown".equalsIgnoreCase(strIP))
            return strIP;

        /*
         * 多级反向代理，x-forwarded-for的值并不止一个, 而是一串IP值,
         * 取x-forwarded-for中第一个非unknown的有效IP字符串为用户真实IP：
         * x-forwarded-for：192.168.1.110， 192.168.1.120， 192.168.1.130，
         * 192.168.1.100 真实IP : 192.168.1.110
         */
        String[] sArray = strIP.split(",");
        for (String str : sArray) {
            strIP = StringUtil.trimToEmpty(str);
            if (strIP.length() > 0 && !("unknown".equalsIgnoreCase(strIP))) {
                break;
            }
        }
        return strIP;
    }

    /**
     * 判断ajax请求
     * 
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null
                && "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With")));
    }

    /**
     * 
     * reponseJons:将obj转换为json并返回给客户端
     *
     * @author ren7wei
     * @param response
     *            http响应对象
     * @param obj
     *            响应数据
     * @since JDK 1.8
     */
    public static void reponseJons(HttpServletResponse response, Object obj) {
        ServletOutputStream sou = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(obj);
            byte[] content = json.getBytes("utf-8");

            response.setContentType("application/json; charset=utf-8");
            response.setContentLength(content.length);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            sou = response.getOutputStream();
            sou.write(content, 0, content.length);
            sou.flush();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                if (null != sou)
                    sou.close();
            } catch (IOException f) {
            }
        }
    }

    /**
     * 
     * getCookie:获取指定cookie信息
     *
     * @author ren7wei
     * @param request
     *            http请求对象
     * @param name
     *            cookie的name
     * @return cookie信息
     * @since JDK 1.8
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null)
            return Arrays.stream(request.getCookies()).filter(c -> {
                return StringUtil.eq(c.getName(), name);
            }).findFirst().orElse(null);
        else
            return null;
    }

    /**
     * 
     * getCookieValue:获取指定cookie的值.
     *
     * @author ren7wei
     * @param request
     *            http请求对象
     * @param name
     *            cookie的name属性
     * @return cookie的value值
     * @since JDK 1.8
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null)
            return cookie.getValue();
        return null;
    }

    /**
     * 
     * addCookie:给客户端增加cookie
     *
     * @author ren7wei
     * @param response
     *            http响应对象
     * @param name
     *            cookie的name值
     * @param value
     *            cookie的value值
     * @param path
     *            cookie的path值
     * @param maxAge
     *            cookie的maxAge值
     * @param secure
     *            cookie的secure值
     * @param httpOnly
     *            cookie的httpOnly值
     * @since JDK 1.8
     */
    public static void addCookie(HttpServletResponse response, String name, String value, String path, int maxAge,
            boolean secure, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secure);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

    /**
     * <p>
     * 发送Excel流
     * <p>
     * 
     * @param list
     *            数据行集合
     * @param fileName
     *            文件名字
     * @throws Exception
     */
    public static void sendExcel(List<? extends IRow> list, String fileName, HttpServletResponse response) {
        try {
            OutputStream os;
            os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition",
                    "attachment; filename=" + StringUtil.encodeUri("gbk", fileName) + ".xls");
            response.setContentType("application/msexcel");
            ExcelUtils.sendExcel(list, fileName, os);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * <p>
     * 发送Excel流
     * <p>
     * 
     * @param list
     *            数据行集合
     * @param fileName
     *            文件名字
     * @throws Exception
     */
    public static void sendExcel(Map<String, List<IRow>> data, String fileName, HttpServletResponse response) {
        try {
            OutputStream os;
            os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition",
                    "attachment; filename=" + StringUtil.encodeUri("gbk", fileName) + ".xls");
            response.setContentType("application/msexcel");
            ExcelUtils.sendExcel(data, fileName, os);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
