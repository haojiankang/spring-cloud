/** 
 * Project Name:EHealthData 
 * File Name:ConfigurationCallback.java 
 * Package Name:com.ghit.ecg.sysmgr 
 * Date:2016年6月30日下午1:30:06  
*/

package com.github.haojiankang.framework.provider.sysmanager.supports.sysmgr;

import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.Configuration;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.DataOper;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.security.context.Context;

/**
 * ClassName:ConfigurationCallback <br/>
 * Function: 系统配置信息新增、变更、删除后触发的回调函数接口. <br/>
 * Date: 2016年6月30日 下午1:30:06 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@FunctionalInterface
public interface ConfigurationCallback {
    /**
     * 
     * call:系统配置信息变更后的回调接口
     *
     * @author ren7wei
     * @param configuration
     * @return
     * @since JDK 1.8
     */
    boolean call(Configuration configuration, Context context,DataOper oper);
}
