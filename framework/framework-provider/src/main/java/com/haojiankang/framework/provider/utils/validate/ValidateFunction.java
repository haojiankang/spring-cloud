/** 
 * Project Name:EHealthData 
 * File Name:ValidateFunction.java 
 * Package Name:com.ghit.common.validate 
 * Date:2016年6月23日上午11:10:33  
*/

package com.haojiankang.framework.provider.utils.validate;

import java.util.List;

/**
 * ClassName:ValidateFunction <br/>
 * Function: 注册验证时的函数 <br/>
 * Date: 2016年6月23日 上午11:10:33 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.6
 * @see
 */
public class ValidateFunction {
    public boolean eq(Object arg1, Object arg2) {
        if (arg1 == arg2)
            return true;
        if (arg1 == null || arg2 == null)
            return false;
        return arg1.toString().equals(arg2.toString());
    }

    public boolean empty(Object obj) {
        if (obj == null)
            return true;
        if (obj instanceof String)
            return obj.toString().equals("");
        return false;
    }

    public boolean notEmpty(Object obj) {
        return !empty(obj);
    }

    public boolean len(Object obj, int min, int max) {
        int length = 0;
        if (obj != null)
            length = obj.toString().length();
        return length >= min && length <= max ;
    }

    public boolean in(Object obj, List<Object> args) {
        return args.stream().filter(t -> {
            return eq(obj, t);
        }).count() > 0;
    }
}
