package com.haojiankang.framework.provider.sysmanager.dao.sysmgr.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysOrganization;
import com.haojiankang.framework.provider.sysmanager.dao.sysmgr.OrganizationDao;
import com.haojiankang.framework.provider.utils.hibernate.BaseDaoImpl;

@Repository
public class SysOrganizationDaoImpl extends BaseDaoImpl<SysOrganization, Serializable> implements OrganizationDao {
}
