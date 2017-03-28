/** 
 * Project Name:ghit-basic-core 
 * File Name:Row.java 
 * Package Name:com.ghit.common.util.excel 
 * Date:2016年12月13日上午10:31:13  
*/  
  
package com.haojiankang.framework.commons.utils.excel;

import java.util.List;

/** 
 * ClassName:Row <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2016年12月13日 上午10:31:13 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public interface IRow {
    /**
     * <p>
     * 返回行内的单元格集合
     * <p>
     */
    List<? extends ICell> cells();

    List<? extends ITable> tables();
}
  