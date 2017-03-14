/** 
 * Project Name:framework-consumer 
 * File Name:ConsumerConf.java 
 * Package Name:com.ghit.framework.consumer.conf 
 * Date:2017年3月14日下午5:13:57  
*/

package com.ghit.framework.consumer.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ghit.framework.consumer.utils.security.filter.SecurityManagerFilter;
import com.ghit.framework.consumer.utils.security.filter.SessionManagerFilter;

/**
 * ClassName:ConsumerConf <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年3月14日 下午5:13:57 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@Configuration
@EnableConfigurationProperties(value = { SecurityProperties.class })
public class ConsumerConf {
    @Autowired
    SecurityProperties conf;

    @Bean
    public FilterRegistrationBean filterRegistrationBeanBySession() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        SessionManagerFilter session = new SessionManagerFilter(conf);
        registrationBean.setFilter(session);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBeanBySecurity() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        SecurityManagerFilter security = new SecurityManagerFilter(conf);
        registrationBean.setFilter(security);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
