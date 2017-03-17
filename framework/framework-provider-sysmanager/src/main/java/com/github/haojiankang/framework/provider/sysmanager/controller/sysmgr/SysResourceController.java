package com.github.haojiankang.framework.provider.sysmanager.controller.sysmgr;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.haojiankang.framework.commons.utils.Page;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysResource;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOResource;
import com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr.ResourceService;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.TreeNode;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;
import com.github.haojiankang.framework.provider.sysmanager.controller.common.BaseController;

@Controller
@RequestMapping("/resource")
public class SysResourceController extends BaseController<SysResource,VOResource> {
    @javax.annotation.Resource
    private ResourceService service;

    @Override
    protected boolean listBefore(Map<String, Object> maps, Page page) {
        return false;
    }

    @Override
    protected Object listReturn(Map<String, Object> maps, Page page) {
        TreeNode tree = service.toTree(service.loadResource(""));
        return tree;
    }

    @Override
    public BaseService<SysResource,VOResource> getBaseService() {
        return service;
    }

}
