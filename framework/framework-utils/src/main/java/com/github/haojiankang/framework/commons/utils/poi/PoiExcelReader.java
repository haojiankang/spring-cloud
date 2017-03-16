package com.github.haojiankang.framework.commons.utils.poi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.github.haojiankang.framework.commons.utils.excel.WorkbookType;
import com.github.haojiankang.framework.commons.utils.excel.XssfDataType;


/**
 * ClassName: PoiExcelReader2 
 * Function: 从Excel文件读取数据
 * date: 2016年7月5日 下午1:52:51 
 *
 * @author admin
 * @version 
 * @since JDK 1.7
 */
public class PoiExcelReader {
    private Log log = LogFactory.getLog(PoiExcelReader.class);

	/* 起始行，Excel表格内部索引从0开始编号 */
    private int firstrow;

    /* 截止行 */
    private int lastrow;

	/* 起始列，Excel表格内部索引从0开始编号 */
    private int firstcol;

    /* 截止列 */
    private int lastcol;

    private int rownumber;

    private String[] cells;

    private ArrayList<String[]> records;

    private WorkbookType booktype = WorkbookType.XLS;

    public PoiExcelReader(WorkbookType booktype) {
		super();
		this.booktype = booktype;
	}

	/**
     * 从Excel文件中的指定区域读取数据记录
     * @param filename 文件路径
     * @param rowfrom 数据起始行(从1开始)
     * @param columnto 数据截止列(从1开始)
     * @return 返回字符串数组
     */
    public String[][] readData(String filename, int rowfrom, int columnto) {
        FileInputStream fis = null;
        String[][] arrl = null;
        try {
            if (filename != null && filename.trim().length() > 0) {
                fis = new FileInputStream(filename.trim());
                arrl = booktype == WorkbookType.XLSX ?
                		readXSSFData(fis, rowfrom, 1, 1, columnto, -1)
                		: readHSSFData(fis, rowfrom, 1, 1, columnto);
            }
        } catch (Exception e) {
        	log.error("", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
            }
        }
        return arrl;
    }

    /**
     * 从Excel文件中的指定区域读取数据记录
     * @param filename 文件路径
     * @param rowfrom 数据起始行(从1开始)
     * @param columnto 数据截止列(A,AD)
     * @return 返回包含字符串数组的列表
     */
    public String[][] readData(String filename, int rowfrom, String columnto) {
        return readData(filename, rowfrom, column2Int(columnto));
    }

    /**
	 * 从Excel文件中的指定区域读取数据记录
	 * @param is Excel输入流
	 * @param rowfrom 数据起始行(从1开始)
	 * @param rowto 数据截止行(小于等于1表示无限制)
	 * @param columnfrom 数据起始列(A,AD)
	 * @param columnto 数据截止列(A,AD)
	 * @return 返回字符串数组
	 */
	public String[][] readData(InputStream is, int rowfrom, int rowto, String columnfrom, String columnto) {
	    return booktype == WorkbookType.XLSX ?
	    		readXSSFData(is, rowfrom, rowto, column2Int(columnfrom), column2Int(columnto), -1)
	    		: readHSSFData(is, rowfrom, rowto, column2Int(columnfrom), column2Int(columnto));
	}

	/**
     * 从Excel文件中的指定区域读取数据记录
     * @param is Excel输入流
     * @param rowfrom 数据起始行(从1开始)
     * @param rowto 数据截止行(小于等于1表示无限制)
     * @param columnfrom 数据起始列(从1开始)
     * @param columnto 数据截止列(从1开始)
     * @return 返回字符串数组
     */
    public String[][] readHSSFData(InputStream is, int rowfrom, int rowto, int columnfrom, int columnto) {

    	this.firstrow = rowfrom - 1;
        this.lastrow = rowto - 1;
        if (this.firstrow < 0) this.firstrow = 0;
        if (this.lastrow < 0) this.lastrow = 0;

        this.firstcol = columnfrom - 1;
        this.lastcol = columnto - 1;
        if (this.firstcol < 0) this.firstcol = 0;
        if (this.lastcol < 0) this.lastcol = 0;

        this.rownumber = -1;
        this.cells = new String[this.lastcol - this.firstcol + 1];
        this.records = new ArrayList<String[]>();

        try {
        	ExcelEvent ee = new ExcelEvent(this);
            ee.process(is);
        } catch (Exception e) {
        	log.error("", e);
        }
        this.records.add(this.cells);//last row

        String[][] arrayString = new String[this.records.size()][];
        for (int i = 0; i < this.records.size(); i++) {
            arrayString[i] = (String[])this.records.get(i);
            for (int j = 0; j < arrayString[i].length; j++) {
            	arrayString[i][j] = (arrayString[i][j]==null?"":arrayString[i][j].trim());
        	}
        }
        return arrayString;
    }

