package com.ghit.framework.provider.sysmanager.api.supports.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.ghit.framework.provider.utils.hibernate.AbstractPojo;

/**
 * Description:所有实体对象的基类。
 * 
 * @author lengzhihui
 * @version 1.0
 * 
 *          <pre>
 * Modification History:
 * Date         Author       Version     Description
 * ------------------------------------------------------------------
 * 2016-05-13      lengzhihui    1.0        1.0 Version
 *          </pre>
 */

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity  implements AbstractPojo<String>,Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = -8909480247829411830L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    
    @Column(name = "create_user")
    private String createUser;
    @Column(name = "create_time")
    private Date createTime;
    
    @Column(name = "update_user")
    private String updateUser;
    
    @Column(name = "update_time")
    private Date updateTime;
    
    @Column(name = "remark")
    private String remark;
    @Column(name = "CREATEUSER_TYPE")
    private String createUserType;
    @Column(name = "UPDATEUSER_TYPE")
    private String updateUserType;

    public String getId() {
        return this.id;
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

    /**
     * Description:clone方法。
     * 
     * @param
     * @return Entity，返回克隆后的对象。
     * @throws CloneNotSupportedException
     * @throws @Author
     *             lengzhihui Create Date: 2012-12-20 下午2:55:26
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
