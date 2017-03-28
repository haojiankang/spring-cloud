package com.haojiankang.framework.provider.sysmanager;
/** 
 * Project Name:cloud-simple-service 
 * File Name:Application.java 
 * Package Name:cloud.simple.service 
 * Date:2017年3月2日下午7:55:41  
*/


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:Application <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年3月2日 下午7:55:41 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages={"com.github.haojiankang.framework"})
public class ProviderSysmanagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderSysmanagerApplication.class, args);
    }
}
