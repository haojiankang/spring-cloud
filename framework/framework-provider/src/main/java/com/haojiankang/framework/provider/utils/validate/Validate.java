/** 
 * Project Name:EHealthData 
 * File Name:Validate.java 
 * Package Name:com.ghit.common.validate 
 * Date:2016年6月22日下午3:02:52  
*/  
  
package com.haojiankang.framework.provider.utils.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * ClassName:Validate <br/> 
 * Function: 验证注解，用于aop拦截 <br/> 
 * Date:     2016年6月22日 下午3:02:52 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
@Target({ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented 
public @interface Validate {
    /**
     * 
     * indexs:校验的参数下标.
     * <p>下标从0开始,默认为0</p> 
     * @author ren7wei
     * @return
     * @since JDK 1.7
     */
    int[] indexs() default {0};
    /**
     * 
     * mapping:校验信息映射文件名称. 
     * <p>默认为空，使用类全名</p>
     * @author ren7wei
     * @return
     * @since JDK 1.7
     */
    String[] mapping() default {""};
    /**
     * 
     * processMode:校验不通过后的处理模式
     *
     * @author ren7wei
     * @return
     * @since JDK 1.7
     */
    ProcessMode  processMode() default ProcessMode.Exception;

}
  