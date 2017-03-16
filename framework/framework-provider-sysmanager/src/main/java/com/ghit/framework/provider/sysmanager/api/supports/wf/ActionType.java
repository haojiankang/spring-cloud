/** 
 * Project Name:EHealthData 
 * File Name:ActionType.java 
 * Package Name:com.ghit.wf.model 
 * Date:2016年8月28日下午10:01:18  
*/

package com.ghit.framework.provider.sysmanager.api.supports.wf;


/**
 * ClassName:ActionType <br/>
 * Function: 动作类型枚举. <br/>
 * Date: 2016年8月28日 下午10:01:18 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public enum ActionType {
    /**
     * 自动执行动作
     */
    Automatic,
    /**
     * 手动执行动作
     */
    Manual;
    public static ActionType paser(String type) {
        for (ActionType t : ActionType.values()) {
            if (t.name().equalsIgnoreCase(type))
                return t;
        }
        return null;
    }
}
