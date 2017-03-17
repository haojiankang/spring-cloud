/** 
 * Project Name:EHealthData 
 * File Name:ConfigurationController.java 
 * Package Name:com.ghit.ecg.sysmgr.controller 
 * Date:2016年7月1日上午11:26:50  
*/

package com.github.haojiankang.framework.provider.sysmanager.controller.sysmgr;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.haojiankang.framework.commons.utils.Page;
import com.github.haojiankang.framework.commons.utils.SSTO;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysConfiguration;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOConfiguration;
import com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr.ConfigurationService;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;
import com.github.haojiankang.framework.provider.sysmanager.controller.common.BaseController;
import com.github.haojiankang.framework.provider.utils.PS;

/**
 * ClassName:ConfigurationController <br/>
 * Function: 系统配置信息控制器. <br/>
 * Date: 2016年7月1日 上午11:26:50 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */

@Controller
@RequestMapping("/configuration")
public class SysConfigurationController extends BaseController<SysConfiguration, VOConfiguration> {
    @Resource
    private ConfigurationService service;

    @Override
    protected boolean listBefore(Map<String, Object> maps, Page page) {
        return false;
    }

    @Override
    protected SSTO<?> listReturn(Map<String, Object> maps, Page page) {
        Map<String, List<VOConfiguration>> listGroup = service.listGroup(page);
        return SSTO.structure(true, PS.message(), listGroup);
    }

    @Override
    protected void saveAfter(VOConfiguration configuration, boolean state, boolean isAdd) {
        // service.callBack(configuration, isAdd ? DataOper.ADD :
        // DataOper.EDIT);
    }

    @Override
    protected void delAfter(VOConfiguration configuration, boolean state) {
        // service.callBack(configuration, DataOper.REMOVE);
    }

    @Override
    public BaseService<SysConfiguration, VOConfiguration> getBaseService() {
        return service;
    }

}
