package com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.Role;
import com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.RoleDao;
import com.github.haojiankang.framework.provider.utils.hibernate.BaseDaoImpl;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role, Serializable> implements RoleDao {
}
