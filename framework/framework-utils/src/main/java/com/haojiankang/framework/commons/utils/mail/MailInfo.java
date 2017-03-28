/** 
 * Project Name:EHealthData 
 * File Name:MailInfo.java 
 * Package Name:com.ghit.common.util.mail 
 * Date:2016年7月15日上午10:18:35  
*/

package com.haojiankang.framework.commons.utils.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:MailInfo <br>
 * Function: 邮箱信息. <br>
 * Date: 2016年7月15日 上午10:18:35 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class MailInfo {

    public MailInfo() {
        super();
        auth = true;
    }

    public MailInfo(String userName, String password) {
        this();
        this.userName = userName;
        this.password = password;
    }

    public MailInfo(ProtocolType protocol, String userName, String password, String sendUser, int port, String url) {
        this(userName, password);
        this.protocol = protocol;
        this.port = port;
        this.url = url;
        this.sendUser = sendUser;
    }

    public MailInfo(boolean auth, ProtocolType protocol, String userName, String password, String sendUser, int port,
            String url, List<String> recvUser, String content, String subject) {
        this(protocol, userName, password, sendUser, port, url);
        this.auth = auth;
        this.recvUser = recvUser;
        this.content = content;
        this.subject = subject;
    }

    /**
     * 是否需要认证
     */
    private boolean auth;
    /**
     * 协议类型
     */
    private ProtocolType protocol;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 协议端口
     */
    private int port;
    /**
     * 协议地址
     */
    private String url;
    /**
     * 接收用户
     */
    private List<String> recvUser;
    /**
     * 发送用户
     */
    private String sendUser;
    /**
     * 内容
     */
    private String content;
    /**
     * 主题
     */
    private String subject;

    public boolean getAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public ProtocolType getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolType protocol) {
        this.protocol = protocol;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getRecvUser() {
        return recvUser;
    }

    public void setRecvUser(List<String> recvUser) {
        this.recvUser = recvUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public void addUserRecv(String recvUser) {
        if (this.recvUser == null)
            this.recvUser = new ArrayList<>();
        this.recvUser.add(recvUser);
    }

}
