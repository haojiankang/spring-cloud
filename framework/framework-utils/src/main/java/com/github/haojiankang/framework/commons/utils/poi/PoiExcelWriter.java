package com.github.haojiankang.framework.commons.utils.poi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.haojiankang.framework.commons.utils.excel.WorkbookType;

/**
 * ClassName: PoiExcelWriter 
 * Function: 将字符串数组转化成Excel对象
 * date: 2016年7月5日 下午1:52:55 
 *
 * @author admin
 * @version 
 * @since JDK 1.7
 */
public class PoiExcelWriter {
    private Log log = LogFactory.getLog(PoiExcelWriter.class);

	public enum CELLTYPE {TEXT,NUMBER,DATE};

    private Workbook workbook = null;
    private WorkbookType booktype = WorkbookType.XLS;

    public PoiExcelWriter(WorkbookType booktype) {
		super();
		this.booktype = booktype;
	}

	/**
     * 将传入的字符串数组和Excel输入流合并到Excel对象
     * @param records 传入的数据
     * @param is 模板文件输入流
     * @param beginrow 起始行，从1开始编号
     * @param begincol 起始列，从1开始编号
     * @param preserveNodes 是否保留宏对象
     */
    public void writeDataToExcel(String[][] records, InputStream is, int beginrow, int begincol, boolean preserveNodes) {
    	this.writeDataToExcel(records, null, is, beginrow, begincol, preserveNodes);
    }

    /**
     * 将传入的字符串数组和Excel输入流合并到Excel对象
     * @param records 传入的数据
     * @param types 字段类型：CELLTYPE.TEXT,CELLTYPE.NUMBER,CELLTYPE.DATE
     * @param is 模板文件输入流
     * @param beginrow 起始行，从1开始编号
     * @param begincol 起始列，从1开始编号
     * @param preserveNodes 是否保留宏对象
     */
    public void writeDataToExcel(String[][] records, CELLTYPE[] types, InputStream is, int beginrow, int begincol, boolean preserveNodes) {

        int rownumber = beginrow - 1;//Excel表格内部索引从0开始编号
        int colnumber = begincol - 1;//Excel表格内部索引从0开始编号
        if(rownumber < 0) rownumber = 0;
        if(colnumber < 0) colnumber = 0;

        if (workbook == null && is != null) {
	        try {
				this.workbook = this.booktype == WorkbookType.XLSX ? new XSSFWorkbook(is)
						: new HSSFWorkbook(new POIFSFileSystem(is), preserveNodes);
	         } catch (Exception e) {}
        }
        if (workbook == null) {
        	this.workbook = this.booktype == WorkbookType.XLSX ? new XSSFWorkbook() : new HSSFWorkbook();
        }

        Sheet sheet = null;
        try {
        	sheet = this.workbook.getSheetAt(this.workbook.getActiveSheetIndex());
        } catch (Exception e) {}

        Row row = null;
        Cell cell = null;
        String[] datas = null;

        try {
            if (sheet == null) sheet = this.workbook.createSheet();

            if (records != null) {
                for (int i = 0; i < records.length; i++){
                    datas = (String[])records[i];

                    row = sheet.getRow(rownumber);
                    if (row == null) row = sheet.createRow(rownumber);

                    for (int j = 0; j < datas.length; j++) {
                        cell = row.getCell(colnumber + j);
                        if (cell == null) cell = row.createCell(colnumber + j);
                        if (types != null && types.length > j)
                        	setCellValue(cell, datas[j], types[j]);
                        else
                        	setCellValue(cell, datas[j], null);
                    }
                    rownumber++;
                }
            }
        } catch(Exception e){
        	log.error("", e);
        }
    }

    /**
     * 设置表格值
     * @param cell 表格
     * @param value 待设置的值
     * @param type 值类型
     */
    private void setCellValue(Cell cell, String value, CELLTYPE type) {
    	if (cell == null) return;
    	if (value == null) value = "";
        if (type == null) type = CELLTYPE.TEXT;
       	switch (type) {
       	case NUMBER :
       		if(this.getNumberType(value) < 0) {
				cell.setCellValue(this.booktype==WorkbookType.XLSX ? new XSSFRichTextString(value)
						: new HSSFRichTextString(value));
            } else if(this.getNumberType(value) > 0) {
                cell.setCellValue(Double.parseDouble(value));
            } else {
                cell.setCellValue(Long.parseLong(value));
            }
   			break;
       	case DATE :
       		Date dt = parseDate(value);
    		if (dt != null) {
    			cell.setCellValue(dt);
    		} else {
    			cell.setCellValue(this.booktype==WorkbookType.XLSX ? new XSSFRichTextString(value)
						: new HSSFRichTextString(value));
    		}
    		break;
		default:
			cell.setCellValue(this.booktype==WorkbookType.XLSX ? new XSSFRichTextString(value)
					: new HSSFRichTextString(value));
			break;
       	}
    }

