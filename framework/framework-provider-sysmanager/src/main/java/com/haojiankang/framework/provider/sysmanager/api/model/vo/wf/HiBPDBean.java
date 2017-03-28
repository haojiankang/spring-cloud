/** 
 * Project Name:EHealthData 
 * File Name:HiBPD.java 
 * Package Name:com.ghit.wf.hibernate 
 * Date:2016年8月29日上午9:52:27  
*/

package com.haojiankang.framework.provider.sysmanager.api.model.vo.wf;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haojiankang.framework.provider.sysmanager.api.model.po.wf.HiBPD;

/**
 * ClassName:HiBPD <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月29日 上午9:52:27 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class HiBPDBean implements BPD ,Serializable{
    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String code;
    @JsonIgnore
    private HiBPNBean startBPN;
    @JsonIgnore
    private HiBPNBean endBPN;

    private List<HiBPNBean> allBPN;

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

    public HiBPNBean getStartBPN() {
        return startBPN;
    }

    public void setStartBPN(HiBPNBean startBPN) {
        this.startBPN = startBPN;
    }

    public HiBPNBean getEndBPN() {
        return endBPN;
    }

    public void setEndBPN(HiBPNBean endBPN) {
        this.endBPN = endBPN;
    }

    public List<HiBPNBean> getAllBPN() {
        return allBPN;
    }

    public void setAllBPN(List<HiBPNBean> allBPN) {
        this.allBPN = allBPN;
    }

    public static HiBPD convert(HiBPDBean hiBean, HiBPD hi) {
        if (hi == null)
            hi = new HiBPD();
        hi.setCode(hiBean.getCode());
        if (hiBean.getEndBPN() != null)
            hi.setEndBpnCode(hiBean.getEndBPN().getCode());
        if (hiBean.getStartBPN() != null)
            hi.setStartBpnCode(hiBean.getStartBPN().getCode());
        hi.setId(hiBean.getId());
        hi.setName(hiBean.getName());
        return hi;
    }

    @Override
    public String toString() {
        return "HiBPDBean [id=" + id + ", name=" + name + ", code=" + code + "]";
    }
    
}
