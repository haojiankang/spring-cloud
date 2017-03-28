/** 
 * Project Name:framework-consumer-sysmanager 
 * File Name:SysmgrOrganization.java 
 * Package Name:com.github.haojiankang.framework.consumer.sysmanager.controller 
 * Date:2017年3月17日下午4:19:24  
*/

package com.haojiankang.framework.consumer.sysmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.haojiankang.framework.commons.utils.Page;
import com.haojiankang.framework.commons.utils.SSTO;
import com.haojiankang.framework.consumer.sysmanager.feignclient.OrganizationFeignClient;

/**
 * ClassName:SysmgrOrganization <br/>
 * Function: 对应js模块：sysmgr.organization. <br/>
 * Date: 2017年3月17日 下午4:19:24 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */

@RestController
@RequestMapping("/sysmgr/organization")
public class SysmgrOrganizationController {
    @Autowired
    OrganizationFeignClient organizationClient;

    @RequestMapping("list")
    @ResponseBody
    public SSTO<Page> list(Page page) {
        return null;
    }
}
