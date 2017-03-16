/** 
 * Project Name:framework-utils 
 * File Name:SSTO.java 
 * Package Name:com.ghit.framework.commons.utils 
 * Date:2017年3月15日下午2:21:39  
*/

package com.ghit.framework.commons.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * ClassName:SSTO <br/>
 * Function: 跨服务通讯标准对象. <br/>
 * Date: 2017年3月15日 下午2:21:39 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */

@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
public class SSTO<O> {
    private boolean state;
    private String message;
    private O data;

    public static <I> SSTO<I> structure(boolean state, String message, I data) {
        SSTO<I> ssto = new SSTO<>();
        ssto.setData(data);
        ssto.setState(state);
        ssto.setMessage(message);
        return ssto;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public O getData() {
        return data;
    }

    public void setData(O data) {
        this.data = data;
    }

}
