package com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.Resource;
import com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.ResourceDao;
import com.github.haojiankang.framework.provider.utils.hibernate.BaseDaoImpl;

@Repository
public class ResourceDaoImpl extends BaseDaoImpl<Resource, Serializable> implements ResourceDao {

}
