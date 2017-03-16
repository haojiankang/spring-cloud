/** 
 * Project Name:EHealthData 
 * File Name:DataDictionaryUtils.java 
 * Package Name:com.ghit.ecg.sysmgr.utils 
 * Date:2016年7月25日下午4:26:13  
*/

package com.ghit.framework.provider.sysmanager.supports.sysmgr;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.ghit.framework.commons.utils.Page;
import com.ghit.framework.commons.utils.spring.SpringUtils;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.DataDictionary;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.DataDictionaryService;
import com.ghit.framework.provider.sysmanager.api.supports.security.context.Context;
import com.ghit.framework.provider.sysmanager.supports.ProviderConstant;
import com.ghit.framework.provider.sysmanager.supports.ProviderContext;
import com.ghit.framework.provider.utils.PS;

/**
 * ClassName:DataDictionaryUtils <br>
 * Function: 数据字典工具类. <br>
 * Date: 2016年7月25日 下午4:26:13 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class DataDictionaryUtils {
    private static final String CONTEXT_KEY = ProviderConstant.SYSMGR_DATA_DICTIONARY_CACHE;
    private static DataDictionaryUtils utils;
    DataDictionaryService service;
    Context context;

    private DataDictionaryUtils() {
        context = ProviderContext.getContext();
        service = SpringUtils.getBean(DataDictionaryService.class);
    }

    public static DataDictionaryUtils initialization() {
        if (utils == null)
            utils = new DataDictionaryUtils();
        return utils;
    }

    public Object lookup(String name) {
        return getCache().get(name);
    }

    public void bind(String name, String value) {
        getCache().put(name, PS.strToJsonObj(value));
    }

    public void unbind(String name) {
        getCache().remove(name);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getCache() {
        Object lookup = context.lookup(CONTEXT_KEY);
        Map<String, Object> map = null;
        if (lookup == null) {
            map = new Hashtable<>();
            initCache(context, map);
            context.bind(CONTEXT_KEY, map);
        } else {
            map = (Map<String, Object>) lookup;
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
    @SuppressWarnings("unchecked")
    protected void initCache(Context context, Map<String, Object> map) {
        context.bind(CONTEXT_KEY, map);
        Page page = new Page();
        page.setRows(Integer.MAX_VALUE);
        service.list(page);
        ((List<DataDictionary>) page.getResult()).forEach(d -> {
            map.put(d.getName(), PS.strToJsonObj(d.getContent()));
        });
    }
}
