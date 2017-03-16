/** 
 * Project Name:EHealthData 
 * File Name:HiBPNAction.java 
 * Package Name:com.ghit.wf.hibernate 
 * Date:2016年8月29日上午9:52:43  
*/

package com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.wf.HiBPNAction;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.wf.ActionType;

/**
 * ClassName:HiBPNAction <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月29日 上午9:52:43 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class HiBPNActionBean implements BPNAction,Serializable {
    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;
    private String code;
    @JsonIgnore
    private HiBPDBean bpd;
    @JsonIgnore
    private Set<HiBPNBean> ownBPN;
    @JsonIgnore
    private HiBPNBean nextBPN;
    private ActionType type;
    private String authenticationExpression;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<HiBPNBean> getOwnBPN() {
        return ownBPN;
    }

    public void setOwnBPN(Set<HiBPNBean> ownBPN) {
        this.ownBPN = ownBPN;
    }

    public HiBPNBean getNextBPN() {
        return nextBPN;
    }

    public void setNextBPN(HiBPNBean nextBPN) {
        this.nextBPN = nextBPN;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public HiBPDBean getBpd() {
        return bpd;
    }

    public void setBpd(HiBPDBean bpd) {
        this.bpd = bpd;
    }

    public String getAuthenticationExpression() {
        return authenticationExpression;
    }

    public void setAuthenticationExpression(String authenticationExpression) {
        this.authenticationExpression = authenticationExpression;
    }

    public static HiBPNAction convert(HiBPNActionBean hiBean, HiBPNAction hi) {
        if (hi == null)
            hi = new HiBPNAction();
        if (hiBean.getType() != null)
            hi.setActionType(hiBean.getType().name());
        if (hiBean.getBpd() != null)
            hi.setBpdCode(hiBean.getBpd().getCode());
//        if (hiBean.getOwnBPN() != null)
//            hi.setCurrentBpnCode(hiBean.getOwnBPN().getCode());
        if (hiBean.getNextBPN() != null)
            hi.setNextBpnCode(hiBean.getNextBPN().getCode());
        hi.setCode(hiBean.getCode());
        hi.setId(hiBean.getId());
        hi.setName(hiBean.getName());
        return hi;
    }

    @Override
    public String toString() {
        return "HiBPNActionBean [id=" + id + ", name=" + name + ", code=" + code + ", type=" + type
                + ", authenticationExpression=" + authenticationExpression + "]";
    }
    @Override
    public boolean equals(Object obj) {
        return this.code.equals(((BPNAction)obj).getCode());
    }
    @Override
    public int hashCode() {
        return this.code.hashCode();
    }
}
