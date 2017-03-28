package com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.haojiankang.framework.provider.sysmanager.api.supports.po.BaseEntity;

/**
 * 机构、组织信息实体
 * 
 * @author ren7wei 2016年6月8日 上午10:40:46
 */
@Entity
@Table(name = "HJK_SYS_ORGANIZATION")
public class SysOrganization extends BaseEntity {
    /** 可用 */
    public static final String IS_USE_ENABLED = "0";
    /** 禁用 */
    public static final String IS_USE_DISENABLED = "1";
    /**
     * 
     */
    private static final long serialVersionUID = 697174501998443291L;
    @Column(name = "code", length = 40)
    private String code;
    @Column(name = "parent_code", length = 40)
    private String pcode;
    @Column(name = "name", length = 60)
    private String name;
    @Column(name = "SLEVEL", length = 2)
    private String slevel;
    @Column(name = "ORDERNUM")
    private Integer orderNum;
    @Column(name = "IS_USE", length = 2)
    private String isUse = IS_USE_ENABLED;
    @Column(name = "CODEINDEX")
    private String codeindex;

    public SysOrganization() {
    }

    public SysOrganization(String id) {
        this.setId(id);
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

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getSlevel() {
        return slevel;
    }

    public void setSlevel(String slevel) {
        this.slevel = slevel;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getCodeindex() {
        return codeindex;
    }

    public void setCodeindex(String codeindex) {
        this.codeindex = codeindex;
    }
    
}