	/**
     * 从Excel文件中的指定区域读取数据记录
     * @param is Excel输入流
     * @param rowfrom 数据起始行(从1开始)
     * @param rowto 数据截止行(小于等于1表示无限制)
     * @param columnfrom 数据起始列(从1开始)
     * @param columnto 数据截止列(从1开始)
     * @param sheetIndex sheet索引，从0开始，小于0表示取当前sheet
     * @return 返回字符串数组
     */
    public String[][] readXSSFData(InputStream is, int rowfrom, int rowto, int columnfrom, int columnto, int sheetIndex) {

        this.firstrow = rowfrom - 1;
        this.lastrow = rowto - 1;
        if (this.firstrow < 0) this.firstrow = 0;
        if (this.lastrow < 0) this.lastrow = 0;

        this.firstcol = columnfrom - 1;
        this.lastcol = columnto - 1;
        if (this.firstcol < 0) this.firstcol = 0;
        if (this.lastcol < 0) this.lastcol = 0;

        this.rownumber = -1;
        this.cells = new String[this.lastcol - this.firstcol + 1];
        this.records = new ArrayList<String[]>();

        try {
        	XSSFSheetSaxHandler handler = new XSSFSheetSaxHandler(this, sheetIndex);
			handler.process(is);
        } catch (Exception e) {
        	log.error("", e);
        }
        this.records.add(this.cells);//last row

        String[][] arrayString = new String[this.records.size()][];
        for (int i = 0; i < this.records.size(); i++) {
            arrayString[i] = (String[])this.records.get(i);
            for (int j = 0; j < arrayString[i].length; j++) {
            	arrayString[i][j] = (arrayString[i][j]==null?"":arrayString[i][j].trim());
        	}
        }
        return arrayString;
    }

    /**
     * 对二维字符串数组进行trim处理，某行数组均为空时，排除该行
     * @param arr 待处理字符串数组
     * @return 去掉空格后的字符串数组
     */
    public String[][] trimRecordList(String[][] arr){
    	String[] t = null;
    	ArrayList<String[]> list = new ArrayList<String[]>();
    	for(int i = 0; i < arr.length; i++){
    		t = arr[i];
    		boolean allEmpty = true;
    		for(int j = 0; j < t.length; j++){
    			if(t[j].trim().length()>0){
    				allEmpty=false;
    				break;
    			}
    		}
    		if(!allEmpty){
    			list.add(t);
    		}
    	}

    	String[][] returnArr = new String[list.size()][];
    	for(int i = 0; i < list.size(); i++){
    		t = (String[]) list.get(i);
    		returnArr[i]=t;
    	}

    	return returnArr;
    }

    /**
     * 把Excel文件的列号(最大支持2位)转换成列数字
     * @param excelcolumn Excel列号：A,B,AC...
     * @return 返回列数字(从1开始)
     */
    public static int column2Int(String excelcolumn) {
        if(excelcolumn==null || excelcolumn.trim().length()<1) {
            return 1;
        }
        int intReturn = 1;
        if(excelcolumn.trim().matches("^\\d*$")) {
            try {
                intReturn = Integer.parseInt(excelcolumn.trim());
            } catch (Exception e) {}
        } else {
            char[] chrs = excelcolumn.trim().toUpperCase().toCharArray();
            if(chrs.length==1) {
                intReturn = chrs[0] - 65 + 1;
            } else {
                intReturn = (chrs[0] - 65 + 1) * 26;
                intReturn += (chrs[1] - 65 + 1);
            }
        }
        return intReturn;
    }

    /**
     * 把Excel文件的列数字转换成列号
     * @param column 列数字(从1开始)
     * @return 返回列号
     */
    public static String int2Column(int column) {
    	int col = column < 1 ? 1 : column;
    	int system = 26, ind = 0;
    	char[] digArr = new char[100];
    	while (col > 0) {
    		int mod = col % system;
    		if (mod == 0) mod = system;
    		digArr[ind++] = dig2Char(mod);
    		col = (col - 1) / 26;
    	}
    	StringBuffer sbf = new StringBuffer(ind);
    	for (int i = ind - 1; i >= 0; i--) {
    		sbf.append(digArr[i]);
    	}
    	return sbf.toString();
    }

    /**
     * 将数字按位置转asc字符
     * @param dig 1对应A，2对应B，3对应C...
     * @return 对应asc字符
     */
    private static char dig2Char(int dig) {
        return (char)(dig - 1 + 'A');
    }

