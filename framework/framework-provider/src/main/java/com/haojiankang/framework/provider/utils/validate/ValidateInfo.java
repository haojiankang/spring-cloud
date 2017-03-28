/** 
 * Project Name:EHealthData 
 * File Name:ValidateInfo.java 
 * Package Name:com.ghit.common.validate 
 * Date:2016年6月22日下午5:05:49  
*/  
  
package com.haojiankang.framework.provider.utils.validate;

import java.util.List;

/** 
 * ClassName:ValidateInfo <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2016年6月22日 下午5:05:49 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public class ValidateInfo {
    private String name;
    private List<Info> infos;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Info> getInfos() {
        return infos;
    }

    public void setInfos(List<Info> infos) {
        this.infos = infos;
    }

    
}
