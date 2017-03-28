/** 
 * Project Name:EHealthData 
 * File Name:WFDaoImpl.java 
 * Package Name:com.ghit.wf.hibernate.dao 
 * Date:2016年9月1日上午10:47:53  
*/

package com.haojiankang.framework.provider.sysmanager.dao.wf;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.haojiankang.framework.provider.sysmanager.api.model.po.wf.HiActivity;
import com.haojiankang.framework.provider.sysmanager.api.model.po.wf.HiBPD;
import com.haojiankang.framework.provider.sysmanager.api.model.po.wf.HiBPN;
import com.haojiankang.framework.provider.sysmanager.api.model.po.wf.HiBPNAction;
import com.haojiankang.framework.provider.sysmanager.api.model.po.wf.HiProcess;

/**
 * ClassName:WFDaoImpl <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年9月1日 上午10:47:53 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@Repository
public class WFDaoImpl implements WFDao {

    @Resource(name = "sessionFactory")
    protected SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void insertProcess(HiProcess process) {
        getSession().save(process);
    }

    @Override
    public HiProcess getProcess(String id) {
        Object object = getSession().get(HiProcess.class, id);
        if (object == null)
            return null;
        return (HiProcess) object;
    }

    @Override
    public void insertActivity(HiActivity activity) {
        getSession().save(activity);
    }

    @Override
    public void activityMoveHistory(String processId) {
//        getSession().flush();
//        SQLQuery query = getSession().createSQLQuery(
//                "insert into hjk_wf_history(id,bpd_code,bpn_code,action_code,executor,executor_type,execute_time,process_id,executor_name,message)"
//                        + "select id,bpd_code,bpn_code,action_code,executor,executor_type,execute_time,process_id,executor_name,message from hjk_wf_activity where process_id=?");
//        query.setString(0, processId);
//        query.executeUpdate();
//        query = getSession().createSQLQuery(
//                "delete from  hjk_wf_activity where process_id=?");
//        query.setString(0, processId);
//        query.executeUpdate();
    }

    @Override
    public void saveProcess(HiProcess process) {
        getSession().merge(process);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<HiBPD> allBPDs() {
        return getSession().createQuery("from HiBPD").list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<HiBPN> allBPNs() {
        return getSession().createQuery("from HiBPN").list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<HiBPNAction> allActions() {
        return getSession().createQuery("from HiBPNAction").list();
    }

	@Override
	public HiActivity getActivity(String id) {
		Query q = getSession().createQuery("from HiActivity where id=?");
		@SuppressWarnings("unchecked")
		List<HiActivity> queryByHSql = q.setParameter(0, id).list();
        return queryByHSql.size() > 0 ? queryByHSql.get(0) : null;
	}

	@Override
	public List<HiActivity> getActivityByProcessId(String...processId) {
	    Query q = getSession().createQuery("from HiActivity where processId in (:processId) order by execute_time");
	    @SuppressWarnings("unchecked")
	    List<HiActivity> list = q.setParameterList("processId", processId).list();
	    return list;
	}
}
