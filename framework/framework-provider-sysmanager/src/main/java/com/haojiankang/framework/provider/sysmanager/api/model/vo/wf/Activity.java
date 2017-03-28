/** 
 * Project Name:EHealthData 
 * File Name:Activity.java 
 * Package Name:com.ghit.wf.model 
 * Date:2016年8月29日上午11:16:23  
*/

package com.haojiankang.framework.provider.sysmanager.api.model.vo.wf;

import java.util.Date;

/**
 * ClassName:Activity <br>
 * Function: 流程活动记录. <br>
 * Date: 2016年8月29日 上午11:16:23 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public interface Activity {
    String getId();

    BPD getBPD();

    BPN getPreBPN();

    BPN getCurrentBPN();

    BPNAction getBPNAction();

    String getExecutor();

    String getExecutorName();

    String getExecutoryType();

    Date getExecuteTime();

    String getMessage();
}
