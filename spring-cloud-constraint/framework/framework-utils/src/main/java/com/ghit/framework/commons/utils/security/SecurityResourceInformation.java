package com.ghit.framework.commons.utils.security;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ghit.framework.commons.utils.lang.StringUtil;

/**
 * 安全资源信息基础类
 * 
 * @author ren7wei
 *
 */
public class SecurityResourceInformation implements ResourceInformation, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 资源类型
     */
    private ResourceType resourceType;
    /**
     * 下级资源信息
     */
    private List<ResourceInformation> sons;
    /**
     * 资源权限编码
     */
    private String code;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源编号
     */
    private String no;
    /**
     * 资源图标
     */
    private String icon;
    /**
     * 资源属性
     */
    private String properties;
    /**
     * 上级资源
     */
    private ResourceInformation parentResource;
    private String id;

    @Override
    public ResourceType getResourceType() {
        return resourceType;
    }

    public List<ResourceInformation> sons() {
        return sons;
    }

    @Override
    public String JurisdictionCode() {
        return code;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceNo() {
        return no;
    }

    @Override
    public String getProperties() {
        return properties;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public ResourceInformation getResource(String path) {
        if (StringUtil.isEmpty(path))
            return this;
        String current = StringUtil.subByEndStr(path, "/");
        String children = StringUtil.subByStartStr(path, "/");
        if (sons != null) {
            Iterator<? extends ResourceInformation> it_sons = sons.iterator();
            while (it_sons.hasNext()) {
                ResourceInformation resource = it_sons.next();
                if (StringUtil.eq(resource.getResourceName(), current)) {
                    return resource.getResource(children);
                }
            }
        }
        return null;
    }

    /**
     * 克隆resource对象
     */
    @Override
    public ResourceInformation clone() {
        SecurityResourceInformation resource = new SecurityResourceInformation();
        resource.resourceType = this.resourceType;
        resource.code = this.code;
        resource.name = this.name;
        resource.no = this.no;
        resource.properties = this.properties;
        resource.icon = this.icon;
        resource.sons = new ArrayList<>();
        if (this.sons != null) {
            Iterator<? extends ResourceInformation> it_sons = sons.iterator();
            while (it_sons.hasNext()) {
                ResourceInformation next = it_sons.next();
                resource.sons.add(next.clone());
            }
        }
        return resource;
    }

    @Override
    public String jsonFormat() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"name\":\"");
        json.append(name);
        json.append("\",\"icon\":\"");
        json.append(icon);
        json.append("\",\"type\":\"");
        json.append(resourceType == null ? "" : resourceType.name());
        json.append("\",\"no\":\"");
        json.append(no);
        json.append("\",\"properties\":\"{");
        json.append(properties == null ? "" : properties);
        json.append("}\",\"sons\":[");
        if (sons != null) {
            Iterator<? extends ResourceInformation> it_sons = sons.iterator();
            while (it_sons.hasNext()) {
                ResourceInformation resource = it_sons.next();
                json.append(resource.jsonFormat());
                if (it_sons.hasNext()) {
                    json.append(",");
                }
            }
        }
        json.append("]");
        json.append("}");
        return json.toString();
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public ResourceInformation getResourceById(String id) {
        ResourceInformation resource = null;
        if (StringUtil.eq(this.getId(), id))
            resource = this;
        if (sons != null) {
            Iterator<? extends ResourceInformation> it_sons = sons.iterator();
            while (it_sons.hasNext()) {
                ResourceInformation next = it_sons.next().getResourceById(id);
                if (next != null) {
                    resource = next;
                    break;
                }
            }
        }
        return resource;
    }

    public boolean remove(ResourceInformation resource) {
        if (sons != null) {
            Iterator<? extends ResourceInformation> iterator = sons.iterator();
            while (iterator.hasNext()) {
                ResourceInformation next = iterator.next();
                if (next == resource) {
                    iterator.remove();
                    return true;
                }
            }
        }
        return false;
    }

    public ResourceInformation getParentResource() {
        return parentResource;
    }

    public void setParentResource(ResourceInformation parentResource) {
        this.parentResource = parentResource;
    }

    @Override
    public ResourceInformation getResourceByNo(String no) {
        ResourceInformation resource = null;
        if (StringUtil.eq(this.getResourceNo(), no))
            resource = this;
        if (sons != null) {
            Iterator<? extends ResourceInformation> it_sons = sons.iterator();
            while (it_sons.hasNext()) {
                ResourceInformation next = it_sons.next().getResourceByNo(no);
                if (next != null) {
                    resource = next;
                    break;
                }
            }
        }
        return resource;
    }

    @Override
    public void add(ResourceInformation resource) {
        if (sons == null)
            sons = new ArrayList<>();
        resource.setParentResource(this);
        sons.add(resource);
        sort();
    }

    @Override
    public void sort() {
        if (sons != null)
            sons.sort((x, y) -> x.getResourceNo().compareTo(y.getResourceNo()));
    }

    public List<ResourceInformation> getSons() {
        return sons;
    }

    public void setSons(List<ResourceInformation> sons) {
        this.sons = sons;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNo() {
        return no;
    }

    public String getIcon() {
        return icon;
    }

}
