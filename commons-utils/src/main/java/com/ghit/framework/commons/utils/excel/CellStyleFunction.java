/** 
 * Project Name:ghit-basic-core 
 * File Name:CellStyleFunction.java 
 * Package Name:com.ghit.common.util.excel 
 * Date:2016年12月13日下午1:31:53  
*/

package com.ghit.framework.commons.utils.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * ClassName:CellStyleFunction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年12月13日 下午1:31:53 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@FunctionalInterface
public interface CellStyleFunction {
    public CellStyle createStyle(Sheet sheet);
}
