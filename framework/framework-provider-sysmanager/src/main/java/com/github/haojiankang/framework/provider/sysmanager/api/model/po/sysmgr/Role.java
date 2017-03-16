package com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.github.haojiankang.framework.provider.sysmanager.api.supports.po.BaseEntity;

@Entity
@Table(name = "HJK_SYS_Role")
public class Role extends BaseEntity {

    {
        jurisdictions=new ArrayList<>();
    }
    private static final long serialVersionUID = -3022168779714627416L;
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "description")
    private String description;

    @Column(name = "role_code")
    private String roleCode;
    @OneToMany(targetEntity = Jurisdiction.class, fetch = FetchType.EAGER)
    @JoinTable(name = "hjk_sys_role_jurisdiction", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
            @JoinColumn(name = "jurisdiction_code", referencedColumnName = "jurisdiction_code") })
    @BatchSize(size = 300)
    @Fetch(FetchMode.SELECT)
    private List<Jurisdiction> jurisdictions;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public List<Jurisdiction> getJurisdictions() {
        return jurisdictions;
    }

    public void setJurisdictions(List<Jurisdiction> jurisdictions) {
        this.jurisdictions = jurisdictions;
    }

}
