package com.haojiankang.framework.provider.sysmanager.dao.sysmgr.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysResource;
import com.haojiankang.framework.provider.sysmanager.dao.sysmgr.ResourceDao;
import com.haojiankang.framework.provider.utils.hibernate.BaseDaoImpl;

@Repository
public class SysResourceDaoImpl extends BaseDaoImpl<SysResource, Serializable> implements ResourceDao {

}
