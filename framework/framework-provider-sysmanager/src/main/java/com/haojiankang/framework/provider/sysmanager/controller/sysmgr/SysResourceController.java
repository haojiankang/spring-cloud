package com.haojiankang.framework.provider.sysmanager.controller.sysmgr;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.haojiankang.framework.commons.utils.Page;
import com.haojiankang.framework.commons.utils.SSTO;
import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysResource;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOResource;
import com.haojiankang.framework.provider.sysmanager.api.service.sysmgr.ResourceService;
import com.haojiankang.framework.provider.sysmanager.api.supports.TreeNode;
import com.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;
import com.haojiankang.framework.provider.sysmanager.controller.common.BaseController;
import com.haojiankang.framework.provider.utils.PS;

@Controller
@RequestMapping("/resource")
public class SysResourceController extends BaseController<SysResource, VOResource> {
    @javax.annotation.Resource
    private ResourceService service;

    @Override
    protected boolean listBefore(Map<String, Object> maps, Page page) {
        return false;
    }

    @Override
    protected SSTO<?> listReturn(Map<String, Object> maps, Page page) {
        TreeNode tree = service.toTree(service.loadResource(""));
        return SSTO.structure(true, PS.message(), tree);
    }

    @Override
    public BaseService<SysResource, VOResource> getBaseService() {
        return service;
    }

}
