/** 
 * Project Name:EHealthData 
 * File Name:SharpEcg.java 
 * Package Name:com.ghit.ecg 
 * Date:2016年7月15日下午3:38:10  
*/

package com.ghit.framework.provider.sysmanager.supports.sysmgr;

import java.util.List;

import com.ghit.framework.commons.utils.spring.SpringUtils;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.User;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOConfiguration;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.OrganizationService;
import com.ghit.framework.provider.sysmanager.api.supports.TreeNode;
import com.ghit.framework.provider.sysmanager.api.supports.security.context.Context;
import com.ghit.framework.provider.sysmanager.supports.PS;
import com.ghit.framework.provider.sysmanager.supports.ProviderConstant;
import com.ghit.framework.provider.sysmanager.supports.ProviderContext;

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
