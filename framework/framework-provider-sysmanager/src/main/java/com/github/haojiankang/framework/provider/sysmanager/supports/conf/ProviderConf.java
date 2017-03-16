/** 
 * Project Name:framework-provider-sysmanager 
 * File Name:ProviderConf.java 
 * Package Name:com.ghit.framework.provider.sysmanager.supports.conf 
 * Date:2017年3月15日下午2:59:30  
*/

package com.github.haojiankang.framework.provider.sysmanager.supports.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:ProviderConf <br/>
 * Function: 服务提供者标准配置加载. <br/>
 * Date: 2017年3月15日 下午2:59:30 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@Configuration
@EnableConfigurationProperties(value = { ProviderProperties.class })
public class ProviderConf {
    @Autowired
    private ProviderProperties conf;

    @Bean(name="providerProperties")
    public ProviderProperties getProviderProperties() {
        return conf;
    }

}
