/** 
 * Project Name:EHealthData 
 * File Name:FlowState.java 
 * Package Name:com.ghit.wf.model 
 * Date:2016年8月28日下午10:24:57  
*/  
  
package com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf;

import com.github.haojiankang.framework.commons.utils.security.model.IUser;

/** 
 * ClassName:Process <br/> 
 * Function: 流程状态对象. <br/> 
 * Date:     2016年8月28日 下午10:24:57 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
public interface WFProcess {
    String getId();
    BPD getBPD();
    BPN getBPN();
    BPNAction getAction();
    IUser getUser();
}
  