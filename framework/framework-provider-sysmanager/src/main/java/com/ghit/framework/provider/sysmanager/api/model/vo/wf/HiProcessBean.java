/** 
 * Project Name:EHealthData 
 * File Name:HiFlowState.java 
 * Package Name:com.ghit.wf.hibernate 
 * Date:2016年8月29日上午9:52:58  
*/

package com.ghit.framework.provider.sysmanager.api.model.vo.wf;

import java.io.Serializable;
import java.util.Date;

import com.ghit.framework.commons.utils.security.model.IUser;
import com.ghit.framework.provider.sysmanager.api.model.po.wf.HiProcess;

/**
 * ClassName:HiFlowState <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月29日 上午9:52:58 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */

public class HiProcessBean implements WFProcess, Serializable {
    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * 
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String message;
    private Date executeTime;
    private HiBPNBean bpn;
    private HiBPDBean bpd;
    private HiBPNActionBean ation;
    private IUser user;

    public HiBPNBean getBPN() {
        return bpn;
    }

    public void setBpn(HiBPNBean bpn) {
        this.bpn = bpn;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public HiBPDBean getBPD() {
        return bpd;
    }

    @Override
    public HiBPNActionBean getAction() {
        return ation;
    }

    public String getExecutor() {
        return user == null ? null : user.getId();
    }

    public String getExecutoryType() {
        return user == null ? null : user.getUserType().toString();
    }

    public String getExecutorName() {
        return user == null ? null : user.getUserName();
    }

    @Override
    public IUser getUser() {
        return user;
    }

    public void setUser(IUser user) {
        this.user = user;
    }

    public void setAtion(HiBPNActionBean ation) {
        this.ation = ation;
    }

    public void setBpd(HiBPDBean bpd) {
        this.bpd = bpd;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static HiProcess convert(HiProcessBean hiBean, HiProcess hi) {
        if (hi == null)
            hi = new HiProcess();

        if (hiBean.getAction() != null)
            hi.setActionCode(hiBean.getAction().getCode());
        if (hiBean.getBPD() != null)
            hi.setBpdCode(hiBean.getBPD().getCode());
        if (hiBean.getBPN() != null)
            hi.setCurrentBpnCode(hiBean.getBPN().getCode());
        hi.setId(hiBean.getId());
        hi.setMessage(hiBean.getMessage());
        hi.setExecuteTime(hiBean.getExecuteTime());
        hi.setExecutorName(hiBean.getExecutorName());
        hi.setExecutorType(hiBean.getExecutoryType());
        hi.setExecutor(hiBean.getExecutor());
        hi.setId(hiBean.getId());
        hi.setMessage(hiBean.getMessage());
        return hi;
    }

    @Override
    public String toString() {
        return "HiProcessBean [id=" + id + ", message=" + message + ", executeTime=" + executeTime + "]";
    }
    
}
