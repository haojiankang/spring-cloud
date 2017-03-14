package com.ghit.framework.provider.sysmanager.dao.sysmgr;

import java.io.Serializable;

import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.User;
import com.ghit.framework.provider.utils.hibernate.BaseDao;

public interface UserDao extends BaseDao<User, Serializable> {

    public User findUsers(User user);

    public User findUsersByName(User user);
}
