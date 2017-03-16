package com.github.haojiankang.framework.provider.sysmanager.service.sysmgr.impl;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.Jurisdiction;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOJurisdiction;
import com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr.JurisdictionService;
import com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.JurisdictionDao;
import com.github.haojiankang.framework.provider.sysmanager.supports.service.BaseServiceImpl;
import com.github.haojiankang.framework.provider.utils.hibernate.BaseDao;

@Service
public class JurisdictionServiceImpl extends BaseServiceImpl<Jurisdiction,VOJurisdiction> implements JurisdictionService {
    @Resource
    private JurisdictionDao jurisdictionDao;

    @Override
    public BaseDao<Jurisdiction, Serializable> getBaseDao() {
        return jurisdictionDao;
    }


}
