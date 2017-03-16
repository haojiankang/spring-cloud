/** 
 * Project Name:EHealthData 
 * File Name:HiWorkFlow.java 
 * Package Name:com.ghit.wf.hibernate 
 * Date:2016年8月29日上午9:53:10  
*/

package com.github.haojiankang.framework.provider.sysmanager.service.wf.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.github.haojiankang.framework.commons.utils.security.model.IUser;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf.Activity;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf.BPD;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf.BPN;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf.BPNAction;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf.HiActivityBean;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf.HiBPDBean;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf.HiBPNActionBean;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf.HiBPNBean;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf.HiProcessBean;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf.WFProcess;
import com.github.haojiankang.framework.provider.sysmanager.api.service.wf.ActivitiesFilter;
import com.github.haojiankang.framework.provider.sysmanager.api.service.wf.ActivityFilter;
import com.github.haojiankang.framework.provider.sysmanager.api.service.wf.WFService;
import com.github.haojiankang.framework.provider.sysmanager.api.service.wf.WorkFlow;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.wf.FlowStateError;
import com.github.haojiankang.framework.provider.sysmanager.supports.wf.HiWorkFlowContext;

/**
 * ClassName:HiWorkFlow <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月29日 上午9:53:10    <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@Component
public class HiWorkFlow implements WorkFlow {
    @Resource
    WFService<HiBPDBean, HiBPNBean, HiProcessBean, HiActivityBean, HiBPNActionBean> wfService;

    @Override
    public WFProcess getFlowState(String id) {
        return wfService.getWFProcess(id);
    }

    @Override
    public WFProcess StartUpProcess(String bpdCode, IUser user) {
        // 根据bpdCode获取BPD对象。
        HiBPDBean bpd = HiWorkFlowContext.getBPD(bpdCode, true);
        WFProcess process = wfService.startBPD(bpd, user);
        return process;
    }

    @Override
    public WFProcess ExecutiveAction(String processId, String actionCode, String message, IUser user) {
        HiBPNActionBean action = wfService.getAction(actionCode, true);
        HiProcessBean wfProcess = wfService.getWFProcess(processId);
        if (action.getOwnBPN().stream().filter(n -> {
            return n.getCode().equals(wfProcess.getBPN().getCode());
        }).findFirst().orElse(null) == null)
            throw new FlowStateError("流程状态已经发生变化，请刷新后再执行动作。");
        HiProcessBean wfProcess2 = wfService.action(wfProcess, action, user, message);
        return wfProcess2;
    }

    public WFService<HiBPDBean, HiBPNBean, HiProcessBean, HiActivityBean, HiBPNActionBean> getWfService() {
        return wfService;
    }

    public void setWfService(
            WFService<HiBPDBean, HiBPNBean, HiProcessBean, HiActivityBean, HiBPNActionBean> wfService) {
        this.wfService = wfService;
    }

    @Override
    public BPD getBPD(String code) {
        return wfService.getBPD(code, false);
    }

    @Override
    public BPN getBPN(String code) {
        return wfService.getBPN(code, false);
    }

    @Override
    public BPNAction getAction(String code) {
        return wfService.getAction(code, false);
    }

    @Override
    public Activity getActivity(String id) {
        return wfService.getActivity(id);
    }

    @Override
    public Map<String, List<Activity>> getActivityByProcessId(String... processId) {
        return wfService.getActivityByProcessId(processId);
    }

    @Override
    public Map<String, String> actionNodeMapping() {
        return wfService.getActionNodeMapping();
    }

    @Override
    public Map<String, List<Activity>> getActivityByProcessId(ActivitiesFilter filter, String... processId) {
        Map<String, List<Activity>> activies = wfService.getActivityByProcessId(processId);
        activies.forEach((k, v) -> filter.filter(v));
        return activies;
    }

    @Override
    public Map<String, Activity> getActivityByProcessId(ActivityFilter filter, String... processId) {
        return wfService.getActivityByProcessId(processId).entrySet().stream()
                .collect(Collectors.toMap(t -> t.getKey(), t -> filter.filter(t.getValue())));
    }
}
