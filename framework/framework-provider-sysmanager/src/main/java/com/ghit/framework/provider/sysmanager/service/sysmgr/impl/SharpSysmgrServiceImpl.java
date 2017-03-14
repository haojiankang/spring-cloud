/** 
 * Project Name:ytj-manage-service 
 * File Name:SharpSysmgrServiceImpl.java 
 * Package Name:com.ghit.ytj.sysmgr.impl 
 * Date:2017年2月16日下午7:51:55  
*/

package com.ghit.framework.provider.sysmanager.service.sysmgr.impl;

import org.springframework.stereotype.Service;

import com.ghit.framework.provider.sysmanager.api.service.sysmgr.SharpSysmgrService;
import com.ghit.framework.provider.sysmanager.supports.sysmgr.SharpSysmgr;

/**
 * ClassName:SharpSysmgrServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年2月16日 下午7:51:55 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
public class SharpSysmgrServiceImpl implements SharpSysmgrService {

    public static final String CNFIG_GROUP_NAME = "sids";

    @Override
    public Object dataDicLookup(String name) {
        return SharpSysmgr.dataDicLookup(name);
    }

    @Override
    public String getConfigValueDef(String name, String def) {
        return SharpSysmgr.getConfigValueDef(name, def);
    }


    @Override
    public String getConfigValue(String name) {
        return SharpSysmgr.getConfigValue(name);
    }

}
