/** 
 * Project Name:EHealthData 
 * File Name:LoginSystem.java 
 * Package Name:com.ghit.ecg.sysmgr.service 
 * Date:2016年8月16日下午1:07:45  
*/  
  
package com.ghit.framework.provider.sysmanager.api.service.sysmgr;

import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.User;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOUser;
import com.ghit.framework.provider.sysmanager.api.supports.security.model.IUser;

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
    IUser login(User user);
    boolean modifyPassword(String userid,String oldPwd,String newPwd);
    boolean resetPassword(VOUser user);
}
  