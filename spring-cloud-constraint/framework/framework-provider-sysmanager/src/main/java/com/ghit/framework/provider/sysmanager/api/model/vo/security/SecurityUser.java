package com.ghit.framework.provider.sysmanager.api.model.vo.security;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.ghit.framework.commons.utils.bean.BeanUtils;
import com.ghit.framework.commons.utils.i18n.LanguageType;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Jurisdiction;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Role;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.User;
import com.ghit.framework.provider.sysmanager.api.supports.security.AuthenticationType;
import com.ghit.framework.provider.sysmanager.api.supports.security.model.IDepartment;
import com.ghit.framework.provider.sysmanager.api.supports.security.model.IJurisdiction;
import com.ghit.framework.provider.sysmanager.api.supports.security.model.IRole;
import com.ghit.framework.provider.sysmanager.api.supports.security.model.IUser;

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
    private List<? extends IRole> roles;
    // 用于存放用户具有的所有权限的编码
    private String codes;
    // 用于存放用户具有的所有url认证类型的路径
    private String urls;
    // 用户类型信息,0为系统管理员
    private Integer userType;

    private SecurityDepartment department;
    private Map<String,Object> data;
    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public List<? extends IRole> getRoles() {
        return roles;
    }

    @Override
    public String authenticationURL() {
        if (urls == null) {
            StringBuilder sb = new StringBuilder();
            // &拼接authenticationRule
            Consumer<IJurisdiction> action = juris -> {
                if (juris.getAuthenticationType() == AuthenticationType.URL) {
                    sb.append(juris.getAuthenticationRule());
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

    public void setRoles(List<? extends IRole> roles) {
        this.roles = roles;
    }

    public static SecurityUser paserUser(User user) {
        SecurityUser secUser = new SecurityUser();
        secUser.setUserName(user.getFullname());
        secUser.setId(user.getId());
        secUser.setUserType(user.getUserType());
        if (user.getRoles() != null) {
            List<SecurityRole> secRoles = new ArrayList<SecurityRole>();
            for (Role role : user.getRoles()) {
                if (role.getJurisdictions() != null) {
                    SecurityRole secRole = new SecurityRole();
                    List<SecurityJurisdiction> secJurisList = new ArrayList<SecurityJurisdiction>();
                    for (Jurisdiction juris : role.getJurisdictions()) {
                        SecurityJurisdiction secJuris = new SecurityJurisdiction();
                        secJuris.setCode(juris.getJurisdictionCode());
                        secJuris.setRule(juris.getAuthenticationRule());
                        if (juris.getAuthenticationType() != null)
                            secJuris.setType(AuthenticationType.valueOf(juris.getAuthenticationType()));
                        secJurisList.add(secJuris);
                    }
                    secRole.setJurisdictions(secJurisList);
                    secRoles.add(secRole);
                }
            }
            secUser.setRoles(secRoles);
        }
        secUser.department = new SecurityDepartment();
        secUser.department.setId("-1");
        if (user.getOrganization() != null) {
            secUser.department.setCode(user.getOrganization().getCode());
            secUser.department.setId(user.getOrganization().getId());
            secUser.department.setName(user.getOrganization().getName());
        }
        secUser.data=BeanUtils.map("loginName", user.getUserName());
        return secUser;

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
