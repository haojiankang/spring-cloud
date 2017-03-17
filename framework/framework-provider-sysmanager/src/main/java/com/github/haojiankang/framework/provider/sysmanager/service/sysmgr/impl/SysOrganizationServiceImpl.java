package com.github.haojiankang.framework.provider.sysmanager.service.sysmgr.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.haojiankang.framework.commons.utils.Page;
import com.github.haojiankang.framework.commons.utils.bean.BeanUtils;
import com.github.haojiankang.framework.commons.utils.lang.StringUtil;
import com.github.haojiankang.framework.commons.utils.security.context.Context;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysOrganization;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOOrganization;
import com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr.OrganizationService;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.TreeNode;
import com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.OrganizationDao;
import com.github.haojiankang.framework.provider.sysmanager.supports.ProviderConstant;
import com.github.haojiankang.framework.provider.sysmanager.supports.ProviderContext;
import com.github.haojiankang.framework.provider.sysmanager.supports.service.BaseServiceImpl;
import com.github.haojiankang.framework.provider.sysmanager.supports.sysmgr.SharpSysmgr;
import com.github.haojiankang.framework.provider.utils.hibernate.BaseDao;

@Service
public class SysOrganizationServiceImpl extends BaseServiceImpl<SysOrganization, VOOrganization>
        implements OrganizationService {

    @Resource
    OrganizationDao organizationDao;

    public OrganizationDao getOrganizationDao() {
        return organizationDao;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TreeNode loadNode(String nodePath) {
        TreeNode root = loadRoot();
        return root.getNode(nodePath);
    }

    @Override
    protected boolean saveBefore(VOOrganization organization, boolean isAdd) {
        if (isAdd) {
            SysOrganization po = BeanUtils.poVo(organization, poClass, "");
            generateCode(po);
        }
        return super.saveBefore(organization, isAdd);
    }

    @Override
    protected boolean saveAfter(SysOrganization organization, boolean beforeState, boolean isAdd) {
        if (beforeState) {
            TreeNode root = loadRoot();
            if (isAdd) {
                TreeNode node = root.searchNodeByCode(organization.getPcode());
                TreeNode children = new TreeNode();
                copy(organization, children);
                node.getChildren().add(children);
                children.setParentNode(node);
            } else {
                TreeNode node = root.searchNodeByCode(organization.getCode());
                copy(organization, node);
            }
        }
        return beforeState;
    }

    @Override
    protected boolean delAfter(VOOrganization t, boolean beforeState) {
        TreeNode root = loadRoot();
        TreeNode node = root.searchNodeByCode(t.getCode());
        node.getParentNode().getChildren().removeIf(n -> {
            return StringUtils.equals(n.getId(), t.getId());
        });
        return true;
    }

    @SuppressWarnings({ "unchecked" })
    private void generateCode(SysOrganization organization) {
        Page page = new Page();
        page.setRows(32 * 32);
        page.getConditions().put("pcode", organization.getPcode());
        organizationDao.findPage(page);
        List<?> result = page.getResult();
        if (result == null) {
            organization.setCode(organization.getPcode() + "" + 01);
        } else {
            int[] ins = new int[32 * 32];
            ((List<SysOrganization>) result).forEach(o -> {
                ins[Integer.parseInt(o.getCode().replace(o.getPcode(), ""), 32) - 1] = 1;
            });
            int index = 0;
            do {
                if (ins[index] == 0) {
                    organization.setCode(Integer.toString(index + 1, 32));
                    organization.setCode(organization.getCode().length() == 1 ? "0" + organization.getCode()
                            : organization.getCode());
                    organization.setCode(organization.getPcode() + organization.getCode());
                    return;
                }
                index++;
            } while (index < ins.length);
        }
    }

    @Override
    public VOOrganization findOrganizationByCode(String code) {
        SysOrganization organization = new SysOrganization();
        TreeNode node = loadNode("");
        node = node.searchNodeByCode(code);
        if (node != null) {
            organization.setCode(node.getCode());
            organization.setName(node.getName());
            organization.setPcode(node.getParentNode() != null ? node.getParentNode().getCode() : "R");
        }
        return BeanUtils.poVo(organization,VOOrganization.class,"");
    }

    /**
     * 加载根节点信息
     * 
     * @return
     */
    private TreeNode loadRoot() {
        Context context = ProviderContext.getContext();
        Object lookup = context.lookup(ProviderConstant.CONTEXT_TREENODE_ROOT);
        if (lookup != null) {
            return (TreeNode) lookup;
        }
        TreeNode treenode = new TreeNode();
        Page page = new Page();
        page.setRows(Integer.MAX_VALUE);
        page.setOrders(new ArrayList<>());
        page.getOrders().add("+createTime");
        getOrganizationDao().findPage(page);
        @SuppressWarnings("unchecked")
        List<SysOrganization> organizations = (List<SysOrganization>) page.getResult();
        // 创建根节点
        createRoot(treenode);
        // 装载子节点
        loadSons(treenode, organizations);
        context.bind(ProviderConstant.CONTEXT_TREENODE_ROOT, treenode);
        return treenode;
    }

    /**
     * 在全部节点列表汇总查找根节点
     * 
     * @param rootNode
     * @param rganizations
     */
    private void createRoot(TreeNode rootNode) {
        rootNode.setName("机构信息");
        rootNode.setCode("R");
        rootNode.setId("-1");
        rootNode.setExtend("R");
    }

    private String getParentIndex(SysOrganization o, Map<String, SysOrganization> map, Set<String> indexCache) {
        SysOrganization po = map.get(o.getPcode());
        if (po == null)
            return "R";
        String start = po.getCodeindex();
        if (start == null) {
            start = getParentIndex(map.get(o.getPcode()), map, indexCache);
            start = generateIndex(start, indexCache);
        }
        return start;
    }

    private String generateIndex(String start, Set<String> indexCache) {
        int i = 0;
        String no = "";
        do {
            no = Integer.toString(i, 32);
            if (i <= 32)
                no = "0" + no;
            i++;
        } while (indexCache.contains(start + no));
        indexCache.add(start + no);
        return start + no;

    }

    /**
     * 装载子节点
     */
    private void loadSons(TreeNode rootNode, List<SysOrganization> list) {
        Map<String, TreeNode> ptree = new HashMap<>();
        // Map<String, Integer> pcount = new HashMap<>();
        Map<String, List<TreeNode>> ctree = new HashMap<>();
        ptree.put("R", rootNode);
        String[] rootCode = SharpSysmgr.getConfigValueDef(ProviderConstant.SYSMGR_CONFIG_TREE_ROOT, "").split(",");
        Map<String, SysOrganization> map = list.stream().collect(Collectors.toMap(k -> k.getCode(), v -> v));
        Set<String> indexCache = list.stream().map(o -> o.getCodeindex()).collect(Collectors.toSet());
        list.forEach(o -> {
            o.getPcode();
        });
        list.forEach(o -> {
            // 是否是顶级节点
            boolean isTop = StringUtil.in(o.getPcode(), rootCode) || StringUtil.eq("R", o.getPcode())
                    || StringUtil.isEmpty(o.getPcode());
            // 是否需要创建codeindex
            boolean isCreateIndex = false;
            // 顶级节点索引以R开始
            String start = "R";
            if (!isTop) {
                // 非顶级节点索引以父节点的索引为开头
                start = getParentIndex(o, map, indexCache);
            }
            TreeNode node = new TreeNode();
            // 无编码索引，建立编码索引
            if (o.getCodeindex() == null || o.getCodeindex().trim().length() == 0) {
                isCreateIndex = true;
            } else {
                // 有编码索引，判断编码索引是否正确
                // 以start开头，并且索引位数为len(start)+2位，表示索引正确
                if (o.getCodeindex().startsWith(start) && o.getCodeindex().trim().length() == start.length() + 2) {
                    indexCache.add(o.getCodeindex());
                    isCreateIndex = false;
                } else {
                    isCreateIndex = true;
                }
            }
            // 生成索引
            if (isCreateIndex) {
                o.setCodeindex(generateIndex(start, indexCache));
            }

            copy(o, node);
            // 存在子节点列表，将子节点列表放入当前节点中
            List<TreeNode> chs = ctree.get(o.getCode());
            if (chs == null) {
                chs = new ArrayList<>();
                ctree.put(o.getCode(), chs);
            }
            node.setChildren(chs);

            TreeNode parentNode = null;
            // 判断父节点是否是顶级节点
            if (isTop) {
                parentNode = ptree.get("R");
                if (parentNode.getChildren() == null)
                    parentNode.setChildren(new ArrayList<TreeNode>());
                parentNode.getChildren().add(node);
            } else {
                // 查找父节点
                parentNode = ptree.get(o.getPcode());
                // 找到父节点，将本节点加入父节点
                if (parentNode != null) {
                    parentNode.getChildren().add(node);
                } else {
                    // 未找到父节点，将子节点暂存到ctree
                    List<TreeNode> ct = ctree.get(o.getPcode());
                    if (ct == null) {
                        ctree.put(o.getPcode(), new ArrayList<>());
                        ct = ctree.get(o.getPcode());
                    }
                    ct.add(node);

                }
            }

        });
    }

    /**
     * 拷贝节点信息
     */
    private void copy(SysOrganization src, TreeNode dis) {
        dis.setName(src.getName());
        dis.setCode(src.getCode());
        dis.setIndex(src.getCodeindex());
        dis.setId(src.getId());
    }

    @Override
    public BaseDao<SysOrganization, Serializable> getBaseDao() {
        return organizationDao;
    }

}
