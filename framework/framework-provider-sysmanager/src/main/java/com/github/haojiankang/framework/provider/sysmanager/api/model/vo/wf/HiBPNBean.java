/** 
 * Project Name:EHealthData 
 * File Name:HiBPN.java 
 * Package Name:com.ghit.wf.hibernate 
 * Date:2016年8月29日上午9:52:07  
*/

package com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.wf.HiBPNAction;

/**
 * ClassName:HiBPN <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月29日 上午9:52:07 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */

public class HiBPNBean implements BPN ,Serializable{
    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String code;
    @JsonIgnore
    private HiBPNActionBean defAction;
    @JsonIgnore
    private List<HiBPNActionBean> actions;
    @JsonIgnore
    private HiBPDBean bpd;

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

    public HiBPNActionBean getDefAction() {
        return defAction;
    }

    public void setDefAction(HiBPNActionBean defAction) {
        this.defAction = defAction;
    }

    public List<HiBPNActionBean> getActions() {
        return actions;
    }

    public void setActions(List<HiBPNActionBean> actions) {
        this.actions = actions;
    }

    public HiBPDBean getBPD() {
        return bpd;
    }

    public void setBpd(HiBPDBean bpd) {
        this.bpd = bpd;
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
        return "HiBPNBean [id=" + id + ", name=" + name + ", code=" + code + "]";
    }
    @Override
    public boolean equals(Object obj) {
        return this.code.equals(((BPN)obj).getCode());
    }
    @Override
    public int hashCode() {
        return this.code.hashCode();
    }
}
