/** 
 * Project Name:framework-consumer-sysmanager 
 * File Name:User.java 
 * Package Name:com.ghit.framework.consumer.sysmanager.model 
 * Date:2017年3月15日下午1:49:11  
*/  
  
package com.github.haojiankang.framework.consumer.sysmanager.model;

import java.util.Date;
import java.util.List;

import com.github.haojiankang.framework.consumer.utils.model.BaseModel;

/** 
 * ClassName:User <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2017年3月15日 下午1:49:11 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
public class User extends BaseModel {
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

    private List<Role> roles;

    private Organization organization;

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }


}
  