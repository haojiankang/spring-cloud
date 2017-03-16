package com.github.haojiankang.framework.provider.utils.hibernate;

import java.io.Serializable;
import java.util.Date;

/**
 * 抽象实体基类，提供统一的ID，和相关的基本功能方法
 * 
 * @author Zhang Kaitao(13-3-20 下午8:38)
 * @version 1.0
 * @param <ID>
 *            标识对象唯一性的可序列化属性
 */
public interface AbstractPojo<ID extends Serializable> extends Serializable {
    /**
     * 
     * getId:返回对象唯一性标识.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    public abstract ID getId();

    /**
     * 设置对象唯一性标识
     * 
     * @param id
     */
    public abstract void setId(final ID id);

    /**
     * 
     * getCreateUser:返回创建用户.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    public abstract String getCreateUser();

    /**
     * 
     * setCreateUser:设置创建用户属性
     *
     * @author ren7wei
     * @param createUser
     * @since JDK 1.8
     */
    public abstract void setCreateUser(String createUser);

    /**
     * 
     * getCreateTime:返回创建时间.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    public abstract Date getCreateTime();

    /**
     * 
     * setCreateTime:设置创建时间.
     *
     * @author ren7wei
     * @param createTime
     * @since JDK 1.8
     */
    public abstract void setCreateTime(Date createTime);

    /**
     * 
     * getUpdateUser:获取修改用户.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    public abstract String getUpdateUser();

    /**
     * 
     * setUpdateUser:设置更新用户.
     *
     * @author ren7wei
     * @param updateUser
     * @since JDK 1.8
     */
    public abstract void setUpdateUser(String updateUser);

    /**
     * 
     * getUpdateTime:获取更新时间.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    public abstract Date getUpdateTime();

    /**
     * 
     * setUpdateTime:设置更新时间.
     *
     * @author ren7wei
     * @param updateTime
     * @since JDK 1.8
     */
    public abstract void setUpdateTime(Date updateTime);

    /**
     * 
     * getRemark:返回备注.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    public abstract String getRemark();

    /**
     * 
     * setRemark:设置备注.
     *
     * @author ren7wei
     * @param remark
     * @since JDK 1.8
     */
    public abstract void setRemark(String remark);

}
