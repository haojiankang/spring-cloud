/** 
 * Project Name:EHealthData 
 * File Name:ConfigurationController.java 
 * Package Name:com.ghit.ecg.sysmgr.controller 
 * Date:2016年7月1日上午11:26:50  
*/

package com.ghit.framework.provider.sysmanager.controller.sysmgr;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ghit.framework.commons.utils.Page;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Configuration;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOConfiguration;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.ConfigurationService;
import com.ghit.framework.provider.sysmanager.api.supports.service.BaseService;
import com.ghit.framework.provider.sysmanager.controller.common.BaseController;

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
public class ConfigurationController extends BaseController<Configuration,VOConfiguration> {
    @Resource
    private ConfigurationService service;

    @Override
    protected boolean listBefore(Map<String, Object> maps, Page page) {
        return false;
    }

    @Override
    protected Object listReturn(Map<String, Object> maps, Page page) {
        Map<String, List<VOConfiguration>> listGroup = service.listGroup(page);
        return listGroup;
    }

    @Override
    protected void saveAfter(VOConfiguration configuration, boolean state, boolean isAdd) {
//        service.callBack(configuration, isAdd ? DataOper.ADD : DataOper.EDIT);
    }

    @Override
    protected void delAfter(VOConfiguration configuration, boolean state) {
//        service.callBack(configuration, DataOper.REMOVE);
    }

    @Override
    public BaseService<Configuration,VOConfiguration> getBaseService() {
        return service;
    }

}
