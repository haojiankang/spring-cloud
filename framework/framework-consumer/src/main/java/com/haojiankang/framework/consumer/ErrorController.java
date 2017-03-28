/** 
 * Project Name:framework-consumer 
 * File Name:ErrorController.java 
 * Package Name:com.ghit.framework.consumer 
 * Date:2017年3月14日下午10:14:24  
*/  
  
package com.haojiankang.framework.consumer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/** 
 * ClassName:ErrorController <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2017年3月14日 下午10:14:24 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
@Controller
public class ErrorController {
    @RequestMapping("/error/info")
    public String error(Model model, @RequestAttribute("message") String message){
        model.addAttribute("message", message);
        return "error";
    }
}
  