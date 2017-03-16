package com.github.haojiankang.framework.provider.sysmanager.controller.sysmgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.haojiankang.framework.commons.utils.Page;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.User;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VORole;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOUser;
import com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr.OrganizationService;
import com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr.RoleService;
import com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr.UserService;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.TreeNode;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;
import com.github.haojiankang.framework.provider.sysmanager.controller.common.BaseController;
import com.github.haojiankang.framework.provider.sysmanager.supports.sysmgr.SharpSysmgr;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController<User,VOUser> {

    @Resource
    private UserService userService;
    @Resource
    private OrganizationService organizationService;
    @Resource
    private RoleService roleService;

    @RequestMapping("list")
    @ResponseBody
    public Object list(Page page) {
        Object result = page;
        if (page.getConditions().get("firstLoad") != null
                && page.getConditions().get("firstLoad").toString().equals("true")) {
            page.getConditions().remove("firstLoad");
            TreeNode rootNode = organizationService.loadNode("");
            List<VORole> roles = roleService.loadRoles();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Organization", rootNode);
            map.put("page", page);
            map.put("roles", roles);
            map.put("sex", SharpSysmgr.dataDicLookup("性别"));
            map.put("flag", SharpSysmgr.dataDicLookup("用户状态"));
            result = map;
        }
//        IUser currentUser = CS.currentUser();
//        if (currentUser != null) {
//            if (currentUser.getDepartment() != null && !currentUser.getDepartment().getId().equals("-1"))
//                // 对用户进行数据权限限制，当前用户只能查看本机构及本机构的下级机构的数据
//                page.getConditions().put("organization.codeLIKE", currentUser.getDepartment().getCode());
//        }
        userService.list(page);
        return result;
    }

    @RequestMapping(value = "info")
    @ResponseBody
    public Object info(VOUser user) {
        VOUser usrOut = userService.findById(user);
        return usrOut;
    }

    @RequestMapping(value = "reset")
    @ResponseBody
    public Object reset(String id) {
        boolean state = false;
        try {
            state = userService.reset(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return initMap(new Object[] { "state", "msg" }, new Object[] { state, (state ? "操作成功！" : "重置密码失败！") });
    }

    @Override
    public BaseService<User,VOUser> getBaseService() {
        return userService;
    }

}
