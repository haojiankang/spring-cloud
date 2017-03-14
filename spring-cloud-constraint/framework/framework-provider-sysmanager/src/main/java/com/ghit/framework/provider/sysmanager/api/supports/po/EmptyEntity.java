/** 
 * Project Name:EHealthData 
 * File Name:EmptyEntity.java 
 * Package Name:com.ghit.common.mvc.entity 
 * Date:2016年8月16日下午5:23:58  
*/

package com.ghit.framework.provider.sysmanager.api.supports.po;

import java.io.Serializable;
import java.util.Date;

import com.ghit.framework.provider.utils.hibernate.AbstractPojo;

/**
 * ClassName:EmptyEntity <br>
 * Function: 空白基类，不实现记录信息的通用方法. <br>
 * Date: 2016年8月16日 下午5:23:58 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public abstract class EmptyEntity<ID extends Serializable> implements AbstractPojo<ID> {

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * 
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String getCreateUser() {

        return null;
    }

    @Override
    public void setCreateUser(String createUser) {

    }

    @Override
    public Date getCreateTime() {

        return null;
    }

    @Override
    public void setCreateTime(Date createTime) {

    }

    @Override
    public String getUpdateUser() {

        return null;
    }

    @Override
    public void setUpdateUser(String updateUser) {

    }

    @Override
    public Date getUpdateTime() {

        return null;
    }

    @Override
    public void setUpdateTime(Date updateTime) {

    }

    @Override
    public String getRemark() {

        return null;
    }

    @Override
    public void setRemark(String remark) {

    }
}
