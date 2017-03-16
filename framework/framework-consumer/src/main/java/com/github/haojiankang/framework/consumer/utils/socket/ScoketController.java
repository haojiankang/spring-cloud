/** 
 * Project Name:basic-core 
 * File Name:ScoketController.java 
 * Package Name:com.ghit.web.socket 
 * Date:2016年11月21日上午9:56:16  
*/

package com.github.haojiankang.framework.consumer.utils.socket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName:ScoketController <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年11月21日 上午9:56:16 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */

@Controller
@RequestMapping("/web/scoket/client")
public class ScoketController {
    @RequestMapping("publish")
    @ResponseBody
    public String publish(String channel, String message) {
        News.publish(channel, message);
        return "publish message success！";
    }
}
