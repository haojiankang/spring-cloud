package com.github.haojiankang.framework.commons.utils.security.model;

import java.util.List;

import com.github.haojiankang.framework.commons.utils.security.ResourceType;

public interface ResourceInformation extends Cloneable {
    String getId();

    /**
     * 返回资源类型
     * 
     * @return
     */
    ResourceType getResourceType();

    /**
     * 返回资源下的所有子资源
     * 
     * @return
     */
    List<? extends ResourceInformation> sons();

    /**
     * 获取菜单对应的权限编码
     * 
     * @return
     */
    String JurisdictionCode();

    /**
     * 获得资源名称
     * 
     * @return
     */
    String getResourceName();

    /**
     * 获得资源编号
     */
    String getResourceNo();

    /**
     * 获取资源属性
     * 
     * @return
     */
    String getProperties();

    /**
     * 根据资源路径获取资源信息 路径格式：资源1/资源2/资源3
     * 检索过程：在当前资源的子资源中寻找资源名称为资源1的资源信息，在找到的资源信息的子资源中寻找资源2，依次递归，直到找到最后一级资源
     * 找不到资源会返回空值,路径为空返回当前资源对象
     * 
     * @param path
     * @return
     */
    ResourceInformation getResource(String path);

    /**
     * 根据ID获取Resource对象
     * 
     * @param id
     * @return
     */
    ResourceInformation getResourceById(String id);

    /**
     * 根据No获取Resource对象
     * 
     * @param no
     * @return
     */
    ResourceInformation getResourceByNo(String no);

    /**
     * 转换为json格式化对象
     * 
     * @return
     */
    String jsonFormat();

    /**
     * 克隆对象
     * 
     * @return
     */
    ResourceInformation clone();

    /**
     * 获取上级资源信息
     * 
     * @return
     */
    ResourceInformation getParentResource();

    /**
     * 删除子元素，用==做为判断方式
     * 
     * @param resource
     * @return
     */
    boolean remove(ResourceInformation resource);

    /**
     * 增加子资源
     * 
     * @param resource
     */
    void add(ResourceInformation resource);

    void setResourceType(ResourceType resourceType);

    void setCode(String code);

    void setName(String name);

    void setNo(String no);

    void setProperties(String properties);

    void setIcon(String icon);

    void setId(String id);

    void setParentResource(ResourceInformation resource);
    /**
     * sort:对子节点进行排序. 
     * @author ren7wei
     * @since JDK 1.7
     */
    void sort();

}
