/** 
 * Project Name:EHealthData 
 * File Name:ConfigurationServiceImpl.java 
 * Package Name:com.ghit.ecg.sysmgr.service.impl 
 * Date:2016年7月1日上午10:41:58  
*/

package com.github.haojiankang.framework.provider.sysmanager.service.sysmgr.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.github.haojiankang.framework.commons.utils.Page;
import com.github.haojiankang.framework.commons.utils.bean.BeanUtils;
import com.github.haojiankang.framework.commons.utils.bean.ObjectUtils;
import com.github.haojiankang.framework.commons.utils.security.context.ContextContainer;
import com.github.haojiankang.framework.commons.utils.spring.SpringUtils;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.Configuration;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOConfiguration;
import com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr.ConfigurationService;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.DataOper;
import com.github.haojiankang.framework.provider.sysmanager.dao.sysmgr.ConfigurationDao;
import com.github.haojiankang.framework.provider.sysmanager.supports.service.BaseServiceImpl;
import com.github.haojiankang.framework.provider.sysmanager.supports.sysmgr.ConfigurationUtils;
import com.github.haojiankang.framework.provider.utils.hibernate.BaseDao;

/**
 * ClassName:ConfigurationServiceImpl <br/>
 * Function: 系统配置信息业务逻辑层 <br/>
 * Date: 2016年7月1日 上午10:41:58 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */

@Service
public class ConfigurationServiceImpl extends BaseServiceImpl<Configuration, VOConfiguration>
        implements ConfigurationService {
    @Resource
    private ConfigurationDao configurationDao;

    @Override
    public Map<String, List<VOConfiguration>> listGroup(Page page) {
        Map<String, List<VOConfiguration>> sfgGroup = new TreeMap<>();
        page.setRows(Integer.MAX_VALUE);
        page.setPage(1);
        configurationDao.findPage(page);
        if (page.getResult() == null)
            return null;
        @SuppressWarnings("unchecked")
        List<Configuration> cfgs = (List<Configuration>) page.getResult();
        if (cfgs != null && cfgs.size() > 0)
            cfgs.forEach(cfg -> {
                String groupName = cfg.getConfigurationGroup() == null ? "" : cfg.getConfigurationGroup();
                List<VOConfiguration> list = sfgGroup.get(groupName);
                if (list == null) {
                    list = new ArrayList<>();
                    sfgGroup.put(groupName, list);
                }
                list.add(BeanUtils.poVo(cfg, VOConfiguration.class, ""));
            });
        return sfgGroup;
    }

    @Override
    public BaseDao<Configuration, Serializable> getBaseDao() {
        return configurationDao;
    }

    public boolean callBack(Configuration configuration, DataOper oper) {
        Boolean flag = null;
        if (StringUtils.isNotEmpty(configuration.getCallback())) {
            try {
                // Class<ConfigurationCallback> callType =
                // (Class<ConfigurationCallback>)
                // Class.forName(configuration.getCallback());
                // ConfigurationCallback call = callType.newInstance();
                // flag = call.call(configuration,
                // ContextContainer.getContainer().getContext(), oper);
                flag = executeCallBack(configuration.getCallback(), configuration, oper);
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                ContextContainer.getContainer().getContext().appendBind("message", e.getMessage());
                flag = false;
            }
        } else {
            flag = true;
        }
        cacheDataProcessing(configuration, oper);
        return flag != null ? flag.booleanValue() : false;
    }

    /**
     * 
     * cache:缓存数据处理
     *
     * @author ren7wei
     * @param po
     * @param oper
     * @since JDK 1.8
     */
    protected void cacheDataProcessing(Configuration po, DataOper oper) {
        Map<String, VOConfiguration> map = ConfigurationUtils.initialization().getCache();
        VOConfiguration vo = BeanUtils.poVo(po, voClass, "");
        if (oper == DataOper.ADD) {
            map.put(po.getId(),vo);
        } else if (oper == DataOper.EDIT) {
            map.put(po.getId(), vo);
        } else if (oper == DataOper.REMOVE) {
            map.remove(po.getId());
        }
    }

    private boolean executeCallBack(String callback, Configuration configuration, DataOper oper) {
        if (ObjectUtils.isEmpty(callback)) {
            ContextContainer.getContainer().getContext().appendBind("message", "Callback is empty!");
            return false;
        }

        int intIdx = StringUtils.lastIndexOf(callback, ".");
        String beanName = StringUtils.left(callback, intIdx);
        String methodName = StringUtils.right(callback, callback.length() - intIdx - 1);

        Object bean = null;
        try {
            bean = SpringUtils.getBean(beanName);
        } catch (Exception e) {
        }

        Class<?> clazz = null;
        if (null == bean) {
            try {
                clazz = Class.forName(beanName);
                bean = SpringUtils.getBean(clazz);
            } catch (Exception e) {
            }
        }

        Boolean blReturn = null;
        if (null != bean) {
            try {
                blReturn = (Boolean) ObjectUtils.getObjectByBean(bean, methodName, new Object[] { configuration, oper },
                        new Class[] { Configuration.class, DataOper.class });
            } catch (Exception e) {
            }
        }

        if (null == blReturn) {
            try {
                if (null == clazz) {
                    clazz = Class.forName(beanName);
                }
                blReturn = (Boolean) ObjectUtils.getObjectByClass(clazz, methodName,
                        new Object[] { configuration, oper }, new Class[] { Configuration.class, DataOper.class });
            } catch (Exception e) {
            }
        }
        return blReturn;
    }


}
