/** 
 * Project Name:EHealthData 
 * File Name:BPD.java 
 * Package Name:com.ghit.wf.model 
 * Date:2016年8月28日下午7:49:31  
*/

package com.ghit.framework.provider.sysmanager.api.model.vo.wf;

import java.util.Set;

import com.ghit.framework.provider.sysmanager.api.supports.wf.ActionType;

/**
 * ClassName:BPN <br/>
 * Function: Business process node action. <br/>
 * Date: 2016年8月28日 下午7:49:31 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public interface BPNAction {
    /**
     * 
     * getId:返回节点动作ID.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    String getId();

    /**
     * 
     * getName:返回节点动作名称.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    String getName();

    /**
     * 
     * getCode:返回节点动作编码.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    String getCode();

    /**
     * 
     * getOwnBPN:获取所属节点信息.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    Set<? extends BPN> getOwnBPN();

    /**
     * 
     * getNextBPN:获取动作后的节点信息.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    BPN getNextBPN();

    /**
     * 
     * getType:获取动作类型.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    ActionType getType();

    /**
     * 
     * getAuthenticationExpression:获取权限表达式.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.7
     */
    String getAuthenticationExpression();

}
