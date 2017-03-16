/** 
 * Project Name:framework-provider 
 * File Name:ConsumerConf.java 
 * Package Name:com.ghit.framework.provider.conf 
 * Date:2017年3月16日上午11:46:43  
*/  
  
package com.github.haojiankang.framework.provider.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.haojiankang.framework.provider.utils.security.UserHeaderFilter;

/** 
 * ClassName:ConsumerConf <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2017年3月16日 上午11:46:43 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
@Configuration
public class ConsumerConf {
    @Bean
    public FilterRegistrationBean filterRegistrationBeanBySession() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        UserHeaderFilter session = new UserHeaderFilter();
        registrationBean.setFilter(session);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
  