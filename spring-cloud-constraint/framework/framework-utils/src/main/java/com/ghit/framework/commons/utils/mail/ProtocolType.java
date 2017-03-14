/** 
 * Project Name:EHealthData 
 * File Name:ProtocolType.java 
 * Package Name:com.ghit.common.util.mail 
 * Date:2016年7月15日上午10:01:50  
*/

package com.ghit.framework.commons.utils.mail;

/**
 * ClassName:ProtocolType <br>
 * Function: 协议类型枚举. <br>
 * Date: 2016年7月15日 上午10:01:50 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public enum ProtocolType {
    /**
     * SMTP协议
     */
    SMTP(0),
    /**
     * POP3协议
     */
    POP3(1),
    /**
     * IMAP协议
     */
    IMAP(2);
    private int index;

    private ProtocolType(int index) {
        this.index = index;
    }

    private static final String[] protocols;
    static {
        protocols = new String[3];
        protocols[0] = "smtp";
        protocols[1] = "pop3";
        protocols[2] = "impa";
    }
    /**
     * 
     * getProtocol:返回当前枚举对应的协议类型.
     *
     * @author ren7wei
     * @return 协议类型
     * @since JDK 1.8
     */
    public String getProtocol() {
        return protocols[index];
    }
}
