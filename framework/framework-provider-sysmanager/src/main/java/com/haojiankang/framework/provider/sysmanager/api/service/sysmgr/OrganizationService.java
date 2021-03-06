package com.haojiankang.framework.provider.sysmanager.api.service.sysmgr;


import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysOrganization;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOOrganization;
import com.haojiankang.framework.provider.sysmanager.api.supports.TreeNode;
import com.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;

/**
 * 机构业务处理接口
 * 
 * @author ren7wei 2016年6月8日 上午11:23:45
 */
public interface OrganizationService extends BaseService<SysOrganization,VOOrganization> {
    /**
     * 根据节点路径加载节点信息
     * 
     * @return
     */
    TreeNode loadNode(String nodePath);

    VOOrganization findOrganizationByCode(String pcode);
}
