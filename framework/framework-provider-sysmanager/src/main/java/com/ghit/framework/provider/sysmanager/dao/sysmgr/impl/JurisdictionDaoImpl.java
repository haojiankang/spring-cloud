package com.ghit.framework.provider.sysmanager.dao.sysmgr.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Jurisdiction;
import com.ghit.framework.provider.sysmanager.dao.sysmgr.JurisdictionDao;
import com.ghit.framework.provider.utils.hibernate.BaseDaoImpl;

@Repository
public class JurisdictionDaoImpl extends BaseDaoImpl<Jurisdiction, Serializable> implements JurisdictionDao {

}
