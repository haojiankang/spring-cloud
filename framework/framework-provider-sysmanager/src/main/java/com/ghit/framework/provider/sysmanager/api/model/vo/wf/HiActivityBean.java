/** 
 * Project Name:EHealthData 
 * File Name:HiActivity.java 
 * Package Name:com.ghit.wf.hibernate 
 * Date:2016年8月29日上午11:21:33  
*/

package com.ghit.framework.provider.sysmanager.api.model.vo.wf;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ghit.framework.commons.utils.lang.StringUtil;
import com.ghit.framework.provider.sysmanager.api.model.po.wf.HiActivity;
import com.ghit.framework.provider.sysmanager.api.supports.security.model.IUser;

/**
 * ClassName:HiActivity <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月29日 上午11:21:33 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class HiActivityBean implements Activity, Serializable {
    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * 
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String message;
    private Date executeTime;
    private HiBPNBean preBpn;
    private HiBPNBean currentBpn;
    private HiBPDBean bpd;
    private HiBPNActionBean ation;
    private HiProcessBean process;
    @JsonIgnore
    private IUser user;

    @Override
    public BPD getBPD() {
        return bpd;
    }

    @Override
    public BPN getPreBPN() {
        return preBpn;
    }

    public BPN getCurrentBPN() {
        return currentBpn;
    }

    @Override
    public BPNAction getBPNAction() {
        return ation;
    }

    @Override
    public String getExecutor() {
        return user == null ? null : user.getId();
    }

    @Override
    public String getExecutoryType() {
        return user == null ? null : user.getUserType().toString();
    }

    @Override
    public Date getExecuteTime() {
        return executeTime;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getExecutorName() {
        return user == null ? null : user.getUserName();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPreBpn(HiBPNBean preBpn) {
        this.preBpn = preBpn;
    }

    public void setCurrentBpn(HiBPNBean currentBpn) {
        this.currentBpn = currentBpn;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public void setBpd(HiBPDBean bpd) {
        this.bpd = bpd;
    }

    public void setAtion(HiBPNActionBean ation) {
        this.ation = ation;
    }

    public void setUser(IUser user) {
        this.user = user;
    }

    public HiProcessBean getProcess() {
        return process;
    }

    public void setProcess(HiProcessBean process) {
        this.process = process;
    }

    @Override
    public String getId() {
        return id;
    }

    public IUser getUser() {
        return user;
    }

    public static HiActivity convert(HiActivityBean hiBean, HiActivity hi) {
        if (hi == null)
            hi = new HiActivity();
        if (hiBean.getBPNAction() != null)
            hi.setActionCode(hiBean.getBPNAction().getCode());
        if (hiBean.getBPD() != null)
            hi.setBpdCode(hiBean.getBPD().getCode());
        if (hiBean.getCurrentBPN() != null)
            hi.setCurrentBpnCode(hiBean.getCurrentBPN().getCode());
        if (hiBean.getPreBPN() != null)
            hi.setPreBpnCode(hiBean.getPreBPN().getCode());
        if (hiBean.getProcess() != null)
            hi.setProcessId(hiBean.getProcess().getId());
        hi.setExecuteTime(hiBean.getExecuteTime());
        if (hiBean.getUser() != null) {
            if (hiBean.getUser().getDepartment() != null
                    && !StringUtil.isEmpty(hiBean.getUser().getDepartment().getName())) {
                hi.setExecutorName(hiBean.getUser().getUserName() + "(" + hiBean.getUser().getDepartment().getName() + ")");
            } else {
                hi.setExecutorName(hiBean.getUser().getUserName());
            }
        }
        hi.setExecutorType(hiBean.getExecutoryType());
        hi.setExecutor(hiBean.getExecutor());
        hi.setId(hiBean.getId());
        hi.setMessage(hiBean.getMessage());
        return hi;
    }

    @Override
    public String toString() {
        return "HiActivityBean [id=" + id + ", message=" + message + ", executeTime=" + executeTime + ", ation=" + ation
                + ", user=" + user + "]";
    }

}
