package com.haojiankang.framework.provider.sysmanager.dao.sysmgr;

import java.io.Serializable;

import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysUser;
import com.haojiankang.framework.provider.utils.hibernate.BaseDao;

public interface UserDao extends BaseDao<SysUser, Serializable> {

    public SysUser findUsers(SysUser user);

    public SysUser findUsersByName(SysUser user);
}
