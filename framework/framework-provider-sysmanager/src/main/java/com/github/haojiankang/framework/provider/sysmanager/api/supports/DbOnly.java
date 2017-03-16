/** 
 * Project Name:EHealthData 
 * File Name:Only.java 
 * Package Name:com.ghit.common.mvc 
 * Date:2016年7月12日上午10:59:50  
*/

package com.ghit.framework.provider.sysmanager.api.supports;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName:Only <br>
 * Function: 用于标记entity的属性是否唯一 <br>
 * Date: 2016年7月12日 上午10:59:50 <br>
 * 
 * @author ren7wei
 * @version 1.0
 * @since JDK 1.8
 */

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DbOnly {
    String name() default "";

    String group() default "";
}
