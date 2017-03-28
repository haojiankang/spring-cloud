package com.haojiankang.framework.provider.sysmanager.api.service.sysmgr;

import java.util.List;

import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysRole;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VORole;
import com.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;

public interface RoleService extends BaseService<SysRole,VORole> {
    /**
     * 获取所有角色信息
     * 
     * @return
     */
    public List<VORole> loadRoles();

//    public Map<String, List<VOJurisdiction>> listReturnJuris(Page page);

}
