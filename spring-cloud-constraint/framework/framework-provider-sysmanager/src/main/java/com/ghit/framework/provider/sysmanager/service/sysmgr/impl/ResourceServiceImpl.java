package com.ghit.framework.provider.sysmanager.service.sysmgr.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ghit.framework.commons.utils.lang.StringUtil;
import com.ghit.framework.commons.utils.security.model.IUser;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Resource;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOResource;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.ResourceService;
import com.ghit.framework.provider.sysmanager.api.supports.TreeNode;
import com.ghit.framework.provider.sysmanager.api.supports.security.ResourceInformation;
import com.ghit.framework.provider.sysmanager.api.supports.security.ResourceType;
import com.ghit.framework.provider.sysmanager.api.supports.security.SecurityResourceInformation;
import com.ghit.framework.provider.sysmanager.api.supports.security.context.Context;
import com.ghit.framework.provider.sysmanager.dao.sysmgr.ResourceDao;
import com.ghit.framework.provider.sysmanager.supports.ProviderConstant;
import com.ghit.framework.provider.sysmanager.supports.ProviderContext;
import com.ghit.framework.provider.sysmanager.supports.service.BaseServiceImpl;
import com.ghit.framework.provider.utils.hibernate.BaseDao;

@Service
public class ResourceServiceImpl extends BaseServiceImpl<Resource,VOResource> implements ResourceService {
    @javax.annotation.Resource
    ResourceDao resourceDao;

    private Context context;
    {
        context = ProviderContext.getContext();
    }

    @Override
    public ResourceInformation loadResource(String resourceName) {
        ResourceInformation root = (ResourceInformation) context.lookup(ProviderConstant.CONTEXT_RESOURCE_ROOT);
        if (root == null) {
            root = loadRoot();
            context.bind(ProviderConstant.CONTEXT_RESOURCE_ROOT, root);
        }
        return root.getResource(resourceName);
    }

    @Override
    public ResourceInformation loadResource(String resourceName, IUser user) {
        ResourceInformation root = (ResourceInformation) context.lookup(ProviderConstant.CONTEXT_RESOURCE_ROOT);
        if (root == null) {
            root = loadRoot();
            context.bind(ProviderConstant.CONTEXT_RESOURCE_ROOT, root);
        }
        return ProviderContext.getResource(user).getResource(resourceName);
    }

    public TreeNode toTree(ResourceInformation resource) {
        TreeNode node = new TreeNode();
        convert(resource, node);
        return node;
    }

    private void convert(ResourceInformation resource, TreeNode node) {
        if (resource != null) {
            node.setId(resource.getId());
            node.setName(resource.getResourceName());
            node.setIsHidden(resource.getResourceType() == ResourceType.Button);
            node.setExtend(new StringBuilder(
                    "({juris_code:\"" + (resource.JurisdictionCode() != null ? resource.JurisdictionCode() : "")
                            + "\",resourceType:\"" +(resource.getResourceType()==null?-1:resource.getResourceType().getIndex() ) + "\",properties:\""
                            + (resource.getProperties() != null ? resource.getProperties() : "") + "\",no:\""
                            + (resource.getResourceNo() != null ? resource.getResourceNo() : "") + "\"})").toString());
            if (resource.sons() != null && resource.sons().size() > 0) {
                List<TreeNode> nodes = new ArrayList<TreeNode>();
                for (ResourceInformation res : resource.sons()) {
                    TreeNode t = new TreeNode();
                    nodes.add(t);
                    convert(res, t);
                }
                node.setChildren(nodes);
            }
        }

    }

    private ResourceInformation loadRoot() {
        SecurityResourceInformation rootResource = new SecurityResourceInformation();
        List<Resource> resources = resourceDao.getAllList();
        // 查找根节点
        findRoot(rootResource, resources);
        // 装载子节点
        loadSons(rootResource, resources);
        return rootResource;
    }

    /**
     * 查找根节点
     * 
     * @param rootResource
     * @param resources
     */
    private void findRoot(SecurityResourceInformation rootResource, List<Resource> resources) {
        Iterator<Resource> it_root = resources.iterator();
        Resource root = null;
        while (it_root.hasNext()) {
            Resource next = it_root.next();
            if (next.getResourceType()!=null&& next.getResourceType() == ResourceType.Root.getIndex()) {
                root = next;
                copy(root, rootResource);
                it_root.remove();
                break;
            }
        }
        if (root == null)
            throw new RuntimeException("Root node not found!");
    }

    /**
     * 装载子节点
     */
    private void loadSons(ResourceInformation root, List<Resource> list) {
        Iterator<Resource> iterator = list.iterator();
        while (iterator.hasNext()) {
            Resource next = iterator.next();
            if (StringUtil.eq(next.getParentNO(), root.getResourceNo())) {
                ResourceInformation secRecouse = new SecurityResourceInformation();
                copy(next, secRecouse);
                loadSons(secRecouse, list);
                root.add(secRecouse);
            }
        }
    }

    /**
     * 拷贝节点信息
     */
    private void copy(Resource src, ResourceInformation dis) {
        dis.setCode(src.getJuris_code());
        dis.setIcon(src.getIcon());
        dis.setName(src.getResourceName());
        dis.setNo(src.getNo());
        dis.setId(src.getId());
        dis.setProperties(src.getProperties());
        if (src.getResourceType() == null) {
            dis.setResourceType(null);
        } else {
            dis.setResourceType(ResourceType.parser(src.getResourceType()));
        }
    }

    @Override
    protected boolean delAfter(VOResource resource, boolean beforeState) {
        ResourceInformation current = loadResource("").getResourceById(resource.getId());
        current.getParentResource().remove(current);
        return true;
    }

    @Override
    protected boolean saveAfter(Resource resource, boolean beforeState, boolean isAdd) {
        if (isAdd) {
            ResourceInformation parent = loadResource("").getResourceByNo(resource.getParentNO());
            SecurityResourceInformation sec = new SecurityResourceInformation();
            copy(resource, sec);
            parent.add(sec);
        } else {
            ResourceInformation current = loadResource("").getResourceById(resource.getId());
            copy(resource, current);
            current.getParentResource().sort();
        }
        return true;
    }

    @Override
    public BaseDao<Resource, Serializable> getBaseDao() {
        return resourceDao;
    }

}
