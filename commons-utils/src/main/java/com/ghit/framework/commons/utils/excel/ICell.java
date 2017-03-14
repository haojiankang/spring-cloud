/** 
 * Project Name:ghit-basic-core 
 * File Name:Cell.java 
 * Package Name:com.ghit.common.util.excel 
 * Date:2016年12月13日上午10:31:06  
*/

package com.ghit.framework.commons.utils.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

/**
 * ClassName:Cell <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年12月13日 上午10:31:06 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.6
 * @see
 */
public interface ICell {
    CellStyleFunction cellStyle();

    /**
     * <p>
     * 返回单元格内容
     * <p>
     */
    String content();

    /**
     * <p>
     * 隐藏单元格
     * <p>
     */
    boolean hide();

    /**
     * <p>
     * 单元格跨列
     * <p>
     */
    int colspan();

    /**
     * <p>
     * 单元格跨行
     * <p>
     */
    int rowspan();

    CellStyleFunction CENTER = sheet -> {
        CellStyle cellstyle = sheet.getWorkbook().createCellStyle();
        cellstyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellstyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        return cellstyle;
    };

    CellStyleFunction CENTER_BOLD = sheet -> {
        CellStyle cellstyle = sheet.getWorkbook().createCellStyle();
        cellstyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellstyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        cellstyle.setFont(font);
        return cellstyle;
    };

    CellStyleFunction BOLD = sheet -> {
        CellStyle cellstyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        cellstyle.setFont(font);
        return cellstyle;
    };

}
