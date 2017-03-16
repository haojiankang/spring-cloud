/** 
 * Project Name:EHealthData 
 * File Name:Department.java 
 * Package Name:com.ghit.common.security.model 
 * Date:2016年7月18日上午10:22:16  
*/  
  
package com.ghit.framework.commons.utils.security.model;

import java.io.Serializable;

/** 
 * ClassName:Department <br> 
 * Function: 部门接口. <br> 
 * Date:     2016年7月18日 上午10:22:16 <br> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
public interface IDepartment extends Serializable {
    /**
     * 
     * getId:获得机构的标识.
     *
     * @author ren7wei
     * @return 机构ID
     * @since JDK 1.8
     */
    String getId();
    /**
     * 
     * getName:获取机构名称.
     *
     * @author ren7wei
     * @return 机构名称
     * @since JDK 1.8
     */
    String getName();
    /**
     * 
     * getCode:获取机构编码.
     *
     * @author ren7wei
     * @return 机构编码
     * @since JDK 1.8
     */
    String getCode();
}
  