/** 
 * Project Name:ghit-basic-api 
 * File Name:BaseVo.java 
 * Package Name:com.ghit.basic.api.supports.vo 
 * Date:2017年2月27日下午2:34:17  
*/

package com.ghit.framework.provider.sysmanager.api.supports.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ghit.framework.provider.utils.hibernate.AbstractPojo;

/**
 * ClassName:BaseVo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年2月27日 下午2:34:17 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
public class BaseVO implements AbstractPojo<String> {
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

    @Override
    public void setId(String pk) {
        id = pk;
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
