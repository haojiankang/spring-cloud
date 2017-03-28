package com.haojiankang.framework.provider.sysmanager.api.service.sysmgr;

import com.haojiankang.framework.commons.utils.security.model.IUser;
import com.haojiankang.framework.commons.utils.security.model.ResourceInformation;
import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysResource;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOResource;
import com.haojiankang.framework.provider.sysmanager.api.supports.TreeNode;
import com.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;

/**
 * 资源信息业务管理类
 * 
 * @author ren7wei
 *
 */
public interface ResourceService extends BaseService<SysResource,VOResource>{
    /**
     * 根据resourceName查找Resource
     * 
     * @param resourceName
     * @return
     */
    ResourceInformation loadResource(String resourceName);

    public TreeNode toTree(ResourceInformation resource);

    ResourceInformation loadResource(String resourceName, IUser user);
}
