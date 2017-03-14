/** 
 * Project Name:Telehealth 
 * File Name:FlowStateError.java 
 * Package Name:com.ghit.wf.exception 
 * Date:2016年10月8日下午2:45:30  
*/  
  
package com.ghit.framework.provider.sysmanager.api.supports.wf;  
/** 
 * ClassName:FlowStateError <br> 
 * Function: TODO ADD FUNCTION. <br> 
 * Date:     2016年10月8日 下午2:45:30 <br> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
public class FlowStateError extends RuntimeException {

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 1L;
    public FlowStateError() {
        super();
    }

    public FlowStateError(String message) {
        super(message);
    }

    public FlowStateError(String message, Throwable cause) {
        super(message, cause);
    }
    public FlowStateError(Throwable cause) {
        super(cause);
    }

    protected FlowStateError(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
  