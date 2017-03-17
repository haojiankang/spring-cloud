package com.github.haojiankang.framework.provider.sysmanager.service.sysmgr.impl;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysJurisdiction;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOJurisdiction;
import com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr.JurisdictionService;
import com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.JurisdictionDao;
import com.github.haojiankang.framework.provider.sysmanager.supports.service.BaseServiceImpl;
import com.github.haojiankang.framework.provider.utils.hibernate.BaseDao;

@Service
public class SysJurisdictionServiceImpl extends BaseServiceImpl<SysJurisdiction,VOJurisdiction> implements JurisdictionService {
    @Resource
    private JurisdictionDao jurisdictionDao;

    @Override
    public BaseDao<SysJurisdiction, Serializable> getBaseDao() {
        return jurisdictionDao;
    }


}
