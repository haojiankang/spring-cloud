/** 
 * Project Name:framework-consumer 
 * File Name:ConsumerConf.java 
 * Package Name:com.ghit.framework.consumer.conf 
 * Date:2017年3月14日下午5:13:57  
*/

package com.haojiankang.framework.consumer.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import com.haojiankang.framework.commons.utils.spring.convert.FeignRemoteCallMessageConvert;
import com.haojiankang.framework.consumer.utils.CS;
import com.haojiankang.framework.consumer.utils.security.filter.SecurityManagerFilter;
import com.haojiankang.framework.consumer.utils.security.filter.SessionManagerFilter;

import feign.Feign;
import feign.Logger;
import feign.Request;

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
        CS.setSecurityConfig(conf);
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

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }

    @Bean
    public Logger.Level feignLogger() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options options() {
        return new Request.Options();
    }

    @Bean
    public FeignRemoteCallMessageConvert getFeignRemoteCallMessageConvert() {
        return new FeignRemoteCallMessageConvert();
    }

}
