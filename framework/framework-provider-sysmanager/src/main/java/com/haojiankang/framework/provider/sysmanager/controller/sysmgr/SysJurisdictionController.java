package com.haojiankang.framework.provider.sysmanager.controller.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysJurisdiction;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOJurisdiction;
import com.haojiankang.framework.provider.sysmanager.api.service.sysmgr.JurisdictionService;
import com.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;
import com.haojiankang.framework.provider.sysmanager.controller.common.BaseController;

@Controller
@RequestMapping("/jurisdiction")
public class SysJurisdictionController extends BaseController<SysJurisdiction,VOJurisdiction>{
    @Resource
    private JurisdictionService service;

    @Override
    public BaseService<SysJurisdiction,VOJurisdiction> getBaseService() {
        return service;
    }

}
