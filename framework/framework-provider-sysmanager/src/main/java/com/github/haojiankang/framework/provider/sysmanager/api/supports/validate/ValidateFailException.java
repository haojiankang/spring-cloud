/** 
 * Project Name:EHealthData 
 * File Name:ValidateException.java 
 * Package Name:com.ghit.common.validate 
 * Date:2016年6月22日下午3:22:17  
*/  
  
package com.github.haojiankang.framework.provider.sysmanager.api.supports.validate;  
/** 
 * ClassName:ValidateException <br/> 
 * Function: 验证异常类表示验证不通过 <br/> 
 * Date:     2016年6月22日 下午3:22:17 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public class ValidateFailException extends Exception {

    /**
     * @since JDK 1.7
     */
    private static final long serialVersionUID = 1L;
    private Object validateFailMessage;
    public Object getValidateFailMessage() {
        return validateFailMessage;
    }
    public ValidateFailException setValidateFailMessage(Object validateFailMessage) {
        this.validateFailMessage = validateFailMessage;
        return this;
    }
    
}
  