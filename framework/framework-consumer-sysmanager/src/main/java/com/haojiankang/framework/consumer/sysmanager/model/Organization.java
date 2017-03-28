/** 
 * Project Name:framework-consumer-sysmanager 
 * File Name:Organization.java 
 * Package Name:com.ghit.framework.consumer.sysmanager.model 
 * Date:2017年3月15日下午1:54:00  
*/

package com.haojiankang.framework.consumer.sysmanager.model;

import com.haojiankang.framework.consumer.utils.model.BaseModel;

/**
 * ClassName:Organization <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年3月15日 下午1:54:00 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class Organization extends BaseModel {
    private static final long serialVersionUID = 697174501998443291L;
    private String code;
    private String pcode;
    private String name;
    private String slevel;
    private Integer orderNum;
    private String isUse;
    private String codeindex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getSlevel() {
        return slevel;
    }

    public void setSlevel(String slevel) {
        this.slevel = slevel;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getCodeindex() {
        return codeindex;
    }

    public void setCodeindex(String codeindex) {
        this.codeindex = codeindex;
    }

}
