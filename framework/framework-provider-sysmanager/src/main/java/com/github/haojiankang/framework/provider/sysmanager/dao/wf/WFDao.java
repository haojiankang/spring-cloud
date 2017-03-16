/** 
 * Project Name:EHealthData 
 * File Name:WFDao.java 
 * Package Name:com.ghit.wf.model 
 * Date:2016年8月30日下午2:08:18  
*/

package com.ghit.framework.provider.sysmanager.dao.wf;

import java.util.List;

import com.ghit.framework.provider.sysmanager.api.model.po.wf.HiActivity;
import com.ghit.framework.provider.sysmanager.api.model.po.wf.HiBPD;
import com.ghit.framework.provider.sysmanager.api.model.po.wf.HiBPN;
import com.ghit.framework.provider.sysmanager.api.model.po.wf.HiBPNAction;
import com.ghit.framework.provider.sysmanager.api.model.po.wf.HiProcess;

/**
 * ClassName:WFDao <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月30日 下午2:08:18 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public interface WFDao {

    void insertProcess(HiProcess process);

    HiProcess getProcess(String id);

    void insertActivity(HiActivity activity);

    void activityMoveHistory(String processId);

    void saveProcess(HiProcess entityProcess);

    List<HiBPD> allBPDs();

    List<HiBPN> allBPNs();

    List<HiBPNAction> allActions();

    HiActivity getActivity(String id);

    List<HiActivity> getActivityByProcessId(String...processId);
}
