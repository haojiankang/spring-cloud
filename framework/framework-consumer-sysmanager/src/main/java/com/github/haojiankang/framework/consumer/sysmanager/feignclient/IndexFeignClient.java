/** 
 * Project Name:framework-consumer-sysmanager 
 * File Name:IndexFeignClient.java 
 * Package Name:com.ghit.framework.consumer.sysmanager.feignclient 
 * Date:2017年3月15日上午11:06:29  
*/

package com.ghit.framework.consumer.sysmanager.feignclient;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ghit.framework.commons.utils.SSTO;
import com.ghit.framework.commons.utils.security.model.SecurityUser;
import com.ghit.framework.consumer.sysmanager.model.User;

/**
 * ClassName:IndexFeignClient <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年3月15日 上午11:06:29 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@FeignClient(name = "${provider.sysmanager.name}", path = "/index")
public interface IndexFeignClient {
    @RequestMapping("config")
    public SSTO<Map<String, String>> config();

    @RequestMapping(value = "resetPassword", method = RequestMethod.POST)
    public SSTO<String> resetPassword(User user);

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public SSTO<SecurityUser> login(User user);
    
    @RequestMapping(value = "loadResource")
    public SSTO<Map<String,Object>> loadResource(@RequestParam("resourceName") String resourceName,@RequestHeader(name="userinfo") String user);
}
