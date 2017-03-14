/** 
 * Project Name:EHealthData 
 * File Name:BasicEntity.java 
 * Package Name:com.ghit.common.mvc.entity 
 * Date:2016年8月16日下午5:31:44  
*/  
  
package com.ghit.framework.provider.sysmanager.api.supports.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnore;

/** 
 * ClassName:BasicEntity <br> 
 * Function: TODO ADD FUNCTION. <br> 
 * Date:     2016年8月16日 下午5:31:44 <br> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
public abstract class BasicEntity<ID extends Serializable> extends EmptyEntity<ID> {

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @Column(name = "create_user")
    private String createUser;
    @JsonIgnore
    @Column(name = "create_time")
    private Date createTime;
    @JsonIgnore
    @Column(name = "update_user")
    private String updateUser;
    @JsonIgnore
    @Column(name = "update_time")
    private Date updateTime;
    @JsonIgnore
    @Column(name = "remark")
    private String remark;
    @Column(name = "CREATEUSER_TYPE")
    private String createUserType;
    @Column(name = "UPDATEUSER_TYPE")
    private String updateUserType;

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUserType() {
        return createUserType;
    }

    public void setCreateUserType(String createUserType) {
        this.createUserType = createUserType;
    }

    public String getUpdateUserType() {
        return updateUserType;
    }

    public void setUpdateUserType(String updateUserType) {
        this.updateUserType = updateUserType;
    }




}
  