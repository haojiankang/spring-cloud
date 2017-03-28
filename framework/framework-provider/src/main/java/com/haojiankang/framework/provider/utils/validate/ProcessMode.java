/** 
 * Project Name:EHealthData 
 * File Name:ProcessMode.java 
 * Package Name:com.ghit.common.validate 
 * Date:2016年6月24日上午11:37:01  
*/  
  
package com.haojiankang.framework.provider.utils.validate;  
/** 
 * ClassName:ProcessMode  <br>
 * Function: 校验不通过，处理模式枚举 <br> 
 * Date:     2016年6月24日 上午11:37:01 <br> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8 
 * @see       
 */
public enum ProcessMode {
    /**
     * 抛出异常
     */
    Exception,
    /**
     * 不处理
     */
    NoProcess,
    /**
     * 存放在上下文中
     */
    Context
}
  