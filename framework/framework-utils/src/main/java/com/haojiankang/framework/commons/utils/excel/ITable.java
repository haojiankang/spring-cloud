/** 
 * Project Name:ghit-basic-core 
 * File Name:Table.java 
 * Package Name:com.ghit.common.util.excel 
 * Date:2016年12月13日上午10:31:45  
*/  
  
package com.haojiankang.framework.commons.utils.excel;

import java.util.List;

/** 
 * ClassName:Table <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2016年12月13日 上午10:31:45 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public interface ITable {
    List<? extends IRow> rows();
}
  