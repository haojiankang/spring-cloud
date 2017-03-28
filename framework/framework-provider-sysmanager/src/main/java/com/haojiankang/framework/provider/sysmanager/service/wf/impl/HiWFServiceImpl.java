/** 
 * Project Name:EHealthData 
 * File Name:HiWFServiceImpl.java 
 * Package Name:com.ghit.wf.hibernate.service 
 * Date:2016年8月30日下午2:12:49  
*/

package com.haojiankang.framework.provider.sysmanager.service.wf.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haojiankang.framework.commons.utils.lang.StringUtil;
import com.haojiankang.framework.commons.utils.security.model.IUser;
import com.haojiankang.framework.commons.utils.security.model.SecurityUser;
import com.haojiankang.framework.provider.sysmanager.api.model.po.wf.HiActivity;
import com.haojiankang.framework.provider.sysmanager.api.model.po.wf.HiBPD;
import com.haojiankang.framework.provider.sysmanager.api.model.po.wf.HiBPN;
import com.haojiankang.framework.provider.sysmanager.api.model.po.wf.HiBPNAction;
import com.haojiankang.framework.provider.sysmanager.api.model.po.wf.HiProcess;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.wf.Activity;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.wf.HiActivityBean;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.wf.HiBPDBean;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.wf.HiBPNActionBean;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.wf.HiBPNBean;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.wf.HiProcessBean;
import com.haojiankang.framework.provider.sysmanager.api.service.wf.WFService;
import com.haojiankang.framework.provider.sysmanager.api.supports.wf.ActionType;
import com.haojiankang.framework.provider.sysmanager.dao.wf.WFDao;
import com.haojiankang.framework.provider.sysmanager.supports.wf.HiWorkFlowContext;

