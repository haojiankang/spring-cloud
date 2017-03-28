package com.haojiankang.framework.provider.sysmanager.controller.sysmgr;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haojiankang.framework.commons.utils.Page;
import com.haojiankang.framework.commons.utils.SSTO;
import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysRole;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VORole;
import com.haojiankang.framework.provider.sysmanager.api.service.sysmgr.ResourceService;
import com.haojiankang.framework.provider.sysmanager.api.service.sysmgr.RoleService;
import com.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;
import com.haojiankang.framework.provider.sysmanager.controller.common.BaseController;
import com.haojiankang.framework.provider.utils.PS;

@Controller
@RequestMapping("/role")
public class SysRoleController extends BaseController<SysRole, VORole> {
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
    protected SSTO<?> listReturn(Map<String, Object> maps, Page page) {
        maps.put("page", page);
        return SSTO.structure(true, PS.message(), maps);
    }

    @RequestMapping(value = "all")
    @ResponseBody
    public List<VORole> all() {
        // 只有系统管理用户才能查看所有角色
        return roleService.loadRoles();
    }

    @Override
    public BaseService<SysRole, VORole> getBaseService() {
        return roleService;
    }
}
