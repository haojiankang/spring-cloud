package com.ghit.framework.provider.sysmanager.controller.sysmgr;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ghit.framework.commons.utils.Page;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Organization;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOOrganization;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.OrganizationService;
import com.ghit.framework.provider.sysmanager.api.supports.TreeNode;
import com.ghit.framework.provider.sysmanager.api.supports.service.BaseService;
import com.ghit.framework.provider.sysmanager.controller.common.BaseController;

@Controller
@RequestMapping("/organization")
public class OrganizationController extends BaseController<Organization,VOOrganization> {

    @Resource
    private OrganizationService service;

    @Override
    protected boolean listBefore(Map<String, Object> maps, Page page) {
        return false;
    }

    @Override
    protected Object listReturn(Map<String, Object> maps, Page page) {
        TreeNode rootNode = service.loadNode("");
        return rootNode;
    }

    @Override
    protected Object infoReturn(VOOrganization organization) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("organization", organization);
        organization = service.findOrganizationByCode(organization.getPcode());
        if (organization != null)
            map.put("pname", organization.getName());
        return map;
    }

    @Override
    public BaseService<Organization,VOOrganization> getBaseService() {
        return service;
    }

}
