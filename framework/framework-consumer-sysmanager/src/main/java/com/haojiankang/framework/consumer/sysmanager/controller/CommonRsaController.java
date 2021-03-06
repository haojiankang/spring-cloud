/** 
 * Project Name:framework-consumer-sysmanager 
 * File Name:CommonRsaController.java 
 * Package Name:com.ghit.framework.consumer.sysmanager.controller 
 * Date:2017年3月15日上午10:53:28  
*/

package com.haojiankang.framework.consumer.sysmanager.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haojiankang.framework.commons.utils.SSTO;
import com.haojiankang.framework.consumer.sysmanager.feignclient.IndexFeignClient;

/**
 * ClassName:CommonRsaController <br/>
 * Function: 对应js模块：common.rsa. <br/>
 * Date: 2017年3月15日 上午10:53:28 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@RestController
public class CommonRsaController {
    @Autowired
    private IndexFeignClient indexClient;
    @RequestMapping("/common/session/rsa")
    public SSTO<Map<String, String>> rsaKey() {
        return indexClient.config();
    }


}
