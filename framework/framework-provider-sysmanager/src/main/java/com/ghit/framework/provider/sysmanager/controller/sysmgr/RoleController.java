package com.ghit.framework.provider.sysmanager.controller.sysmgr;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ghit.framework.commons.utils.Page;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Role;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VORole;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.ResourceService;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.RoleService;
import com.ghit.framework.provider.sysmanager.api.supports.service.BaseService;
import com.ghit.framework.provider.sysmanager.controller.common.BaseController;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController<Role,VORole> {
    @Resource
    private RoleService roleService;
    @Resource
    private ResourceService resourceService;

    @Override
    protected boolean listBefore(Map<String, Object> maps, Page page) {
        if (page.getConditions().get("firstLoad") != null
                && page.getConditions().get("firstLoad").toString().equals("true")) {
            page.getConditions().remove("firstLoad");
            maps.put("resource", resourceService.toTree(resourceService.loadResource("")));
        }
        return true;
    }
    @Override
    protected Object listReturn(Map<String, Object> maps, Page page) {
        maps.put("page", page);
        return maps;
    }

    @RequestMapping(value = "save")
    @ResponseBody
    public Object save(@RequestBody VORole role) {
        return super.save(role);
    }

    @RequestMapping(value="all")
    @ResponseBody
    public List<VORole> all() {
    	//只有系统管理用户才能查看所有角色
    	return  roleService.loadRoles();
    }

    @Override
    public BaseService<Role,VORole> getBaseService() {
        return roleService;
    }
}
