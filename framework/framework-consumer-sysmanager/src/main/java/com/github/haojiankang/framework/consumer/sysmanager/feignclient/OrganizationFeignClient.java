/** 
 * Project Name:framework-consumer-sysmanager 
 * File Name:OrganizationFeignClient.java 
 * Package Name:com.github.haojiankang.framework.consumer.sysmanager.feignclient 
 * Date:2017年3月17日下午4:22:35  
*/  
  
package com.github.haojiankang.framework.consumer.sysmanager.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;

/** 
 * ClassName:OrganizationFeignClient <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2017年3月17日 下午4:22:35 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */

@FeignClient(name = "${provider.sysmanager.name}", path = "/organization")
public interface OrganizationFeignClient {

}
  