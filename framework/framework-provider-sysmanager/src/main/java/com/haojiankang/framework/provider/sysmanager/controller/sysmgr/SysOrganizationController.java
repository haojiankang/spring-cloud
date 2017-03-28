package com.haojiankang.framework.provider.sysmanager.controller.sysmgr;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.haojiankang.framework.commons.utils.Page;
import com.haojiankang.framework.commons.utils.SSTO;
import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysOrganization;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOOrganization;
import com.haojiankang.framework.provider.sysmanager.api.service.sysmgr.OrganizationService;
import com.haojiankang.framework.provider.sysmanager.api.supports.TreeNode;
import com.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;
import com.haojiankang.framework.provider.sysmanager.controller.common.BaseController;
import com.haojiankang.framework.provider.utils.PS;

@Controller
@RequestMapping("/organization")
public class SysOrganizationController extends BaseController<SysOrganization,VOOrganization> {

    @Resource
    private OrganizationService service;

    @Override
    protected boolean listBefore(Map<String, Object> maps, Page page) {
        return false;
    }

    @Override
    protected SSTO<?> listReturn(Map<String, Object> maps, Page page) {
        TreeNode rootNode = service.loadNode("");
        return SSTO.structure(true, PS.message(), rootNode);
    }

    @Override
    protected SSTO<?> infoReturn(VOOrganization organization) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("organization", organization);
        organization = service.findOrganizationByCode(organization.getPcode());
        if (organization != null)
            map.put("pname", organization.getName());
        return SSTO.structure(true, PS.message(), map);
    }

    @Override
    public BaseService<SysOrganization,VOOrganization> getBaseService() {
        return service;
    }

}
