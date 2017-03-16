package com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.Organization;
import com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.OrganizationDao;
import com.github.haojiankang.framework.provider.utils.hibernate.BaseDaoImpl;

@Repository
public class OrganizationDaoImpl extends BaseDaoImpl<Organization, Serializable> implements OrganizationDao {
}
