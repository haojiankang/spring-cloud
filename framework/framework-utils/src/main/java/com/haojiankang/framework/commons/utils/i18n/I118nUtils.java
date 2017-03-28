package com.haojiankang.framework.commons.utils.i18n;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.haojiankang.framework.commons.utils.FileUtils;

/**
 * 语言工具类
 * 
 * @author ren7wei
 *
 */
public class I118nUtils {

    protected final Log logger = LogFactory.getLog(getClass());
    private static I118nUtils i118n;
    private Map<String, Properties> i118nMap;

    private I118nUtils() {
        loadI118n();
    }

    /**
     * 加载i118n配置信息
     */
    private void loadI118n() {
        try {
            i118nMap = new HashMap<String, Properties>();
            // 加载i118n配置信息
            Properties properties = FileUtils.loadPropertie("/i118n/i118n.properties");
            Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();
            // 根据i118n配置信息加载对象的语言文件
            while (iterator.hasNext()) {
                Entry<Object, Object> next = iterator.next();
                Properties i118nValue = FileUtils.loadProperties("/i118n/" + next.getValue().toString());
                i118nMap.put(next.getKey().toString(), i118nValue);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 
     * i118n:返回I118nUtils实体(单态模式)
     *
     * @author ren7wei
     * @return I118nUtils实体
     * @since JDK 1.8
     */
    public static I118nUtils i118n() {
        if (i118n == null)
            i118n = new I118nUtils();
        return i118n;
    }

    /**
     * 
     * get:获取指定语言类型的资源信息
     *
     * @author ren7wei
     * @param key
     *            资源的名称
     * @param type
     *            语言类型
     * @return 资源的值
     * @since JDK 1.8
     */
    public static String get(String key, LanguageType type) {
        String value = i118n().i118nMap.get(type.name()).getProperty(key);
        if (value == null)
            value = key;
        return value;
    }
    /**
     * 
     * get:获取默认语言类型的资源信息
     *
     * @author ren7wei
     * @param key
     *            资源的名称
     * @return 资源的值
     * @since JDK 1.8
     */
    public static String get(String key) {
        String value = i118n().i118nMap.get(LanguageType.Simplified_Chinese.name()).getProperty(key);
        if (value == null)
            value = key;
        return value;
    }

}
