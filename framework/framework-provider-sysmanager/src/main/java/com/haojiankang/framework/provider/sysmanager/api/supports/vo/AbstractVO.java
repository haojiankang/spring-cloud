/** 
 * Project Name:ghit-basic-api 
 * File Name:AbstractVo.java 
 * Package Name:com.ghit.basic.api.supports.vo 
 * Date:2017年2月27日下午2:32:02  
*/

package com.haojiankang.framework.provider.sysmanager.api.supports.vo;

import java.io.Serializable;

/**
 * ClassName:AbstractVo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年2月27日 下午2:32:02 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public interface AbstractVO<PK extends Serializable> extends Serializable {
    public abstract PK getId();

    public abstract void setId(PK pk);

}
