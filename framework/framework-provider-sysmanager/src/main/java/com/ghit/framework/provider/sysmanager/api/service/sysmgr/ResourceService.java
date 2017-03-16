package com.ghit.framework.provider.sysmanager.api.service.sysmgr;

import com.ghit.framework.commons.utils.security.model.IUser;
import com.ghit.framework.commons.utils.security.model.ResourceInformation;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Resource;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOResource;
import com.ghit.framework.provider.sysmanager.api.supports.TreeNode;
import com.ghit.framework.provider.sysmanager.api.supports.service.BaseService;

/**
 * 资源信息业务管理类
 * 
 * @author ren7wei
 *
 */
public interface ResourceService extends BaseService<Resource,VOResource>{
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