    /**
     * 设置表格值
     * @param row 行位置
     * @param col 列位置
     * @param value 要设置的值
     */
    private void setCellValue(int row, int col, String value) {
        if (row < this.firstrow || (row > this.lastrow && this.lastrow != 0)) {
            return;
        }
        if (row != this.rownumber && this.rownumber > -1) {
            this.records.add(this.cells);
            this.cells = new String[this.lastcol - this.firstcol + 1];
        }
        if (col >= this.firstcol && col <= this.lastcol) {
            this.cells[col - this.firstcol] = value.trim();
        }
        this.rownumber = row;
    }

    /**
     * 读取Excel文件时，以触发事件方式读取数据
     */
    private class ExcelEvent implements HSSFListener {
        private POIFSFileSystem poifs;

        /** Should we output the formula, or the value it has? */
        private boolean outputFormulaValues = true;

        /** For parsing Formulas */
        private FormatTrackingHSSFListener formatListener;

        private SheetRecordCollectingListener workbookBuildingListener;

        private HSSFWorkbook stubWorkbook;

        // Records we pick up as we process
        private SSTRecord sstRecord;

        private int BoundSheetIndex = -1;

        private int unHiddenSheetIndex = -1;

        private int BOFRecordSheetIndex = -1;

        /* -1:unfind, 0:finding, 1:finded */
        private int ActiveSheetFind = -1;

        // For handling formulas with string results
        private int nextRow;

        private int nextColumn;

        private boolean outputNextStringRecord;

        private PoiExcelReader parent;

        public ExcelEvent(PoiExcelReader parent) {
            super();
            this.parent = parent;
        }

        /**
         * 处理输入文件流
         * @param is 输入流
         * @throws Exception
         */
        public void process(InputStream is) throws Exception {
            poifs = new POIFSFileSystem(is);

            MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(
                    this);
            formatListener = new FormatTrackingHSSFListener(listener);

            HSSFRequest request = new HSSFRequest();
            if (outputFormulaValues) {
                request.addListenerForAllRecords(formatListener);
            } else {
                workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
                request.addListenerForAllRecords(workbookBuildingListener);
            }

            HSSFEventFactory factory = new HSSFEventFactory();
            factory.processWorkbookEvents(request, poifs);
        }

        /**
         * 处理每个单元格
         * @param record 单元格Cell
         */
        public void processRecord(Record record) {
            int thisRow = -1, thisColumn = -1;
            String thisStr = null;

            switch (record.getSid()) {
            case BoundSheetRecord.sid:
                BoundSheetRecord bsr = (BoundSheetRecord) record;
                BoundSheetIndex++;
                //find first unhidden sheet
                if (unHiddenSheetIndex < 0 && bsr.isHidden() == false) {
                    unHiddenSheetIndex = BoundSheetIndex;
                }
                break;

            case BOFRecord.sid:
                BOFRecord br = (BOFRecord) record;
                if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                    BOFRecordSheetIndex++;
                    //the first unhidden sheet is activesheet
                    if (ActiveSheetFind < 0) {
                        if (BOFRecordSheetIndex == unHiddenSheetIndex) {
                            ActiveSheetFind = 0;
                        }
                    } else if (ActiveSheetFind == 0) {
                        ActiveSheetFind = 1;
                    }
                    if(workbookBuildingListener != null && stubWorkbook == null) {
                        stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
                    }
                }
                break;

            case SSTRecord.sid:
                sstRecord = (SSTRecord) record;
                break;

            case BlankRecord.sid:
                BlankRecord brec = (BlankRecord) record;
                thisRow = brec.getRow();
                thisColumn = brec.getColumn();
                thisStr = "";
                break;

            case BoolErrRecord.sid:
                BoolErrRecord berec = (BoolErrRecord) record;
                thisRow = berec.getRow();
                thisColumn = berec.getColumn();
                thisStr = "";
                break;

            case FormulaRecord.sid:
                FormulaRecord frec = (FormulaRecord) record;
                thisRow = frec.getRow();
                thisColumn = frec.getColumn();
                if (outputFormulaValues) {
                    if (Double.isNaN(frec.getValue())) {
                        // Formula result is a string
                        // This is stored in the next record
                        outputNextStringRecord = true;
                        nextRow = frec.getRow();
                        nextColumn = frec.getColumn();
                    } else {
                        thisStr = formatListener.formatNumberDateCell(frec);
                    }
                } else {
                    thisStr='"'+HSSFFormulaParser.toFormulaString(stubWorkbook,frec.getParsedExpression())+'"';
                }
                break;

            case StringRecord.sid:
                if (outputNextStringRecord) {
                    // String for formula
                    StringRecord srec = (StringRecord) record;
                    thisStr = srec.getString();
                    thisRow = nextRow;
                    thisColumn = nextColumn;
                    outputNextStringRecord = false;
                }
                break;

            case LabelRecord.sid:
                LabelRecord lrec = (LabelRecord) record;
                thisRow = lrec.getRow();
                thisColumn = lrec.getColumn();
                //thisStr = '"' + lrec.getValue() + '"';
                thisStr = lrec.getValue();
                break;

            case LabelSSTRecord.sid:
                LabelSSTRecord lsrec = (LabelSSTRecord) record;
                thisRow = lsrec.getRow();
                thisColumn = lsrec.getColumn();
                if (sstRecord == null) {
                    //thisStr = '"' + "(No SST Record, can't identify string)" + '"';
                    thisStr = "";
                } else {
                    //thisStr = '"' + sstRecord.getString(lsrec.getSSTIndex()).toString() + '"';
                    thisStr = sstRecord.getString(lsrec.getSSTIndex()).toString();
                }
                break;

            case NoteRecord.sid:
                NoteRecord nrec = (NoteRecord) record;
                thisRow = nrec.getRow();
                thisColumn = nrec.getColumn();
                thisStr = "";
                break;

            case NumberRecord.sid:
                NumberRecord numrec = (NumberRecord) record;
                thisRow = numrec.getRow();
                thisColumn = numrec.getColumn();
                // Format
                thisStr = formatListener.formatNumberDateCell(numrec);
                if(thisStr.trim().endsWith("_")) {
                	thisStr=thisStr.substring(0, thisStr.lastIndexOf("_"));
                }
                break;

            case RKRecord.sid:
                RKRecord rkrec = (RKRecord) record;
                thisRow = rkrec.getRow();
                thisColumn = rkrec.getColumn();
                thisStr = "";
                break;

            default:
                break;
            }

