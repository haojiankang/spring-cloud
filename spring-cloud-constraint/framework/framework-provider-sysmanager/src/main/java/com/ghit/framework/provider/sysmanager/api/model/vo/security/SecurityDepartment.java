package com.ghit.framework.provider.sysmanager.api.model.vo.security;
/** 
 * Project Name:EHealthData 
 * File Name:SecurityDepartment.java 
 * Package Name:com.ghit.common.security.model 
 * Date:2016年7月18日上午10:24:43  
*/ 

import java.io.Serializable;

import com.ghit.framework.provider.sysmanager.api.supports.security.model.IDepartment;

/** 
 * ClassName:SecurityDepartment <br> 
 * Function: 部门接口基本实现类. <br> 
 * Date:     2016年7月18日 上午10:24:43 <br> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
public class SecurityDepartment implements IDepartment,Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String code;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
  