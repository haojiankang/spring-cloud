/** 
 * Project Name:EHealthData 
 * File Name:ConfigurationController.java 
 * Package Name:com.ghit.ecg.sysmgr.controller 
 * Date:2016年7月1日上午11:26:50  
*/

package com.github.haojiankang.framework.provider.sysmanager.controller.sysmgr;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.haojiankang.framework.commons.utils.Page;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysDataDictionary;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VODataDictionary;
import com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr.DataDictionaryService;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;
import com.github.haojiankang.framework.provider.sysmanager.controller.common.BaseController;

/**
 * ClassName:ConfigurationController <br/>
 * Function: 数据字典控制器. <br/>
 * Date: 2016年7月1日 上午11:26:50 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */

@Controller
@RequestMapping("/dataDictionary")
public class SysDataDictionaryController extends BaseController<SysDataDictionary,VODataDictionary> {
    @Resource
    private DataDictionaryService service;

    @Override
    public BaseService<SysDataDictionary,VODataDictionary> getBaseService() {
        return service;
    }
    
    @Override
    protected boolean listBefore(Map<String, Object> maps, Page page) {
        page.setRows(Integer.MAX_VALUE);
        return true;
    }

   

}
