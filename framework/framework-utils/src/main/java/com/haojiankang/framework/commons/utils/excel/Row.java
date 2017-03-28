/** 
 * Project Name:ghit-basic-core 
 * File Name:Row.java 
 * Package Name:com.ghit.common.util.excel 
 * Date:2016年12月13日上午11:28:09  
*/  
  
package com.haojiankang.framework.commons.utils.excel;

import java.util.List;

/** 
 * ClassName:Row <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2016年12月13日 上午11:28:09 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public class Row implements IRow {
    private List<Cell> cells;
    private List<Table> tables;
    @Override
    public List<Cell> cells() {
        return cells;
    }

    @Override
    public List<Table> tables() {
        return tables;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }
    
}
  