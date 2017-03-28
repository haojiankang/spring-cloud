/** 
 * Project Name:ghit-basic-core 
 * File Name:Cell.java 
 * Package Name:com.ghit.common.util.excel 
 * Date:2016年12月13日上午11:26:44  
*/  
  
package com.haojiankang.framework.commons.utils.excel;

/** 
 * ClassName:Cell <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2016年12月13日 上午11:26:44 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public class Cell implements ICell {
    private String content;
    private boolean hide;
    private int colspan;
    private int rowspan;
    private CellStyleFunction cellstyle;
    {
        hide=false;
        colspan=1;
        rowspan=1;
    }
    @Override
    public String content() {
        return content;
    }
    @Override
    public boolean hide() {
        return hide;
    }

    @Override
    public int colspan() {
        return colspan;
    }

    @Override
    public int rowspan() {
        return rowspan;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public boolean isHide() {
        return hide;
    }
    public void setHide(boolean hide) {
        this.hide = hide;
    }
    public int getColspan() {
        return colspan;
    }
    public void setColspan(int colspan) {
        this.colspan = colspan;
    }
    public int getRowspan() {
        return rowspan;
    }
    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }
    @Override
    public CellStyleFunction cellStyle() {
        return cellstyle;
    }
    public CellStyleFunction getCellstyle() {
        return cellstyle;
    }
    public void setCellstyle(CellStyleFunction cellstyle) {
        this.cellstyle = cellstyle;
    }
}
  