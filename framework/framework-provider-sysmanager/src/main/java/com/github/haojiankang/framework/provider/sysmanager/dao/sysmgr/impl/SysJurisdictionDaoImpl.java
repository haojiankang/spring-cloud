package com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysJurisdiction;
import com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.JurisdictionDao;
import com.github.haojiankang.framework.provider.utils.hibernate.BaseDaoImpl;

@Repository
public class SysJurisdictionDaoImpl extends BaseDaoImpl<SysJurisdiction, Serializable> implements JurisdictionDao {

}
