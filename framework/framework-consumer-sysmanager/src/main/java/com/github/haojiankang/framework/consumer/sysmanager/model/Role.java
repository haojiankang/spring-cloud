/** 
 * Project Name:framework-consumer-sysmanager 
 * File Name:Role.java 
 * Package Name:com.ghit.framework.consumer.sysmanager.model 
 * Date:2017年3月15日下午1:50:04  
*/  
  
package com.ghit.framework.consumer.sysmanager.model;

import java.util.List;

import com.ghit.framework.consumer.utils.model.BaseModel;

/** 
 * ClassName:Role <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2017年3月15日 下午1:50:04 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
public class Role extends BaseModel{
    private static final long serialVersionUID = -3022168779714627416L;
    private String roleName;

    private String description;

    private String roleCode;
    private List<Jurisdiction> jurisdictions;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public List<Jurisdiction> getJurisdictions() {
        return jurisdictions;
    }

    public void setJurisdictions(List<Jurisdiction> jurisdictions) {
        this.jurisdictions = jurisdictions;
    }



}
  