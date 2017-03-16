package com.github.haojiankang.framework.commons.utils.security.model;

import java.io.Serializable;
import java.util.List;

/**
 * 角色信息接口
 * 
 * @author ren7wei
 *
 */
public interface IRole extends Serializable{
    /**
     * 获取所有权限信息
     * 
     * @return
     */
    List<? extends IJurisdiction> getJurisdiction();
}