    /**
     * 将传入的字符串数组和Excel文件合并到Excel对象
     * @param records 字符串数组
     * @param filename Excel文件
     * @param beginrow 起始行，从1开始编号
     * @param begincol 起始列，从1开始编号
     */
    public void writeDataToExcel(String[][] records, String filename, int beginrow, int begincol) {
        FileInputStream fis = null;
        try {
            if(filename != null && filename.trim().length() > 0)
                fis = new FileInputStream(filename.trim());
        } catch(Exception e){}
        this.writeDataToExcel(records, fis, beginrow, begincol, false);
        try {
            if(fis != null) fis.close();
        } catch(IOException e){}
    }

    /**
     * 将传入的字符串数组转化到Excel对象
     * @param records 字符串数组
     */
    public void writeDataToExcel(String[][] records) {
        this.writeDataToExcel(records, null, 1, 1, false);
    }

    /**
     * 将Excel对象写入输出流，当输入流和输出流指向同一源时，输出流必须在先执行convertRecordListToExcel函数之后再初始化
     * @param os 输出流
     */
    public void writeExcelToStream(OutputStream os) {
        if(this.workbook == null || os == null) return;
        try {
            this.workbook.write(os);
        } catch(Exception e) {
        	log.error("", e);
        }
    }

    /**
     * 将Excel内容写入文件
     * @param filename 输出文件
     */
    public void writeExcelToFile(String filename) {
        if(this.workbook == null || filename == null || filename.trim().length() < 1) return;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename.trim());
            this.workbook.write(fos);
            fos.flush();
        } catch(Exception e) {
        	log.error("", e);
        } finally {
            try {
                if (fos != null) fos.close();
            } catch(IOException f) {}
        }
    }

    /**
     * getWorkbook:获取当前工作簿. 
     *
     * @return
     * @since JDK 1.7
     */
    public Workbook getWorkbook() {
        return workbook;
    }

    /**
     * 判断字符串是否为数字
     * @param num 待判定的字符串
     * @return 整数返回0，小数返回1，不是数字返回-1
     */
    private int getNumberType(String num){
        if(num == null) return -1;
        if(num.trim().length() < 1) return -1;
        if(num.matches("0+[1-9]+")) return -1;//可能是代码01，008等
        if(num.matches("[+-]?[0-9]+[.]?")) return 0;
        if(num.matches("[+-]?[0-9]+[.][0-9]+")) return 1;
        return -1;
    }

    /**
     * 解析日期字符串
     * @param obj 传入字符串
     * @return 返回日期
     */
    private Date parseDate(Object obj) {
		if (obj == null) return null;
		Date dtReturn = null;
		try {
			dtReturn = Date.class.cast(obj);
			if (dtReturn != null) return dtReturn;
		} catch (Exception e) {}
		SimpleDateFormat sdf = null;
		String str = obj.toString().trim();
		if (str.length() < 1) {return null;}
    	String[][] patterns = {
    			{"[1-2]\\d{3}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2]\\d|3[0-1]) ([0-1]\\d|2[0-3]):[0-5]?\\d:[0-5]?\\d", "yyyy-MM-dd HH:mm:ss"},
    			{"[1-2]\\d{3}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2]\\d|3[0-1])", "yyyy-MM-dd"},
    			{"[1-2]\\d{3}\\/(0?[1-9]|1[0-2])\\/(0?[1-9]|[1-2]\\d|3[0-1])", "yyyy/MM/dd"},
    			{"(0?[1-9]|1[0-2])\\/(0?[1-9]|[1-2]\\d|3[0-1])\\/\\d{2}", "MM/dd/yy"},
    			{"[1-2]\\d{3}\\d[0-1]\\d[0-3]\\d([0-1]\\d|2[0-3])[0-5]\\d[0-5]\\d", "yyyyMMddHHmmss"},
    			{"([0-1]\\d|2[0-3]):[0-5]?\\d:[0-5]?\\d", "HH:mm:ss"},
    			{"[1-2]\\d{3}\\d[0-1]\\d[0-3]\\d", "yyyyMMdd"},
    			{"\\d{2}\\d[0-1]\\d[0-3]\\d", "yyMMdd"},
    			{"([0-1]\\d|2[0-3])[0-5]\\d[0-5]\\d", "HHmmss"}};
		for (int i = 0; i < patterns.length; i++) {
			if (str.matches(patterns[i][0])) {
				sdf = new SimpleDateFormat(patterns[i][1]);
				try {
					dtReturn = sdf.parse(str);
					break;
				} catch (Exception e) {}
			}
		}
		return dtReturn;
	}
}