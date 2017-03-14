/** 
 * Project Name:EHealthData 
 * File Name:BPD.java 
 * Package Name:com.ghit.wf.model 
 * Date:2016年8月28日下午7:49:31  
*/

package com.ghit.framework.provider.sysmanager.api.model.vo.wf;

import java.util.List;

/**
 * ClassName:BPD <br/>
 * Function: Business process definition. <br/>
 * Date: 2016年8月28日 下午7:49:31 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public interface BPD {
    /**
     * 
     * getId:返回流程信息ID.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    String getId();
    /**
     * 
     * getName:返回流程名称.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    String getName();
    /**
     * 
     * getCode:返回流程编码.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    String getCode();
    /**
     * 
     * getFirstBPN:返回流程初始节点.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    BPN getStartBPN();
    /**
     * 
     * getEndBPN:返回流程结束节点.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    BPN getEndBPN();
    /**
     * 
     * getAllBPN:返回本流程下的所有节点.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    List<? extends BPN> getAllBPN();
}
