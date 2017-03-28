/** 
 * Project Name:framework-consumer-sysmanager 
 * File Name:IndexIndexController.java 
 * Package Name:com.ghit.framework.consumer.sysmanager.controller 
 * Date:2017年3月16日上午9:42:04  
*/

package com.haojiankang.framework.consumer.sysmanager.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.haojiankang.framework.commons.utils.SSTO;
import com.haojiankang.framework.commons.utils.bean.BeanUtils;
import com.haojiankang.framework.consumer.sysmanager.feignclient.IndexFeignClient;
import com.haojiankang.framework.consumer.utils.CS;

/**
 * ClassName:IndexIndexController <br/>
 * Function: 对应js模块：index.index. <br/>
 * Date: 2017年3月16日 上午9:42:04 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@RestController
@RequestMapping("/index")
public class IndexIndexController {
    @Autowired
    private IndexFeignClient indexClient;

    @RequestMapping(value = "loadResource")
    @ResponseBody
    public SSTO<Map<String, Object>> loadResource(String resourceName) {
        SSTO<Map<String, Object>> ssto = indexClient.loadResource(resourceName, BeanUtils.encodeHeader(CS.currentUser()));
        return ssto;
    }
}
