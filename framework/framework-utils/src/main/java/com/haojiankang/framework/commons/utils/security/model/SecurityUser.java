package com.haojiankang.framework.commons.utils.security.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.haojiankang.framework.commons.utils.i18n.LanguageType;
import com.haojiankang.framework.commons.utils.security.AuthenticationType;

/**
 * 安全用户信息基础类
 * 
 * @author ren7wei
 *
 */
/**
 * ClassName: SecurityUser Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). date: 2016年7月18日 上午10:31:56
 *
 * @author ren7wei
 * @version
 * @since JDK 1.8
 */

@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
public class SecurityUser implements IUser, Serializable {
    private String id;
    /**
     * 语言类型默认为简体中文
     */
    private LanguageType languageType = LanguageType.Simplified_Chinese;
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String userName;
    private List<SecurityRole> roles=new ArrayList<>();
    // 用于存放用户具有的所有权限的编码
    private String codes;
    // 用于存放用户具有的所有url认证类型的路径
    private String urls;
    // 用户类型信息,0为系统管理员
    private Integer userType;

    private SecurityDepartment department=new SecurityDepartment();
    private Map<String,Object> data=new HashMap<>();
    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public List<SecurityRole> getRoles() {
        return roles;
    }

    @Override
    public String authenticationURL() {
        if (urls == null) {
            StringBuilder sb = new StringBuilder();
            // &拼接authenticationRule
            Consumer<IJurisdiction> action = juris -> {
                if (juris.getType() == AuthenticationType.URL) {
                    sb.append(juris.getRule());
                    sb.append(",");
                }
            };
            iterationRoles(action);
            // 删除最后一个&
            urls = sb.length() > 0 ? sb.deleteCharAt(sb.length() - 1).toString() : sb.toString();
        }
        return urls;

    }

    /**
     * 
     * iterationRole:迭代roles集合对象
     *
     * @author ren7wei
     * @param action
     * @since JDK 1.8
     */
    private void iterationRoles(Consumer<IJurisdiction> action) {
        if (getRoles() != null) {
            getRoles().forEach(role -> {
                if (role.getJurisdiction() != null) {
                    role.getJurisdiction().forEach(action);
                }
            });
        }

    }

    @Override
    public String JurisdictionCode() {
        if (codes == null) {
            StringBuilder sb = new StringBuilder();
            // &拼接code
            Consumer<IJurisdiction> action = juris -> {
                sb.append(juris.getCode());
                sb.append("&");
            };
            iterationRoles(action);
            // 删除最后一个&
            codes = sb.length() > 0 ? sb.deleteCharAt(sb.length() - 1).toString() : sb.toString();
        }
        return codes;

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRoles(List<SecurityRole> roles) {
        this.roles = roles;
    }

    public void setLanguageType(LanguageType languageType) {
        this.languageType = languageType;
    }

    @Override
    public LanguageType getLanguageType() {
        return languageType;
    }

    public IDepartment getDepartment() {
        return department;
    }

    public void setDepartment(SecurityDepartment department) {
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    @Override
    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }


}
