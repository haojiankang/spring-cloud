package com.ghit.framework.provider.sysmanager.controller.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Jurisdiction;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOJurisdiction;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.JurisdictionService;
import com.ghit.framework.provider.sysmanager.api.supports.service.BaseService;
import com.ghit.framework.provider.sysmanager.controller.common.BaseController;

@Controller
@RequestMapping("/jurisdiction")
public class JurisdictionController extends BaseController<Jurisdiction,VOJurisdiction>{
    @Resource
    private JurisdictionService service;

    @Override
    public BaseService<Jurisdiction,VOJurisdiction> getBaseService() {
        return service;
    }

}
