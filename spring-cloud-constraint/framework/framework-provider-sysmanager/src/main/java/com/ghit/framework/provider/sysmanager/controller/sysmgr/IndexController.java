package com.ghit.framework.provider.sysmanager.controller.sysmgr;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ghit.framework.commons.utils.SSTO;
import com.ghit.framework.commons.utils.security.model.IUser;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOUser;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.IndexService;
import com.ghit.framework.provider.sysmanager.supports.PS;

@Controller
@RequestMapping("/index")
public class IndexController {
    Log log = LogFactory.getLog(IndexController.class);

    @Resource
    private IndexService indexService;

    @RequestMapping(value = "login")
    @ResponseBody
    public SSTO<IUser> login(VOUser user) {
        IUser iuser = indexService.userLogin(user);
        return SSTO.structure(iuser != null, PS.message(), iuser);
    }

    @RequestMapping(value = "loadResource")
    @ResponseBody
    public Object loadResource(String resourceName, IUser user) {
        return indexService.loadResource(resourceName, user);
    }

    @RequestMapping(value = "resetPassword")
    @ResponseBody
    public SSTO<String> resetPassword(VOUser user) {
        boolean state = false;
        try {
            state = indexService.resetPassword(user);
        } catch (Exception e) {
            PS.message("执行过程中发生异常：" + e.getMessage());
        }
        return SSTO.structure(state, (PS.message()), null);
    }

    @RequestMapping(value = "modifyPassword")
    @ResponseBody
    public Object modifyPassword(IUser user, String oldPwd, String newPwd) {
        boolean state = false;
        try {
            state = indexService.modifyPassword(user.getId(), oldPwd, newPwd, user.getUserType().toString());
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
    public SSTO<Map<String, String>> loadConfig() {
        Map<String, String> config = indexService.loadConfig();
        return SSTO.structure(true, PS.message(), config);
    }

}
