/** 
 * Project Name:ytj-manage-interface 
 * File Name:SharpService.java 
 * Package Name:com.ghit.ytj.sysmgr.service 
 * Date:2017年2月16日下午7:50:15  
*/  
  
package com.ghit.framework.provider.sysmanager.api.service.sysmgr;

import com.ghit.framework.provider.sysmanager.api.supports.security.SecurityConfig;

/** 
 * ClassName:SharpService <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2017年2月16日 下午7:50:15 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
public interface SharpSysmgrService extends SecurityConfig {

    Object dataDicLookup(String string);

    String getConfigValueDef(String sysmgrConfigAuthInfo, String string);

}
  