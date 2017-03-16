package com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr;

import java.util.List;

import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.Role;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VORole;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;

public interface RoleService extends BaseService<Role,VORole> {
    /**
     * 获取所有角色信息
     * 
     * @return
     */
    public List<VORole> loadRoles();

//    public Map<String, List<VOJurisdiction>> listReturnJuris(Page page);

}
