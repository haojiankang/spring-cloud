/** 
 * Project Name:framework-consumer-sysmanager 
 * File Name:LoginHtmlController.java 
 * Package Name:com.ghit.framework.consumer.sysmanager.controller 
 * Date:2017年3月15日上午10:46:27  
*/

package com.ghit.framework.consumer.sysmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ghit.framework.commons.utils.SSTO;
import com.ghit.framework.commons.utils.security.model.SecurityUser;
import com.ghit.framework.consumer.sysmanager.feignclient.IndexFeignClient;
import com.ghit.framework.consumer.sysmanager.model.User;
import com.ghit.framework.consumer.utils.CS;

/**
 * ClassName:LoginHtmlController <br/>
 * Function: 对应js模块：index.login <br/>
 * Date: 2017年3月15日 上午10:46:27 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@RestController
@RequestMapping("/index")
public class IndexLoginController {
    @Autowired
    private IndexFeignClient indexClient;

    @RequestMapping(value = "login")
    @ResponseBody
    public SSTO<SecurityUser> login(User user) {
        SSTO<SecurityUser> ssto = indexClient.login(user);
        if (ssto.getState()) {
            CS.currentUser(ssto.getData());
        } else {
            if (ssto.getMessage() == null) {
                ssto.setMessage("帐号或者密码不正确");
            }
        }
        return ssto;
    }

    @RequestMapping(value = "resetPassword")
    @ResponseBody
    public SSTO<String> resetPassword(User user) {
        return indexClient.resetPassword(user);
    }

}
