
package com.ghit.framework.provider.sysmanager.service.sysmgr.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ghit.framework.commons.utils.Page;
import com.ghit.framework.commons.utils.bean.BeanUtils;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Role;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VORole;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.RoleService;
import com.ghit.framework.provider.sysmanager.dao.sysmgr.RoleDao;
import com.ghit.framework.provider.sysmanager.supports.service.BaseServiceImpl;
import com.ghit.framework.provider.utils.hibernate.BaseDao;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role,VORole> implements RoleService {
    @Resource
    private RoleDao roleDao;

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }
    @Override
    protected Role getPO(VORole vo) {
        return BeanUtils.poVo(vo, poClass, "jurisdictions");
    }

    @Override
    public List<VORole> loadRoles() {
        return BeanUtils.poVoList(getRoleDao().getAllList(), voClass, "jurisdictions");
    }

    @Override
    public Page list(Page page) {
        roleDao.findPage(page);
        if(page.getResult()!=null)
            page.setResult(BeanUtils.poVoList(page.getResult(), voClass, "jurisdictions"));
        return page;
    }

//    @Override
//    public Map<String, List<VOJurisdiction>> listReturnJuris(Page page) {
//        Map<String, List<VOJurisdiction>> maps = new HashMap<>();
//        roleDao.findPage(page);
//        if (page.getResult() != null && page.getResult().size() > 0) {
//            @SuppressWarnings("unchecked")
//            Iterator<Role> iterator = (Iterator<Role>) page.getResult().iterator();
//            while (iterator.hasNext()) {
//                Role next = iterator.next();
//                next.getJurisdictions().toString();
//                maps.put(next.getId(), next.getJurisdictions());
//            }
//        }
//        return maps;
//    }

    @Override
    public BaseDao<Role, Serializable> getBaseDao() {
        return roleDao;
    }

}
