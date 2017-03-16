/** 
 * Project Name:framework-provider 
 * File Name:EhcacheConf.java 
 * Package Name:com.ghit.framework.provider.conf.cache.ehcache 
 * Date:2017年3月14日下午1:48:24  
*/

package com.github.haojiankang.framework.provider.conf.cache.ehcache;

import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;

/**
 * ClassName:EhcacheConf <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年3月14日 下午1:48:24 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */

@Configuration
public class EhcacheConf {
    @Bean(name="cacheManager")
    public EhCacheCacheManager getEhCacheCacheManager() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(
                new DefaultResourceLoader().getResource("classpath:/context/ehcache-ehealthdata.xml"));
        EhCacheCacheManager manager = new EhCacheCacheManager();
        manager.setCacheManager(factoryBean.getObject());
        return manager;
    }
}
