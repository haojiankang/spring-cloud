package com.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr;

import com.haojiankang.framework.provider.sysmanager.api.supports.vo.BaseVO;

/**
 * 机构、组织信息实体
 * 
 * @author ren7wei 2016年6月8日 上午10:40:46
 */
public class VOOrganization  extends BaseVO{
    private static final long serialVersionUID = 697174501998443291L;
    private String code;
    private String pcode;
    private String name;
    private String slevel;
    private Integer orderNum;
    private String isUse;
    private String codeindex;

    public VOOrganization() {
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
