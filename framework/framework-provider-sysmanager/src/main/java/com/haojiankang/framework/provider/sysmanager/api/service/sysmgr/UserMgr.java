/** 
 * Project Name:EHealthData 
 * File Name:LoginSystem.java 
 * Package Name:com.ghit.ecg.sysmgr.service 
 * Date:2016年8月16日下午1:07:45  
*/  
  
package com.haojiankang.framework.provider.sysmanager.api.service.sysmgr;

import com.haojiankang.framework.commons.utils.security.model.IUser;
import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysUser;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOUser;

/** 
 * ClassName:LoginSystem <br> 
 * Function: 系统登录接口. <br> 
 * Date:     2016年8月16日 下午1:07:45 <br> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
public interface UserMgr {
    IUser login(SysUser user);
    boolean modifyPassword(String userid,String oldPwd,String newPwd);
    boolean resetPassword(VOUser user);
}
  