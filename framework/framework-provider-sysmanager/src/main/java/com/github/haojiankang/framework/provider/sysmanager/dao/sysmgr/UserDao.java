package com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr;

import java.io.Serializable;

import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysUser;
import com.github.haojiankang.framework.provider.utils.hibernate.BaseDao;

public interface UserDao extends BaseDao<SysUser, Serializable> {

    public SysUser findUsers(SysUser user);

    public SysUser findUsersByName(SysUser user);
}
