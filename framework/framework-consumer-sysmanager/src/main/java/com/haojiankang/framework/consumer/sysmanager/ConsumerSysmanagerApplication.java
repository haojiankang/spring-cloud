/** 
 * Project Name:framework-consumer-sysmanager 
 * File Name:ConsumerApplication.java 
 * Package Name:com.ghit.framework.consumer.sysmanager 
 * Date:2017年3月14日下午9:40:20  
*/  
  
package com.haojiankang.framework.consumer.sysmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/** 
 * ClassName:ConsumerApplication <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2017年3月14日 下午9:40:20 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */

@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients
@SpringBootApplication(scanBasePackages={"com.github.haojiankang.framework"})
public class ConsumerSysmanagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerSysmanagerApplication.class, args);
    }
}
  