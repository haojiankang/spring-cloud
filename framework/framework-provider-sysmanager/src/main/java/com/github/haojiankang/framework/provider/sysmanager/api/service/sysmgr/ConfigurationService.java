/** 
 * Project Name:EHealthData 
 * File Name:ConfigurationService.java 
 * Package Name:com.ghit.ecg.sysmgr.service 
 * Date:2016年6月30日下午3:12:40  
*/

package com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr;

import java.util.List;
import java.util.Map;

import com.github.haojiankang.framework.commons.utils.Page;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysConfiguration;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOConfiguration;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;

/**
 * ClassName:ConfigurationService <br/>
 * Function: 系统配置信息业务管理类. <br/>
 * Date: 2016年6月30日 下午3:12:40 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public interface ConfigurationService extends BaseService<SysConfiguration,VOConfiguration> {
    /**
     * 
     * listGroup:按照分组获取数据.
     *
     * @author ren7wei
     * @param page 
     * @return 有序的分组列表
     * @since JDK 1.8
     */
    Map<String, List<VOConfiguration>> listGroup(Page page);
    
}
