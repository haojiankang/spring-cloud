/** 
 * Project Name:ghit-basic-provider 
 * File Name:Info.java 
 * Package Name:com.ghit.basic.supports.validate 
 * Date:2017年2月25日上午10:27:05  
*/  
  
package com.github.haojiankang.framework.provider.utils.validate;

import java.util.List;

/** 
 * ClassName:Info <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2017年2月25日 上午10:27:05 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
public class Info {
    //字段属性名
    private String field;
    //验证表达式
    private String validate;
    //提示消息，优先级低于i118n
    private String message;
    //提示消息
    private String i118n;
    //验证类型对应ValidateType枚举
    private String type;
    //字段对应中文名称，用于提示。
    private String name;
    //表达式预期值，默认为false，如果表达式值满足预期值则该条验证为不通过
    private boolean expect;
    //验证通过后需要验证的项目
    private List<Info> infos;
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public String getValidate() {
        return validate;
    }
    public void setValidate(String validate) {
        this.validate = validate;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getI118n() {
        return i118n;
    }
    public void setI118n(String i118n) {
        this.i118n = i118n;
    }
    public boolean getExpect() {
        return expect;
    }
    public void setExpect(boolean expect) {
        this.expect = expect;
    }
    public List<Info> getInfos() {
        return infos;
    }
    public void setInfos(List<Info> infos) {
        this.infos = infos;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
 


}
  