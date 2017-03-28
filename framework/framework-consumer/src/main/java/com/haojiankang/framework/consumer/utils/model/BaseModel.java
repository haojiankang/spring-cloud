/** 
 * Project Name:framework-consumer 
 * File Name:BaseModel.java 
 * Package Name:com.ghit.framework.consumer.utils.model 
 * Date:2017年3月15日下午1:51:29  
*/  
  
package com.haojiankang.framework.consumer.utils.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * ClassName:BaseModel <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2017年3月15日 下午1:51:29 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
public class BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    protected String id;
    protected String createUser;
    protected Date createTime;
    protected String updateUser;
    protected Date updateTime;
    protected String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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


}
  