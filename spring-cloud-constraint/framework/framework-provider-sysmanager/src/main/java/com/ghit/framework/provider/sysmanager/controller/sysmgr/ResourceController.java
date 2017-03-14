package com.ghit.framework.provider.sysmanager.controller.sysmgr;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ghit.framework.commons.utils.Page;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Resource;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOResource;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.ResourceService;
import com.ghit.framework.provider.sysmanager.api.supports.TreeNode;
import com.ghit.framework.provider.sysmanager.api.supports.service.BaseService;
import com.ghit.framework.provider.sysmanager.controller.common.BaseController;

@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController<Resource,VOResource> {
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
    public BaseService<Resource,VOResource> getBaseService() {
        return service;
    }

}
