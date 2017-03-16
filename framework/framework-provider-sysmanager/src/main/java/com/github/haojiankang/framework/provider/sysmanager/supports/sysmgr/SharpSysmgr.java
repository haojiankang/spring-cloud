/** 
 * Project Name:EHealthData 
 * File Name:SharpEcg.java 
 * Package Name:com.ghit.ecg 
 * Date:2016年7月15日下午3:38:10  
*/

package com.github.haojiankang.framework.provider.sysmanager.supports.sysmgr;

import java.util.ArrayList;
import java.util.List;

import com.github.haojiankang.framework.commons.utils.bean.BeanUtils;
import com.github.haojiankang.framework.commons.utils.security.AuthenticationType;
import com.github.haojiankang.framework.commons.utils.security.context.Context;
import com.github.haojiankang.framework.commons.utils.security.model.IUser;
import com.github.haojiankang.framework.commons.utils.security.model.SecurityDepartment;
import com.github.haojiankang.framework.commons.utils.security.model.SecurityJurisdiction;
import com.github.haojiankang.framework.commons.utils.security.model.SecurityRole;
import com.github.haojiankang.framework.commons.utils.security.model.SecurityUser;
import com.github.haojiankang.framework.commons.utils.spring.SpringUtils;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.Jurisdiction;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.Role;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.User;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOConfiguration;
import com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr.OrganizationService;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.TreeNode;
import com.github.haojiankang.framework.provider.sysmanager.supports.ProviderConstant;
import com.github.haojiankang.framework.provider.sysmanager.supports.ProviderContext;
import com.github.haojiankang.framework.provider.utils.PS;

/**
 * ClassName:SharpEcg <br>
 * Function: 通用工具类，将复杂的通用代码调用进行封装,业务相关部分. <br>
 * Date: 2016年7月15日 下午3:38:10 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class SharpSysmgr {
    public static IUser convertUserToIUser(User user) {
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
        SecurityDepartment deparment = new SecurityDepartment();
        secUser.setDepartment(deparment);
        deparment.setId("-1");
        if (user.getOrganization() != null) {
            deparment.setCode(user.getOrganization().getCode());
            deparment.setId(user.getOrganization().getId());
            deparment.setName(user.getOrganization().getName());
        }
        secUser.setData(BeanUtils.map("loginName", user.getUserName()));
        return secUser;

    }
    /**
     * 
     * getConfigValue:获取系统配置信息指定配置项的值
     *
     * @author ren7wei
     * @param key
     *            配置项名称
     * @return 配置值
     * @since JDK 1.8
     */
    public static String getConfigValue(String key) {
        return getConfigValueDef(key, null);
    }

    /**
     * 
     * getConfigValue:获取系统配置信息指定配置项的值
     *
     * @author ren7wei
     * @param key
     *            配置项名称
     * @param oid
     *            机构标识
     * @return 配置值
     * @since JDK 1.8
     */
    public static String getConfigValue(String key, String oid) {
        return getConfigValueDef(key, oid, null);
    }

    /**
     * 
     * getConfigValueDef:获取系统配置信息指定配置项的值,并且在没有配置值的时候使用默认值
     *
     * @author ren7wei
     * @param key
     *            配置项名称
     * @param defaultValue
     *            默认值
     * @return 配置值
     * @since JDK 1.8
     */
    public static String getConfigValueDef(String key, String defaultValue) {
        VOConfiguration configuration = ConfigurationUtils.initialization().getConfiguration(key);
        if (configuration == null)
            return defaultValue;
        return configuration.getConfigurationValue();
    }

    public static List<VOConfiguration> getConfigsGroup(String group) {
        ConfigurationUtils utils = ConfigurationUtils.initialization();
        return utils.getConfigurationsByGroup(group);
    }

    /**
     * 
     * getConfigValueDef:获取系统配置信息指定配置项的值,并且在没有配置值的时候使用默认值
     *
     * @author ren7wei
     * @param key
     *            配置项名称
     * @param oid
     *            机构唯一标识
     * @param defaultValue
     *            默认值
     * @return 配置值
     * @since JDK 1.8
     */
    public static String getConfigValueDef(String key, String oid, String defaultValue) {
        VOConfiguration configuration = ConfigurationUtils.initialization().getConfiguration(key, oid);
        if (configuration == null)
            return defaultValue;
        return configuration.getConfigurationValue();
    }

    /**
     * 
     * userPwdMd5:对用户的密码进行md5处理
     *
     * @author ren7wei
     * @param user
     *            传入用户信息，密码应该是明文
     * @since JDK 1.8
     */
    public static void userPwdMd5(User user) {
        user.setPassword(md5(user.getPassword(), String.valueOf(user.getCreateTime().getTime())));
    }

    /**
     * 
     * md5:获取md5值
     *
     * @author ren7wei
     * @param args
     * @return
     * @since JDK 1.8
     */
    public static String md5(String str1, String str2) {
        return PS.md5(str1 + str2);
    }

    /**
     * 
     * dataDicLookup:获取指定字典（基于缓存）
     *
     * @author ren7wei
     * @param name
     * @return
     * @since JDK 1.8
     */
    public static Object dataDicLookup(String name) {
        return DataDictionaryUtils.initialization().lookup(name);
    }

    /**
     * 
     * dataDicUnbind:删除指定字典数据（基于缓存）
     *
     * @author ren7wei
     * @param name
     * @since JDK 1.8
     */
    public static void dataDicUnbind(String name) {
        DataDictionaryUtils.initialization().unbind(name);
    }

    /**
     * 
     * dataDicBind:绑定指定字典值（基于缓存）
     *
     * @author ren7wei
     * @param name
     * @param value
     * @since JDK 1.8
     */
    public static void dataDicBind(String name, String value) {
        DataDictionaryUtils.initialization().bind(name, value);
    }

    /**
     * 
     * getOrganizationRoot:获取机构根节点。
     * 
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    public static TreeNode getOrganizationRoot() {
        Context context = ProviderContext.getContext();
        Object lookup = context.lookup(ProviderConstant.CONTEXT_TREENODE_ROOT);
        if (lookup != null) {
            return (TreeNode) lookup;
        }
        return null;
    }

    public static class InitOrganization implements Runnable {

        @Override
        public void run() {
            OrganizationService bean = SpringUtils.getBean(OrganizationService.class);
            bean.loadNode("");
        }

    }
    

}
