package com.ghit.framework.commons.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:分页类。request传过来的参数必须是page（第几页）、rows（每页行数）。
 * 
 * @author lengzhihui
 * @version 1.0
 * 
 *          <pre>
 * Modification History:
 * Date         Author       Version     Description
 * ------------------------------------------------------------------
 * 2016-05-02      lengzhihui    1.0        1.0 Version
 *          </pre>
 */
/**
 * 
 * ClassName: Page <br>
 * Function: 分页信息封装类. <br>
 * Reason: 简化参数传递数量，提供统一分页处理.<br>
 * date: 2016年5月2日 上午9:24:41 <br>
 *
 * @author lengzhihui
 * @version 1.0
 * @since JDK 1.8
 */
@SuppressWarnings("rawtypes")
public class Page implements Serializable{
    private static final long serialVersionUID = 1L;

    {
        conditions = new HashMap<String, Object>();
    }
    /**
     * 默认为第一页。
     */
    private int page = 1;

    /**
     * 默认每页10行。
     */
    private int rows = 10;

    /**
     * 符合条件的所有记录的行数。
     */
    private long records;

    /**
     * 记录开始行数
     */
    @SuppressWarnings("unused")
    private int rowStarted;

    /**
     * 结果集
     */
    private List<?> result;

    /**
     * Page 返回List的类型
     */
    private String returnClass;
    /**
     * 查询分页条件
     */
    private Map<String, Object> conditions;
    /**
     * 排序信息
     * [
     * +id,
     * -name
     * ]
     * 按照id升序（aes），name降序(des)进行排序
     */
    private List<String> orders;

    /**
     * 构造方法。
     */
    public Page() {
        super();
    }

    /**
     * 
     * getPage:获取当前页码，页码从1开始。
     *
     * @author ren7wei
     * @return 当前页码
     * @since JDK 1.8
     */
    public int getPage() {
        return page;
    }

    /**
     * 
     * setPage:设置当前页码,页码从1开始。
     * 
     * @author ren7wei
     * @param page
     *            当前页码
     * @since JDK 1.8
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 
     * getRows:获取页容量（每一页的记录条数），默认为10。
     *
     * @author ren7wei
     * @return 当前页容量
     * @since JDK 1.8
     */
    public int getRows() {
        return rows;
    }

    /**
     * 
     * setRows:设置页容量。
     *
     * @author ren7wei
     * @param rows
     *            页容量
     * @since JDK 1.8
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * 
     * getRecords:获取记录总条数，有查询条件时为符合条件的记录总条数
     * 
     * @author ren7wei
     * @return 记录总条数
     * @since JDK 1.8
     */
    public long getRecords() {
        return records;
    }

    /**
     * 
     * setRecords:设置符合要求的记录总条数
     *
     * @author ren7wei
     * @param records
     *            记录总条数
     * @since JDK 1.8
     */
    public void setRecords(long records) {
        this.records = records;
    }

    /**
     * 
     * getRowStarted:
     * <p>
     * 获得第一条记录的位置，用于分页查询时设置查询开始位置。
     * </p>
     * 
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    public int getRowStarted() {
        return (this.page - 1) * rows;
    }

    /**
     * 
     * getResult:符合条件的记录集合.
     *
     * @author ren7wei
     * @return 记录集合
     * @since JDK 1.8
     */
    public List<?> getResult() {
        return result;
    }

    /**
     * 
     * setResult:设置记录集合数据到Page对象中。
     * 
     * @author ren7wei
     * @param result
     *            当前Page信息对应的数据集合
     * @since JDK 1.8
     */
    public void setResult(List<?> result) {
        this.result = result;
    }

    /**
     * 
     * getReturnClass:获取数据集合的类型。
     *
     * @author ren7wei
     * @return 数据集合的完全限定名。
     * @since JDK 1.8
     */
    public String getReturnClass() {
        return returnClass;
    }

    /**
     * 
     * setReturnClass:设置查询的数据的类型
     * <p>
     * 除非需要限定查询的数据集合类型否则不推荐使用。
     * </p>
     * 
     * @author ren7wei
     * @param returnClass
     *            被查询数据对应的类型的完全限定名。
     * @since JDK 1.8
     */
    public void setReturnClass(String returnClass) {
        this.returnClass = returnClass;
    }

    /**
     * 
     * setReturnClass:设置查询的数据类型
     *
     * @author ren7wei
     * @param returnClass
     *            被查询的数据的类型
     * @see #setReturnClass(String)
     * @since JDK 1.8
     */
    public void setReturnClass(Class returnClass) {
        this.returnClass = returnClass.getName();
    }

    /**
     * 
     * getReturnClassType:获取查询数据的类型
     * 
     * @author ren7wei
     * @return 查询数据的类型
     * @see #getReturnClass()
     * @since JDK 1.8
     */
    public Class getReturnClassType() {
        Class c = null;
        try {
            if (returnClass != null)
                c = Class.forName(returnClass);
        } catch (ClassNotFoundException e) {
            c = null;
        }
        return c;
    }

    /**
     * 
     * getConditions:获取查询条件容器。
     * 
     * @author ren7wei
     * @return 查询条件容器
     * @since JDK 1.8
     */
    public Map<String, Object> getConditions() {
        return conditions;
    }

    /**
     * 
     * setConditions:设置查询条件容器。
     *
     * @author ren7wei
     * @param conditions
     *            查询条件容器
     * @since JDK 1.8
     */
    public void setConditions(Map<String, Object> conditions) {
        this.conditions = conditions;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }
    

}
