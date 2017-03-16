/** 
 * Project Name:EHealthData 
 * File Name:ValidateEnum.java 
 * Package Name:com.ghit.common.validate 
 * Date:2016年6月28日上午11:20:37  
*/

package com.github.haojiankang.framework.provider.utils.validate;

/**
 * ClassName:ValidateEnum <br/>
 * Function: 内置校验器默认枚举. <br/>
 * Date: 2016年6月28日 上午11:20:37 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.6
 * @see
 */
public enum ValidateEnum {
    /**
     * 必填校验
     */
    Required;
    private String message;
    private String validate;

    ValidateEnum() {
        if(this.name().equals("Required")){
            message="{name}为必填项.";
            validate="$notEmpty(#this)";
        }
    }

    public String getMessage(String name) {
        return message.replace("{name}", name);
    }
    public String getValidate(){
        return validate;
    }

}
