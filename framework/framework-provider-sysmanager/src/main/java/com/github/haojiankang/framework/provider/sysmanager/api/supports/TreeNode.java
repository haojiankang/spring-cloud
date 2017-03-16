package com.github.haojiankang.framework.provider.sysmanager.api.supports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.haojiankang.framework.commons.utils.lang.StringUtil;

/**
 * 树的节点信息实体
 * 
 * @author ren7wei 2016年6月8日 上午9:47:33
 */
public class TreeNode implements Serializable {

    /**
     * 
     */
    private String id;
    private static final long serialVersionUID = 1L;
    private int level;
    @JsonIgnore
    private TreeNode parentNode;
    private String code;
    private String extend;
    private Boolean isParent;
    private String index;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 子节点集合
     */
    private List<TreeNode> children;
    /**
     * 是否隐藏
     */
    private boolean isHidden;

    /**
     * 
     * getName:获取节点的name属性
     *
     * @author ren7wei
     * @return name
     * @since JDK 1.8
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * setName:设置节点的name属性
     *
     * @author ren7wei
     * @param name
     *            节点的name属性值
     * @since JDK 1.8
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * getChildren:获得当前节点的子节点。
     *
     * @author ren7wei
     * @return 子节点集合
     * @since JDK 1.8
     */
    public List<TreeNode> getChildren() {
        if (children == null)
            children = new ArrayList<>();
        return children;
    }

    /**
     * 
     * setChildren:给当前节点设置子节点集合。
     *
     * @author ren7wei
     * @param children
     *            子节点集合
     * @since JDK 1.8
     */
    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    /**
     * 
     * getCode:获得当前节点的code属性
     *
     * @author ren7wei
     * @return code属性值
     * @since JDK 1.8
     */
    public String getCode() {
        return code;
    }

    /**
     * 
     * setCode:设置当前节点的code属性
     *
     * @author ren7wei
     * @param code
     *            code属性值
     * @since JDK 1.8
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 
     * clone:对当前节点进行深度克隆
     *
     * @author ren7wei
     * @return 当前节点的克隆体
     * @since JDK 1.8
     */
    public TreeNode clone() {
        TreeNode treeNode = new TreeNode();
        treeNode.name = this.name;
        treeNode.id = this.id;
        treeNode.code = this.code;
        treeNode.children = new ArrayList<TreeNode>();
        if (this.children != null) {
            Iterator<TreeNode> it_sons = children.iterator();
            while (it_sons.hasNext()) {
                TreeNode res = (TreeNode) it_sons.next();
                treeNode.children.add((TreeNode) res.clone());
            }
        }
        return treeNode;
    }

    /**
     * 
     * getNode:查找节点.
     * <p>
     * 根据路径在当前节点下查找子节点， path为空时返回当前节点。<br>
     * 例：/a/b/c => this.children[a].children[b].children[c] <br>
     * 未找到会返回 null
     * </p>
     * 
     * @author ren7wei
     * @param path
     *            查找路径
     * @return 查找到的节点
     * @since JDK 1.8
     */
    public TreeNode getNode(String path) {
        if (StringUtil.isEmpty(path))
            return this;
        String current = StringUtil.subByEndStr(path, "/");
        String children = StringUtil.subByStartStr(path, "/");
        if (children != null) {
            Iterator<TreeNode> it_child = this.children.iterator();
            while (it_child.hasNext()) {
                TreeNode treeNode = it_child.next();
                if (StringUtil.eq(treeNode.getName(), current)) {
                    return treeNode.getNode(children);
                }
            }
        }
        return null;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public TreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(TreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * 
     * searchNodeByCode:在当前节点及其后辈节点中查找code值与输入参数相同的节点。
     *
     * @author ren7wei
     * @param code code属性
     * @return 查找到的节点
     * @since JDK 1.8
     */
    public TreeNode searchNodeByCode(String code) {
        if (StringUtils.equals(code, this.getCode())) {
            return this;
        }
        TreeNode tNode = null;
        if (this.getChildren() != null) {
            for (TreeNode item : this.getChildren()) {
                tNode = item.searchNodeByCode(code);
                if (tNode != null)
                    break;
            }
        }
        return tNode;

    }
    /**
     * 
     * searchNodeById:在当前节点及其后辈节点中查找id值与输入参数相同的节点。
     *
     * @author ren7wei
     * @param id id属性
     * @return 查找到的节点
     * @since JDK 1.8
     */
    public TreeNode searchNodeById(String id) {
        if (id.equals(this.getId())) {
            return this;
        }
        TreeNode tNode = null;
        if (this.getChildren() != null) {
            for (TreeNode item : this.getChildren()) {
                tNode = item.searchNodeById(id);
            }
        }
        return tNode;

    }
}
