package com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.github.haojiankang.framework.provider.sysmanager.api.supports.DbOnly;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.po.BaseEntity;

@Entity
@Table(name = "HJK_SYS_RESOURCE")
public class Resource extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -4340015938952992108L;
    @Column(name = "resource_name", length = 60)
    private String resourceName;
    @Column(name = "resource_type")
    private Integer resourceType;
    @Column(name = "parent_no", length = 32)
    private String parentNO;
    @Column(name = "jurisdiction_code", length = 40)
    private String juris_code;
    @DbOnly(name = "资源编号")
    @Column(name = "resource_no", length = 40)
    private String no;
    @Column(name = "icon", length = 40)
    private String icon;
    @Column(name = "properties", length = 400)
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
