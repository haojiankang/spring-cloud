/** 
 * Project Name:EHealthData 
 * File Name:MailUtils.java 
 * Package Name:com.ghit.common.util 
 * Date:2016年7月15日上午9:21:44  
*/

package com.github.haojiankang.framework.commons.utils.mail;

import java.util.ArrayList;
import java.util.List;
/** 
 * ClassName:MailUtils <br> 
 * Function: 邮件工具类. <br> 
 * Date:     2016年7月15日 上午9:21:44 <br> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MailUtils {
    private static Log log = LogFactory.getLog(MailUtils.class);

    public static boolean send(MailInfo mailInfo) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", String.valueOf(mailInfo.getAuth()));// 设置访问smtp服务器需要认证
        props.setProperty("mail.transport.protocol", mailInfo.getProtocol().getProtocol()); // 设置访问服务器的协议
        Session session = Session.getDefaultInstance(props);
        Message msg = new MimeMessage(session);
        Transport trans = null;
        try {
            msg.setFrom(new InternetAddress(mailInfo.getSendUser())); // 设置发件人，163邮箱要求发件人与登录用户必须一致（必填），其它邮箱不了解
            msg.setText(mailInfo.getContent()); // 设置邮件内容
            msg.setSubject(mailInfo.getSubject()); // 设置邮件主题
            trans = session.getTransport();
            trans.connect(mailInfo.getUrl(), mailInfo.getPort(), mailInfo.getUserName(), mailInfo.getPassword()); // 连接邮箱smtp服务器，25为默认端口
            List<Address> recvUsers = new ArrayList<>();
            mailInfo.getRecvUser().forEach(m -> {
                try {
                    recvUsers.add(new InternetAddress(m));
                } catch (Exception e) {
                    log.error(e);
                }
            });
            trans.sendMessage(msg, recvUsers.toArray(new Address[recvUsers.size()])); // 发送邮件
        } catch (AddressException e) {
            log.error(e);
        } catch (NoSuchProviderException e) {
            log.error(e);
        } catch (MessagingException e) {
            log.error(e);
        } finally {
            try {
                // 关闭连接
                if (trans != null)
                    trans.close();
            } catch (MessagingException e) {
                log.error(e);
            }
        }

        return true;
    }
}