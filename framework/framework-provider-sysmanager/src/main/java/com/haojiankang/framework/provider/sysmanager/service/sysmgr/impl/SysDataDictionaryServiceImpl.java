package com.haojiankang.framework.provider.sysmanager.service.sysmgr.impl;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.jws.WebMethod;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.haojiankang.framework.commons.utils.lang.StringUtil;
import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysDataDictionary;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VODataDictionary;
import com.haojiankang.framework.provider.sysmanager.api.service.sysmgr.DataDictionaryService;
import com.haojiankang.framework.provider.sysmanager.dao.sysmgr.DataDictionaryDao;
import com.haojiankang.framework.provider.sysmanager.supports.service.BaseServiceImpl;
import com.haojiankang.framework.provider.sysmanager.supports.sysmgr.SharpSysmgr;
import com.haojiankang.framework.provider.utils.hibernate.BaseDao;
import com.haojiankang.framework.provider.utils.validate.Validate;

/**
 * 
 * ClassName: DataDictionaryServiceImpl Function: 数据字典业务处理层接口实现. Reason: TODO
 * ADD REASON(可选). date: 2016年7月25日 下午1:22:31
 *
 * @author ren7wei
 * @version
 * @since JDK 1.8
 */
@Service
public class SysDataDictionaryServiceImpl extends BaseServiceImpl<SysDataDictionary,VODataDictionary> implements DataDictionaryService {

    @Resource
    private DataDictionaryDao dataDictionaryDao;

    @WebMethod(exclude = true)
    @Override
    public BaseDao<SysDataDictionary, Serializable> getBaseDao() {
        return dataDictionaryDao;
    }


    @Override
    @Validate
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean save(VODataDictionary t) {
        // 编辑时name属性被更改时需要删除缓存中存储的值
        String oldName = null;
        if (!StringUtils.isEmpty(t.getId())) {
            SysDataDictionary dataDictionary = dataDictionaryDao.get(t.getId());
            if (!StringUtil.eq(dataDictionary.getName(), t.getName())) {
                oldName = dataDictionary.getName();
            }
            dataDictionaryDao.evict(dataDictionary);
        }
        if (super.save(t)) {
            if (oldName != null)
                SharpSysmgr.dataDicUnbind(oldName);
            return true;
        }
        return false;
    }

    @Override
    protected boolean saveAfter(SysDataDictionary t, boolean beforeState, boolean isAdd) {
        if (beforeState) {
            SharpSysmgr.dataDicBind(t.getName(), t.getContent());
        }
        return super.saveAfter(t, beforeState, isAdd);
    }

    @Override
    protected boolean delAfter(VODataDictionary t, boolean beforeState) {
        if (beforeState) {
            SharpSysmgr.dataDicUnbind(t.getName());
        }
        return super.delAfter(t, beforeState);
    }

}
