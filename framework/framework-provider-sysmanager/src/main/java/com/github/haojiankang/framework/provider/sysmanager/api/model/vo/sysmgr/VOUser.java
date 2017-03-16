package com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.haojiankang.framework.provider.sysmanager.api.supports.vo.BaseVO;

public class VOUser  extends BaseVO {
    {
        userType = 0;
        roles=new ArrayList<>();
        organization=new VOOrganization();
    }
    private static final long serialVersionUID = -3907336598168676917L;
    private String userName;
    private String password;
    private String fullname;
    private String doctcode;

    private Integer sex;

    private Date birthday;

    private String tel;

    private String fax;

    private Integer flag;

    private String mail;
    private Integer userType;

    private List<VORole> roles;

    private VOOrganization organization;

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

    public List<VORole> getRoles() {
        return roles;
    }

    public void setRoles(List<VORole> roles) {
        this.roles = roles;
    }

    public VOOrganization getOrganization() {
        return organization;
    }

    public void setOrganization(VOOrganization organization) {
        this.organization = organization;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

}
