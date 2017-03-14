package com.ghit.framework.provider.sysmanager.controller.sysmgr;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOUser;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.IndexService;
import com.ghit.framework.provider.sysmanager.api.supports.security.model.IUser;
import com.ghit.framework.provider.sysmanager.supports.PS;

@Controller
@RequestMapping("/index")
public class IndexController {
    Log log = LogFactory.getLog(IndexController.class);

    @Resource
    private IndexService indexService;

    @RequestMapping(value = "login")
    @ResponseBody
    public IUser login(VOUser user) {
        return indexService.userLogin(user);
    }

    @RequestMapping(value = "loadResource")
    @ResponseBody
    public Object loadResource(String resourceName,IUser user) {
       return  indexService.loadResource(resourceName, user);
    }

    @RequestMapping(value = "resetPassword")
    @ResponseBody
    public Object resetPassword(VOUser user) {
        boolean state = false;
        try {
            state = indexService.resetPassword(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return PS.backAjaxMessage(state, state ? "重置密码邮件已经发送，请到邮箱查收。" : (PS.error()));
    }

    @RequestMapping(value = "modifyPassword")
    @ResponseBody
    public Object modifyPassword(IUser user,String oldPwd,String newPwd) {
        boolean state = false;
        try {
            state = indexService.modifyPassword(user.getId(),oldPwd, newPwd, user.getUserType().toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return PS.backAjaxMessage(state, state ? null : (PS.error()));
    }


    @RequestMapping(value = "loadNodeActionMapping")
    @ResponseBody
    public Object load(String bpdCode) {
        Map<String, String> mapping = indexService.getWorkFlowNodeActionMapping();
        return mapping;
    }

    @RequestMapping(value = "config")
    @ResponseBody
    public Object loadConfig(String rsa) {
        return indexService.loadConfig(rsa);
    }

}
