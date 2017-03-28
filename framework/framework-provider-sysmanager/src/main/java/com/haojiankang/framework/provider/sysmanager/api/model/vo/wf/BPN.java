/** 
 * Project Name:EHealthData 
 * File Name:BPD.java 
 * Package Name:com.ghit.wf.model 
 * Date:2016年8月28日下午7:49:31  
*/

package com.haojiankang.framework.provider.sysmanager.api.model.vo.wf;

import java.util.List;

/**
 * ClassName:BPN <br/>
 * Function: Business process node. <br/>
 * Date: 2016年8月28日 下午7:49:31 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public interface BPN {
    /**
     * 
     * getId:返回节点ID.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    String getId();

    /**
     * 
     * getName:返回节点名称.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    String getName();

    /**
     * 
     * getCode:返回节点编码.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    String getCode();

    /**
     * 
     * getDefAction:返回节点默认动作.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    BPNAction getDefAction();

    /**
     * 
     * getBPD:返回所属流程定义信息.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    BPD getBPD();

    List<? extends BPNAction> getActions();
}
