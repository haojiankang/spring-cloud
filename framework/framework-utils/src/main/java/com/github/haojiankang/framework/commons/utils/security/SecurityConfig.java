package com.github.haojiankang.framework.commons.utils.security;
/** 
 * Project Name:ytj-manage-common 
 * File Name:SecurityConfig.java 
 * Package Name:com.ghit.common.security.web.filter 
 * Date:2017年2月16日下午4:26:32  
*/   
/** 
 * ClassName:SecurityConfig <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2017年2月16日 下午4:26:32 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
public interface SecurityConfig {
    String getConfigValue(String name);
    String getConfigValueDef(String name,String defValue);
    
}
  