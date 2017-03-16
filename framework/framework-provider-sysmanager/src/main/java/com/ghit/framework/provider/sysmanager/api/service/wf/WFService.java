/** 
 * Project Name:EHealthData 
 * File Name:WFService.java 
 * Package Name:com.ghit.wf.model 
 * Date:2016年8月30日下午2:08:01  
*/

package com.ghit.framework.provider.sysmanager.api.service.wf;

import java.util.List;
import java.util.Map;

import com.ghit.framework.commons.utils.security.model.IUser;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.Activity;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.BPD;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.BPN;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.BPNAction;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.WFProcess;

/**
 * ClassName:WFService <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月30日 下午2:08:01 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public interface WFService<D extends BPD, N extends BPN, P extends WFProcess, Active extends Activity,A extends BPNAction> {
    P startBPD(D bpd,IUser user);
    P getWFProcess(String id);
    D getBPD(String code,boolean deep);
    N getBPN(String code,boolean deep);
    A getAction(String code,boolean deep);
    P action(P p,A a,IUser user,String actionMessage);
    List<D> allBPDs();
    List<N> allBPNs();
    List<A> allActions();
	Active getActivity(String id);
    Map<String, List<Activity>> getActivityByProcessId(String...processId);
    Map<String,String> getActionNodeMapping();
}
