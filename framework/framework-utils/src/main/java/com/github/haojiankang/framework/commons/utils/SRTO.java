/** 
 * Project Name:framework-utils 
 * File Name:SRTO.java 
 * Package Name:com.ghit.framework.commons.utils 
 * Date:2017年3月16日上午10:25:59  
*/

package com.github.haojiankang.framework.commons.utils;

/**
 * ClassName:SRTO <br/>
 * Function: 服务入参传输对象. <br/>
 * Date: 2017年3月16日 上午10:25:59 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class SRTO<O, T> {

    private O o;
    private T t;

    public static <O1, T1> SRTO<O1, T1> structure(O1 o1, T1 t1) {
        SRTO<O1, T1> srto = new SRTO<>();
        srto.setO(o1);
        srto.setT(t1);
        return srto;
    }

    public O getO() {
        return o;
    }

    public void setO(O o) {
        this.o = o;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

}
