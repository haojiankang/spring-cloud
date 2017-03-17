package com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysUser;
import com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.UserDao;
import com.github.haojiankang.framework.provider.utils.hibernate.BaseDaoImpl;

@Repository
public class SysUserDaoImpl extends BaseDaoImpl<SysUser, Serializable> implements UserDao {

    @Override
    public SysUser findUsers(SysUser user) {
        String hql = " from "+SysUser.class.getSimpleName()+" where password=?0 and userName=?1";

        List<SysUser> queryByHSql = this.queryByHSql(hql,
                new ArrayList<Object>(Arrays.asList(user.getPassword(), user.getUserName())));
        if (queryByHSql.size() > 0) {
            return queryByHSql.get(0);
        }
        return null;
    }

    @Override
    public SysUser findUsersByName(SysUser user) {
        String hql = " from "+SysUser.class.getSimpleName()+" where  userName=?0";
        List<SysUser> queryByHSql = this.queryByHSql(hql, new ArrayList<Object>(Arrays.asList(user.getUserName())));
        if (queryByHSql.size() > 0) {
            return queryByHSql.get(0);
        }
        return null;
    }


}
