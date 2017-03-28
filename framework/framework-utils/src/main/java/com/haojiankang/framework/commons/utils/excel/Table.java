/** 
 * Project Name:ghit-basic-core 
 * File Name:Table.java 
 * Package Name:com.ghit.common.util.excel 
 * Date:2016年12月13日上午11:28:53  
*/  
  
package com.haojiankang.framework.commons.utils.excel;

import java.util.List;

/** 
 * ClassName:Table <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2016年12月13日 上午11:28:53 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public class Table implements ITable {
    private List<Row> rows;
    @Override
    public List<? extends IRow> rows() {
        return rows;
    }
    public List<Row> getRows() {
        return rows;
    }
    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
    

}
  