package com.ghit.framework.provider.sysmanager.service.sysmgr.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ghit.framework.commons.utils.bean.BeanUtils;
import com.ghit.framework.commons.utils.mail.MailInfo;
import com.ghit.framework.commons.utils.mail.MailUtils;
import com.ghit.framework.commons.utils.security.RSATools;
import com.ghit.framework.commons.utils.security.model.IUser;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.User;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOUser;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.IndexService;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.ResourceService;
import com.ghit.framework.provider.sysmanager.api.service.wf.WorkFlow;
import com.ghit.framework.provider.sysmanager.api.supports.security.ResourceInformation;
import com.ghit.framework.provider.sysmanager.dao.sysmgr.UserDao;
import com.ghit.framework.provider.sysmanager.supports.PS;
import com.ghit.framework.provider.sysmanager.supports.ProviderConstant;
import com.ghit.framework.provider.sysmanager.supports.sysmgr.SecurityPolicy;
import com.ghit.framework.provider.sysmanager.supports.sysmgr.SharpSysmgr;
import com.ghit.framework.provider.utils.validate.Validate;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class IndexServiceImpl implements IndexService {
    Log log = LogFactory.getLog(IndexServiceImpl.class);

    @Resource
    private UserDao usersDao;
    @Resource
    private WorkFlow workflow;
    @Resource
    ResourceService resourceService;

    public UserDao getUsersDao() {
        return usersDao;
    }

    /**
     * 登录密码使用 AEC(PASSWORD||MD5(PASSWORD))加密
     */
    @Override
    @Validate(mapping = { "IndexService.login.User" })
    public IUser userLogin(VOUser user) {
        try {
            user.setUserName(PS.UrlRSADecrypt(user.getUserName()));
            user.setPassword(PS.UrlRSADecrypt(user.getPassword()));
            User po = BeanUtils.poVo(user, User.class, "");
            return SecurityPolicy.login(po);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean resetPassword(VOUser user) throws Exception {
        if (SecurityPolicy.resetPassowrd(user)) {
            MailInfo mailInfo = getMailInfo();
            mailInfo.addUserRecv(user.getMail());
            mailInfo.setSubject("密码重置邮件（请勿回复）");
            mailInfo.setContent("您好，您已经成功重置密码，请尽快修改您的密码，新密码为：\r\n" + user.getPassword() + "");
            MailUtils.send(mailInfo);
            PS.message("重置密码邮件已经发送，请查收。");
            return true;
        }
        return false;
    }

    // 获取系统发送邮件的信息设置
    private MailInfo getMailInfo() {
        MailInfo mailinfo = PS.jsonToT(MailInfo.class,
                "{" + SharpSysmgr.getConfigValue(ProviderConstant.SYSMGR_CONFIG_MAIL) + "}");
        return mailinfo;
    }

    @Override
    public boolean modifyPassword(String userid, String oldpwd, String newpwd, String userType) {
        return SecurityPolicy.modifyPassword(userid, oldpwd, newpwd, userType);
    }

    @Override
    public Map<String, String> getWorkFlowNodeActionMapping() {
        return workflow.actionNodeMapping();
    }

    @Override
    public Map<String, Object> loadResource(String resourceName, IUser user) {
        ResourceInformation resource = resourceService.loadResource(resourceName, user);
        Map<String, Object> map = new HashMap<>();
        map.put("resource", resource);
        map.put("user", user);
        map.put("modulus", RSATools.getPublicKeyModulus());
        map.put("exponent", RSATools.getPublicKeyExponent());
        map.put("pwdValidate", SharpSysmgr.getConfigValueDef(ProviderConstant.SYSMGR_CONFIG_VALIDATE_PWD, ""));
        return map;
    }

    @Override
    public Map<String, String> loadConfig() {
        Map<String, String> map = BeanUtils.map("state", "ture");
        map.put("modulus", RSATools.getPublicKeyModulus());
        map.put("exponent", RSATools.getPublicKeyExponent());
        map.put("auth", SharpSysmgr.getConfigValueDef(ProviderConstant.SYSMGR_CONFIG_AUTH_INFO, ""));
        return map;

    }
}
