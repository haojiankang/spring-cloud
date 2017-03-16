package com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.User;
import com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.UserDao;
import com.github.haojiankang.framework.provider.utils.hibernate.BaseDaoImpl;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User, Serializable> implements UserDao {

    @Override
    public User findUsers(User user) {
        String hql = " from Users where password=?0 and userName=?1";

        List<User> queryByHSql = this.queryByHSql(hql,
                new ArrayList<Object>(Arrays.asList(user.getPassword(), user.getUserName())));
        if (queryByHSql.size() > 0) {
            return queryByHSql.get(0);
        }
        return null;
    }

    @Override
    public User findUsersByName(User user) {
        String hql = " from User where  userName=?0";
        List<User> queryByHSql = this.queryByHSql(hql, new ArrayList<Object>(Arrays.asList(user.getUserName())));
        if (queryByHSql.size() > 0) {
            return queryByHSql.get(0);
        }
        return null;
    }


}
