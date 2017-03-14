/** 
 * Project Name:EHealthData 
 * File Name:WorkFlow.java 
 * Package Name:com.ghit.wf.model 
 * Date:2016年8月28日下午10:09:58  
*/

package com.ghit.framework.provider.sysmanager.api.service.wf;

import java.util.List;
import java.util.Map;

import com.ghit.framework.provider.sysmanager.api.model.vo.wf.Activity;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.BPD;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.BPN;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.BPNAction;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.WFProcess;
import com.ghit.framework.provider.sysmanager.api.supports.security.model.IUser;

/**
 * ClassName:WorkFlow <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月28日 下午10:09:58 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.6
 * @see
 */
public interface WorkFlow {
    WFProcess getFlowState(String id);

    WFProcess StartUpProcess(String bpdCode, IUser user);

    WFProcess ExecutiveAction(String processId, String actionCode, String message, IUser user);

    BPD getBPD(String code);

    BPN getBPN(String code);

    BPNAction getAction(String code);

    Activity getActivity(String id);

    Map<String, List<Activity>> getActivityByProcessId(String... processId);

    Map<String, List<Activity>> getActivityByProcessId(ActivitiesFilter filter, String... processId);

    Map<String, Activity> getActivityByProcessId(ActivityFilter filter, String... processId);

    Map<String, String> actionNodeMapping();
}