            // Handle missing column
            if (record instanceof MissingCellDummyRecord) {
                MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
                thisRow = mc.getRow();
                thisColumn = mc.getColumn();
                thisStr = "";
            }

            // do something with parent, only do activesheet
            if (thisStr != null && ActiveSheetFind == 0) {
                if (parent != null) {
                    parent.setCellValue(thisRow, thisColumn, thisStr);
                }
            }
        }
    }

    private class XSSFSheetSaxHandler extends DefaultHandler {
        //sheet索引，从0开始，0-3
        private int sheetIndex = 0;
    	/**
         * Table with styles
         */
        private StylesTable stylesTable;

        // Set when V start element is seen
        private boolean vIsOpen;

        // Set when cell start element is seen;
        // used when cell close element is seen.
        private XssfDataType nextDataType;

        // Used to format numeric cell values.
        private short formatIndex;
        private String formatString;
        private DataFormatter formatter = new DataFormatter();

        // Gathers characters as they are seen.
        private StringBuffer curValue = new StringBuffer();
        // Columns are 1 based
        private int curRow = 0;
        // Columns are 0 based
        private int curCol = -1;

        private PoiExcelReader parent = null;

    	public XSSFSheetSaxHandler(PoiExcelReader parent, int sheetIndex) {
            super();
            this.parent = parent;
            this.sheetIndex = sheetIndex;
        }

    	//只遍历一个sheet，其中sheetId为要遍历的sheet索引  
    	public void process(InputStream is) throws Exception {  

        	OPCPackage pkg = OPCPackage.open(is);
        	XSSFReader reader = new XSSFReader(pkg);
        	this.stylesTable = reader.getStylesTable();

        	Workbook wb = null;
            try {
            	if(sheetIndex < 0) {
            		wb = new XSSFWorkbook(pkg);
            		this.sheetIndex = wb.getActiveSheetIndex();
            	}

            	int index = 0;
            	boolean found = false;
                InputStream sheetStream = null;
                XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) reader.getSheetsData();
                while (iter.hasNext()) {
                	if (found == true) {
                		break;
                	} else if (index == this.sheetIndex) {
                    	found = true;
                	}
                	try {
	                	sheetStream = iter.next();
	                    if (found == true) processSheet(sheetStream);
	                    ++index;
                	} catch(Exception f) {
                		log.error("", f);
                	} finally {
                    	try {
                    		if(sheetStream != null) sheetStream.close();
                    	} catch(Exception g) {}
                	}
                }
            } catch(Exception e) {
            	throw e;
            } finally {
            	try {
            		if(wb != null) wb.close();
            	} catch(Exception e) {}
            }
        }

    	public void processSheet(InputStream sheetStream)
    			throws ParserConfigurationException, SAXException, IOException {
    		InputSource sheetSource = new InputSource(sheetStream);

            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxFactory.newSAXParser();
            XMLReader sheetParser = saxParser.getXMLReader();
            sheetParser.setContentHandler(this);
            sheetParser.parse(sheetSource);

            //需引入xercesImpl.jar
//        	XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
//        	parser.setContentHandler(this);
//        	parser.parse(sheetSource);
        }

    	@Override
    	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            if ("inlineStr".equals(name) || "v".equals(name)) {
                vIsOpen = true;
                // Clear contents cache
                curValue.setLength(0);
            }
            // c => 单元格
            else if ("c".equals(name)) {
                // Get the cell reference
                String r = attributes.getValue("r");
                int firstDigit = -1;
                for (int c = 0; c < r.length(); ++c) {
                    if (Character.isDigit(r.charAt(c))) {
                        firstDigit = c;
                        break;
                    }
                }
                curCol = nameToColumn(r.substring(0, firstDigit));

                // Set up defaults.
                this.nextDataType = XssfDataType.NUMBER;
                this.formatIndex = -1;
                this.formatString = null;
                String cellType = attributes.getValue("t");
                String cellStyleStr = attributes.getValue("s");
                if ("b".equals(cellType))
                    nextDataType = XssfDataType.BOOL;
                else if ("e".equals(cellType))
                    nextDataType = XssfDataType.ERROR;
                else if ("inlineStr".equals(cellType))
                    nextDataType = XssfDataType.INLINESTR;
                else if ("s".equals(cellType))
                    nextDataType = XssfDataType.SSTINDEX;
                else if ("str".equals(cellType))
                    nextDataType = XssfDataType.FORMULA;
                else if (cellStyleStr != null) {
                    /**
    		          * It's a number, but possibly has a style and/or special format.
    		          * Nick Burch said to use org.apache.poi.ss.usermodel.BuiltinFormats,
    		          * and I see javadoc for that at apache.org, but it's not in the
    		          * POI 3.5 Beta 5 jars.  Scheduled to appear in 3.5 beta 6.
    		          */
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                    this.formatIndex = style.getDataFormat();
                    this.formatString = style.getDataFormatString();
                    if (this.formatString == null)
                        this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
                }

            } else if ("row".equals(name)) {
                // 设置行号
                curRow = Integer.parseInt(attributes.getValue("r"));
            }
        }

    	@Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            // 根据SST的索引值的到单元格的真正要存储的字符串
            // 这时characters()方法可能会被调用多次
            String thisStr = null;

            // v => contents of a cell
            if ("v".equals(name)) {
                // Process the value contents as required.
                // Do now, as characters() may be called more than once
                switch (nextDataType) {

                    case BOOL:
                        char first = curValue.charAt(0);
                        thisStr = first == '0' ? "FALSE" : "TRUE";
                        break;

                    case ERROR:
                        //thisStr = "ERROR:" + value.toString();
                    	thisStr = "";
                        break;

                    case FORMULA:
                        // A formula could result in a string value,
                        // so always add double-quote characters.
                        thisStr = curValue.toString();
                        break;

                    case INLINESTR:
                        // TODO: have seen an example of this, so it's untested.
                        XSSFRichTextString rtsi = new XSSFRichTextString(curValue.toString());
                        thisStr = rtsi.toString();
                        break;

                    case SSTINDEX:
                        try {
                        	int curSheetIndex = Integer.parseInt(curValue.toString());
                        	System.out.println(curSheetIndex);
                        } catch (NumberFormatException ex) {}
                        break;

                    case NUMBER:
                        String n = curValue.toString();
                        if (this.formatString != null)
                            thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
                        else
                            thisStr = n;
                        break;

                    default:
                        //thisStr = "Unexpected type: " + nextDataType;
                    	thisStr = curValue.toString();
                        break;
                }

                if (parent != null){
                    parent.setCellValue(curRow - 1, curCol, thisStr);
                }
            }
        }

        /**
         * Captures characters only if a suitable element is open.
         * Originally was just "v"; extended for inlineStr also.
         */
    	@Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (vIsOpen)
            	curValue.append(ch, start, length);
        }

        /**
         * Converts an Excel column name like "C" to a zero-based index.
         *
         * @param name
         * @return Index corresponding to the specified name
         */
        private int nameToColumn(String name) {
            int column = -1;
            for (int i = 0; i < name.length(); ++i) {
                int c = name.charAt(i);
                column = (column + 1) * 26 + c - 'A';
            }
            return column;
        }
    }
}
