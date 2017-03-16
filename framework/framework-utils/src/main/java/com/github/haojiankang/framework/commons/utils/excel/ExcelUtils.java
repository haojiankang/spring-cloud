/** 
 * Project Name:ghit-basic-core 
 * File Name:ExcelUtils.java 
 * Package Name:com.ghit.common.util 
 * Date:2016年12月13日上午10:26:11  
*/

package com.github.haojiankang.framework.commons.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * ClassName:ExcelUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年12月13日 上午10:26:11 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class ExcelUtils {

    static final Log LOG = LogFactory.getLog(Log.class);

    /**
     * <p>
     * 发送Excel流
     * <p>
     * 
     * @param list
     *            数据行集合
     * @param fileName
     *            文件名字
     * @throws Exception
     */
    public static void sendExcel(List<? extends IRow> list, String fileName, OutputStream os)
            throws Exception {
        Workbook workbook = null;
        try {
            workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet(fileName);
            List<CellRangeAddress> ranges = new ArrayList<>();
            appendRow(sheet, 0, 0, list, ranges);
            for (CellRangeAddress range : ranges) {
                sheet.addMergedRegion(range);
            }
            workbook.write(os);
        } finally {
            if (workbook != null)
                workbook.close();
            os.flush();
            os.close();
        }

    }

    /**
     * <p>
     * 发送Excel流
     * <p>
     * 
     * @param list
     *            数据行集合
     * @param fileName
     *            文件名字
     * @throws Exception
     */
    public static void sendExcel(Map<String, List<IRow>> data, String fileName, OutputStream os)
            throws Exception {
        Workbook workbook = new HSSFWorkbook();
        try {
            data.forEach((sheetName, list) -> {
                try {
                    Sheet sheet = workbook.createSheet(sheetName);
                    List<CellRangeAddress> ranges = new ArrayList<>();
                    appendRow(sheet, 0, 0, list, ranges);
                    for (CellRangeAddress range : ranges) {
                        sheet.addMergedRegion(range);
                    }
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            });
            workbook.write(os);
        } finally {
            if (workbook != null)
                workbook.close();
            os.flush();
            os.close();
        }

    }

    public static void writeExcel(List<? extends IRow> list, File file) throws Exception {
        Workbook workbook = null;
        WorkbookType booktype = WorkbookType.XLS;
        if (file.getName().toLowerCase().endsWith(".xlsx")) {
            booktype = WorkbookType.XLSX;
        }
        if (booktype == WorkbookType.XLSX) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }

        Sheet sheet = workbook.createSheet(file.getName());
        List<CellRangeAddress> ranges = new ArrayList<>();
        try {
            appendRow(sheet, 0, 0, list, ranges);
            for (CellRangeAddress range : ranges) {
                sheet.addMergedRegion(range);
            }
            workbook.write(new FileOutputStream(file));
        } finally {
            workbook.close();
        }
    }

    public static int appendRow(Sheet sheet, int start_x, int start_y, List<? extends IRow> list,
            List<CellRangeAddress> merges) throws Exception {
        Map<CellStyleFunction, CellStyle> map = new HashMap<>();
        int i = start_x;
        if (list != null && !list.isEmpty()) {
            for (IRow row : list) {
                int k = start_y;
                if (sheet.getRow(i) == null) {
                    sheet.createRow(i);
                }
                for (ICell cell : row.cells()) {
                    if (!cell.hide()) {
                        while (sheet.getRow(i).getCell(k) != null) {
                            k++;
                        }
                        sheet.getRow(i).createCell(k);
                        if (cell.colspan() > 1 || cell.rowspan() > 1) {
                            for (int p = 0; p < cell.rowspan(); p++) {
                                if (sheet.getRow(i + p) == null) {
                                    sheet.createRow(i + p);
                                }
                                for (int j = 0; j < cell.colspan(); j++) {
                                    sheet.getRow(i + p).createCell(k + j);
                                }
                            }
                            merges.add(new CellRangeAddress(i, i + cell.rowspan() - 1, k, k + cell.colspan() - 1));
                        }
                        org.apache.poi.ss.usermodel.Cell tcell = sheet.getRow(i).getCell(k);
                        if (cell.cellStyle() != null) {
                            CellStyle cellStyle = map.get(cell.cellStyle());
                            if (cellStyle == null) {
                                cellStyle = cell.cellStyle().createStyle(sheet);
                                map.put(cell.cellStyle(), cellStyle);
                            }
                            tcell.setCellStyle(cellStyle);
                        }
                        tcell.setCellValue(cell.content());
                        k += cell.colspan();
                    }
                }
                i++;
                if (row.tables() != null && !row.tables().isEmpty())
                    for (ITable table : row.tables()) {
                        if (table != null)
                            i = appendRow(sheet, i, 0, table.rows(), merges);
                    }

            }
        }
        return i;
    }

    public static boolean inMerge(int c, int r, int[] ins, int mergeMode) {
        boolean bool = false;
        switch (mergeMode) {
        case 0:
            bool = c >= ins[0] && c <= ins[2] && r >= ins[1] && r <= ins[3];
            break;
        case 1:
            bool = c >= ins[0] && c <= ins[2] && r == ins[1] && r <= ins[3];
            break;
        case 2:
            bool = c == ins[0] && c <= ins[2] && r >= ins[1] && r <= ins[3];
            break;

        }
        return bool;
    }

    public static int[] getMerge(int c, int r, List<int[]> merges, int mergeMode) {
        int[] ins = null;
        for (int[] item : merges) {
            if (inMerge(c, r, item, mergeMode)) {
                ins = item;
                break;
            }
        }
        return ins;
    }

    /**
     * 
     * 读取Excel
     * 
     * @param file
     *            读取的文件
     * @param mergeMode
     *            合并单元格读取模式 -1(不处理)，0(横纵合并都读取)，1（横向读取），2(纵向读取)
     * @throws IOException
     * @throws InvalidFormatException
     * 
     * @see
     */
    public static List<String> readExcel(File file, int mergeMode) throws IOException, InvalidFormatException {
        List<String> list = new ArrayList<String>();
        Workbook workbook = null;
        try {
            WorkbookType booktype = WorkbookType.XLS;
            if (file.getName().toLowerCase().endsWith(".xlsx")) {
                booktype = WorkbookType.XLSX;
            }
            if (booktype == WorkbookType.XLSX) {
                workbook = new XSSFWorkbook(file);
            } else {
                workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(file)));
            }
            Sheet sheet = workbook.getSheetAt(0);

            List<int[]> merges = new ArrayList<int[]>();
            int sheetmergerCount = sheet.getNumMergedRegions();
            // 遍历合并单元格
            for (int i = 0; i < sheetmergerCount; i++) {
                merges.add(mergedConvert(sheet.getMergedRegion(i)));
            }

            StringBuilder line = new StringBuilder("");
            for (int r = 0; r < sheet.getLastRowNum(); r++) {
                line.setLength(0);
                for (int c = 0; c < sheet.getRow(r).getLastCellNum(); c++) {
                    try {
                        int[] merge = getMerge(c, r, merges, mergeMode);
                        if (merge == null) {
                            line.append(sheet.getRow(r).getCell(c).getStringCellValue());
                        } else {
                            line.append(sheet.getRow(merge[1]).getCell(merge[0]).getStringCellValue());
                        }
                        line.append("∴");
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                }
                line.append("~");
                list.add(line.toString());
            }
        } finally {
            workbook.close();
        }
        return list;
    }

    private static int[] mergedConvert(CellRangeAddress mergedRegion) {
        int[] ins = new int[] { mergedRegion.getFirstColumn(), mergedRegion.getFirstRow(), mergedRegion.getLastColumn(),
                mergedRegion.getLastRow() };
        return ins;
    }

}
