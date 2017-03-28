package com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haojiankang.framework.provider.sysmanager.api.supports.DbOnly;
import com.haojiankang.framework.provider.sysmanager.api.supports.po.BaseEntity;

@Entity
@Table(name = "HJK_SYS_USER")
public class SysUser extends BaseEntity {
    {
        userType = 0;
    }
    private static final long serialVersionUID = -3907336598168676917L;
    @Column(name = "username", length = 30)
    @DbOnly(name = "帐号")
    private String userName;
    @JsonIgnore
    @Column(name = "password", length = 30)
    private String password;

    @Column(name = "fullname", length = 30)
    private String fullname;

    @Column(name = "doctcode", length = 30)
    private String doctcode;

    @Column(name = "sex")
    private Integer sex;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "tel", length = 30)
    private String tel;

    @Column(name = "fax", length = 30)
    private String fax;

    @Column(name = "flag", length = 30)
    private Integer flag;

    @Column(name = "mail")
    private String mail;
    @Transient
    private Integer userType ;

    @JsonIgnore
    @OneToMany(targetEntity = SysRole.class, fetch = FetchType.LAZY)
    @JoinTable(name = "hjk_sys_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    @BatchSize(size = 300)
    @Fetch(FetchMode.SELECT)
    private List<SysRole> roles;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @BatchSize(size = 300)
    @JoinColumn(name = "organization_code", referencedColumnName = "code")
    @Fetch(FetchMode.JOIN)
    private SysOrganization organization;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDoctcode() {
        return doctcode;
    }

    public void setDoctcode(String doctcode) {
        this.doctcode = doctcode;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public SysOrganization getOrganization() {
        return organization;
    }

    public void setOrganization(SysOrganization organization) {
        this.organization = organization;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

}
