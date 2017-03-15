/** 
 * Project Name:EHealthData 
 * File Name:ConfigurationUtils.java 
 * Package Name:com.ghit.ecg.sysmgr.utils 
 * Date:2016年7月6日下午4:21:32  
*/

package com.ghit.framework.provider.sysmanager.supports.sysmgr;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.ghit.framework.commons.utils.Page;
import com.ghit.framework.commons.utils.spring.SpringUtils;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOConfiguration;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.ConfigurationService;
import com.ghit.framework.provider.sysmanager.api.supports.TreeNode;
import com.ghit.framework.provider.sysmanager.api.supports.security.context.Context;
import com.ghit.framework.provider.sysmanager.supports.ProviderConstant;
import com.ghit.framework.provider.sysmanager.supports.ProviderContext;

/**
 * ClassName:ConfigurationUtils <br/>
 * Function: 系统配置信息工具类. <br/>
 * Date: 2016年7月6日 下午4:21:32 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class ConfigurationUtils {
    private static ConfigurationUtils utils;
    private static final String CONTEXT_KEY = ProviderConstant.SYSMGR_CONFIGURATION_CACHE;
    Context context;
    ConfigurationService configurationService;

    private ConfigurationUtils() {
        context = ProviderContext.getContext();
        configurationService = SpringUtils.getBean(ConfigurationService.class);
    }

    public static ConfigurationUtils initialization() {
        if (utils == null)
            utils = new ConfigurationUtils();
        return utils;
    }

    public VOConfiguration getConfiguration(String name) {
        Map<String, VOConfiguration> map = getCache();
        return map.values().stream().filter(t -> {
            return StringUtils.equals(t.getConfigurationName(), name);
        }).findFirst().orElse(null);
    }

    public List<VOConfiguration> getConfigurations(String name) {
        Map<String, VOConfiguration> map = getCache();
        return map.values().stream().filter(t -> {
            return StringUtils.equals(t.getConfigurationName(), name);
        }).collect(Collectors.toList());
    }
    public List<VOConfiguration> getConfigurationsByGroup(String group) {
        Map<String, VOConfiguration> map = getCache();
        return map.values().stream().filter(t -> {
            return StringUtils.equals(t.getConfigurationGroup(), group);
        }).collect(Collectors.toList());
    }

    public VOConfiguration getConfiguration(String name, String oid) {
        Context context = ProviderContext.getContext();
        Object lookup = context.lookup(ProviderConstant.CONTEXT_TREENODE_ROOT);
        List<VOConfiguration> configurations = getConfigurations(name);
        TreeNode node = null;
        if (lookup != null) {
            node = (TreeNode) lookup;
            if (configurations != null) {
                TreeNode tnode = node.searchNodeById(oid);
                if (tnode != null) {
                    while (tnode != null) {
                        String id = tnode.getId();
                        VOConfiguration configuration = configurations.stream()
                                .filter(t -> StringUtils.equals(t.getOrganizationId(), id)).findFirst().orElse(null);
                        if (configuration != null)
                            return configuration;
                        tnode = tnode.getParentNode();
                    }
                }
                return configurations.stream().filter(t -> StringUtils.isEmpty(t.getOrganizationId())).findFirst()
                        .orElse(null);

            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<String, VOConfiguration> getCache() {
        Context context = ProviderContext.getContext();
        Object lookup = context.lookup(CONTEXT_KEY);
        Map<String, VOConfiguration> map = null;
        if (lookup == null) {
            map = new Hashtable<>();
            initCache(context, map);
            context.bind(CONTEXT_KEY, map);
        } else {
            map = (Map<String, VOConfiguration>) lookup;
        }
        return map;
    }

    /**
     * 
     * initCache:初始化缓存.
     *
     * @author ren7wei
     * @param context
     * @param map
     * @since JDK 1.8
     */
    protected void initCache(Context context, Map<String, VOConfiguration> map) {
        context.bind(CONTEXT_KEY, map);
        Map<String, List<VOConfiguration>> listGroup = configurationService.listGroup(new Page());
        listGroup.forEach((k, v) -> {
            v.forEach(t -> {
                map.put(t.getId(), t);
            });
        });
    }
}
