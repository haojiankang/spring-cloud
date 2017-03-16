/** 
 * Project Name:basic-core 
 * File Name:ActivityFilter.java 
 * Package Name:com.ghit.wf.model 
 * Date:2016年11月17日下午3:22:50  
*/  
  
package com.github.haojiankang.framework.provider.sysmanager.api.service.wf;

import java.util.List;

import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf.Activity;

/** 
 * ClassName:ActivityFilter <br> 
 * Function: TODO ADD FUNCTION. <br> 
 * Date:     2016年11月17日 下午3:22:50 <br> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
@FunctionalInterface
public interface ActivityFilter {
    Activity filter(List<Activity> activities);
}
  