package com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr;


import com.github.haojiankang.framework.provider.sysmanager.api.supports.vo.BaseVO;

public class VOResource  extends BaseVO {

    /**
     * 
     */
    private static final long serialVersionUID = -4340015938952992108L;
    private String resourceName;
    private Integer resourceType;
    private String parentNO;
    private String juris_code;
    private String no;
    private String icon;
    private String properties;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public String getParentNO() {
        return parentNO;
    }

    public void setParentNO(String parentNO) {
        this.parentNO = parentNO;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getJuris_code() {
        return juris_code;
    }

    public void setJuris_code(String juris_code) {
        this.juris_code = juris_code;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

}