/**
 * ClassName:HiWFServiceImpl <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月30日 下午2:12:49 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class HiWFServiceImpl
        implements WFService<HiBPDBean, HiBPNBean, HiProcessBean, HiActivityBean, HiBPNActionBean> {
    @Resource
    private WFDao wfDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public HiProcessBean startBPD(HiBPDBean bpd, IUser user) {
        HiBPNBean startBPN = bpd.getStartBPN();
        HiProcessBean process = createProcess(bpd, user, startBPN);
        if (automatic(process, user)) {
            updateProcessByBean(process);
        }
        return process;
    }

    private HiProcessBean createProcess(HiBPDBean bpd, IUser user, HiBPNBean startBPN) {
        HiProcessBean process = new HiProcessBean();
        process.setBpd(bpd);
        process.setUser(user);
        process.setBpn(startBPN);
        process.setExecuteTime(new Date());
        HiProcess EntityProcess = HiProcessBean.convert(process, null);
        wfDao.insertProcess(EntityProcess);
        process.setId(EntityProcess.getId());
        createActivivy(process);
        return process;
    }

    private void createActivivy(HiProcessBean process) {
        HiActivityBean activity = new HiActivityBean();
        activity.setAtion(process.getAction());
        activity.setBpd(process.getBPD());
        activity.setCurrentBpn(process.getBPN());
        if (process.getAction() != null) {
            activity.setPreBpn(process.getBPN());
            activity.setCurrentBpn(process.getAction().getNextBPN());
        }
        activity.setMessage(process.getMessage());
        activity.setExecuteTime(process.getExecuteTime());
        activity.setUser(process.getUser());
        activity.setProcess(process);
        HiActivity EntityActivity = HiActivityBean.convert(activity, null);
        wfDao.insertActivity(EntityActivity);
    }

    private boolean automatic(HiProcessBean process, IUser user) {
        // 默认动作为自动执行动作，则自动执行该动作
        if (process.getBPN().getDefAction() != null
                && process.getBPN().getDefAction().getType() == ActionType.Automatic) {
            execAction(process, process.getBPN().getDefAction(), user, null);
            return true;
            // 当前节点为结束节点，执行活动记录转移操作
        } else if (process.getBPN().getCode().equals(process.getBPD().getEndBPN().getCode())) {
            wfDao.activityMoveHistory(process.getId());
        }
        return false;
    }

    @Override
    public HiProcessBean getWFProcess(String id) {
        HiProcess e = wfDao.getProcess(id);
        HiProcessBean process = new HiProcessBean();
        entityToBeanWithProcess(e, process);
        return process;
    }

    private void entityToBeanWithProcess(HiProcess e, HiProcessBean process) {
        process.setAtion(HiWorkFlowContext.getAction(e.getActionCode(), true));
        process.setBpd(HiWorkFlowContext.getBPD(e.getBpdCode(), true));
        process.setBpn(HiWorkFlowContext.getBPN(e.getCurrentBpnCode(), true));
        process.setExecuteTime(e.getExecuteTime());
        process.setId(e.getId());
        process.setMessage(e.getMessage());
        SecurityUser securityUser = new SecurityUser();
        process.setUser(securityUser);
        securityUser.setId(e.getExecutor());
        securityUser.setUserType(Integer.parseInt(e.getExecutorType()));
        securityUser.setUserName(e.getExecutorName());
    }

    @Override
    public HiBPDBean getBPD(String code, boolean deep) {
        return HiWorkFlowContext.getBPD(code, deep);
    }

    @Override
    public HiBPNBean getBPN(String code, boolean deep) {
        return HiWorkFlowContext.getBPN(code, deep);
    }

    @Override
    public HiBPNActionBean getAction(String code, boolean deep) {
        return HiWorkFlowContext.getAction(code, deep);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public HiProcessBean execAction(HiProcessBean process, HiBPNActionBean action, IUser user, String actionMessage) {
        process.setUser(user);
        process.setAtion(action);
        process.setExecuteTime(new Date());
        process.setMessage(actionMessage);
        // 创建一个流程活动记录
        createActivivy(process);
        // 自动处理节点状态问题
        process.setBpn(action.getNextBPN());
        automatic(process, user);
        return process;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public HiProcessBean action(HiProcessBean process, HiBPNActionBean action, IUser user, String actionMessage) {
        process = execAction(process, action, user, actionMessage);
        updateProcessByBean(process);
        return process;
    }

    private void updateProcessByBean(HiProcessBean process) {
        HiProcess EntityProcess = HiProcessBean.convert(process, null);
        wfDao.saveProcess(EntityProcess);
    }

    @Override
    public List<HiBPDBean> allBPDs() {
        List<HiBPD> bpds = wfDao.allBPDs();
        return bpds.stream().map(bpd -> {
            HiBPDBean bean = new HiBPDBean();
            bean.setCode(bpd.getCode());
            bean.setId(bpd.getId());
            bean.setName(bpd.getName());
            bean.setEndBPN(new HiBPNBean());
            bean.setStartBPN(new HiBPNBean());
            bean.getEndBPN().setCode(bpd.getEndBpnCode());
            bean.getStartBPN().setCode(bpd.getStartBpnCode());
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public List<HiBPNBean> allBPNs() {
        List<HiBPN> bpns = wfDao.allBPNs();
        return bpns.stream().map(bpn -> {
            HiBPNBean bean = new HiBPNBean();
            bean.setCode(bpn.getCode());
            bean.setId(bpn.getId());
            bean.setName(bpn.getName());
            bean.setBpd(new HiBPDBean());
            bean.getBPD().setCode(bpn.getBpdCode());
            bean.setDefAction(new HiBPNActionBean());
            bean.getDefAction().setCode(bpn.getDefActionCode());
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public List<HiBPNActionBean> allActions() {
        List<HiBPNAction> bpds = wfDao.allActions();
        return bpds.stream().map(action -> {
            HiBPNActionBean bean = new HiBPNActionBean();
            bean.setAuthenticationExpression(action.getAuthExpr());
            bean.setCode(action.getCode());
            bean.setId(action.getId());
            bean.setName(action.getName());
            if (action.getActionType() == null)
                bean.setType(ActionType.Manual);
            else {
                bean.setType(ActionType.paser(action.getActionType()));
            }
            bean.setBpd(new HiBPDBean());
            bean.getBpd().setCode(action.getBpdCode());
            bean.setNextBPN(new HiBPNBean());
            bean.getNextBPN().setCode(action.getNextBpnCode());
            bean.setOwnBPN(new HashSet<>());
            if (action.getCurrentBpnCode().indexOf(",") != -1) {
                String[] split = action.getCurrentBpnCode().split(",");
                for (String code : split) {
                    HiBPNBean bpn = new HiBPNBean();
                    bpn.setCode(code);
                    bean.getOwnBPN().add(bpn);
                }
            } else {
                HiBPNBean bpn = new HiBPNBean();
                bpn.setCode(action.getCurrentBpnCode());
                bean.getOwnBPN().add(bpn);
            }
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public HiActivityBean getActivity(String id) {
        HiActivity active = wfDao.getActivity(id);
        HiActivityBean hiBean = new HiActivityBean();

        HiBPNActionBean act = new HiBPNActionBean();
        act.setCode(active.getActionCode());
        hiBean.setAtion(act);

        HiBPDBean bpd = new HiBPDBean();
        bpd.setCode(active.getBpdCode());
        hiBean.setBpd(bpd);

        HiBPNBean bpn = new HiBPNBean();
        bpn.setCode(active.getCurrentBpnCode());
        hiBean.setCurrentBpn(bpn);

        HiBPNBean pre_bpn = new HiBPNBean();
        pre_bpn.setCode(active.getPreBpnCode());
        hiBean.setPreBpn(pre_bpn);

        hiBean.setProcess(new HiProcessBean());
        hiBean.getProcess().setId(active.getProcessId());

        SecurityUser usr = new SecurityUser();
        usr.setId(active.getExecutor());
        usr.setUserType(Integer.parseInt(active.getExecutorType()));
        usr.setUserName(active.getExecutorName());
        hiBean.setUser(usr);

        hiBean.setId(active.getId());
        hiBean.setMessage(active.getMessage());
        hiBean.setExecuteTime(active.getExecuteTime());
        return hiBean;
    }

    @Override
    public Map<String, List<Activity>> getActivityByProcessId(String... processId) {
        Map<String, List<Activity>> map = new HashMap<>();

        List<HiActivity> activitys = wfDao.getActivityByProcessId(processId);
        activitys.stream().forEach(a -> {
            if (map.get(a.getProcessId()) == null) {
                map.put(a.getProcessId(), new ArrayList<>());
            }
            if (StringUtil.isEmpty(a.getActionCode())) {
                return;
            }
            HiActivityBean hiBean = new HiActivityBean();

            hiBean.setAtion(HiWorkFlowContext.getAction(a.getActionCode(), false));

            hiBean.setBpd(HiWorkFlowContext.getBPD(a.getBpdCode(), false));

            hiBean.setCurrentBpn(HiWorkFlowContext.getBPN(a.getCurrentBpnCode(), false));

            hiBean.setPreBpn(HiWorkFlowContext.getBPN(a.getPreBpnCode(), false));

            hiBean.setProcess(new HiProcessBean());
            hiBean.getProcess().setId(a.getProcessId());

            SecurityUser usr = new SecurityUser();
            usr.setId(a.getExecutor());
            usr.setUserType(Integer.parseInt(a.getExecutorType()));
            usr.setUserName(a.getExecutorName());
            hiBean.setUser(usr);
            hiBean.setId(a.getId());
            hiBean.setMessage(a.getMessage());
            hiBean.setExecuteTime(a.getExecuteTime());
            map.get(a.getProcessId()).add(hiBean);
        });
        return map;
    }

    @Override
    public Map<String, String> getActionNodeMapping() {
        return HiWorkFlowContext.getActionNodeMapping(true);
    }
}
