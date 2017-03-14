package com.ghit.framework.provider.utils.hibernate;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.type.Type;

import com.ghit.framework.commons.utils.Page;

/**
 * Description:基础dao操作接口类。
 * 
 * @param <T>
 * @param <PK>
 * 
 * @author lengzhihui
 * @version 1.0
 * 
 *          <pre>
 * Modification History:
 * Date         Author       Version     Description
 * ------------------------------------------------------------------
 * 2016-05-02   lengzhihui    1.0        1.0 Version
 *          </pre>
 */
public interface BaseDao<T, PK extends Serializable> {

    public T get(PK id);

    public T load(PK id);

    public List<T> getAllList();

    public Long getTotalCount();

    public PK save(T entity);

    public void saveBatch(List<T> entities);

    public void update(T entity);

    public int modifyNotNullById(Object t);

    public void updateBatch(List<T> entities);

    public void delete(T entity);

    public void delete(PK id);

    public void delete(PK ids[]);

    public void flush();

    public void evict(Object obj);

    public void clear();

    public long findCount(Page page);

    public Page findPage(Page page);

    public T find(Map<String, Object> conditions);

    public List<T> findList(Map<String, Object> conditions);

    public Page findPage(Page page, Criterion acriterion[]);

    public Page findPage(Page page, Order aorder[]);

    public Page findPage(Page page, Criteria criteria);

    public Page findPage(Page page, Map<String, ?> params, Map<String, Integer> orders);

    public Page findPage(Page page, Criteria criteria, Map<String, ?> params, Map<String, Integer> orders);

    public void deleteBatch(List<T> entities);

    public Page findPageByHql(Page page, String hql, Object... values);

    public Integer findCountByHql(String hql, Object... values);

    public List<T> queryByHSql(String hsql);

    public List<T> queryByHSql(String hsql, List<Object> params);

    public List<T> queryBySql(Class<T> className, String sql);

    public List<T> queryBySql(Class<T> className, String sql, List<Object> params);

    public List<T> findXlsListByHql(Class<T> className, String sql, Object... values);

    public List<T> queryByHSql(String hsql, Map<String, ?> params);

    public List<T> queryBySql(Class<T> className, String sql, Map<String, ?> params);

    public Long getSeqByName(String seqName);

    /**
     * SQL执行 允许sql包含“：+ 命名”的占位符
     * 
     * @param String
     *            sql
     * @param Map<String,
     *            Object> params
     */
    public void executeBySql(String sql, Map<String, Object> params);

    public boolean reCreateSequence(String seqName);

    void merge(T entity);

    void persist(T entity);

    /**
     * 装配Sql查询条件语句，开头不包含and和where
     * 
     * @param args
     *            查询条件
     * @param params
     *            用于Hibernate执行查询的参数值
     * @return SQL语句的where条件部分
     */
    String assembleSQLConditions(Map<String, Object> args, LinkedHashMap<String, Object> params);

    /**
     * 组合Sql排序语句，包含order by
     * 
     * @param orders
     *            排序条件
     * @return
     */
    String assembleSQLOrderBy(List<String> orders);

    /**
     * sql分页查询
     * 
     * @param page
     *            分页条件
     * @param querysql
     *            查询语句
     * @param countsql
     *            记录数查询语句
     * @param params
     *            Hibernate执行sql的参数值
     * @param scalar
     *            查询询结果按标量集合(列名->org.hibernate.type)返回Map队列，为空时返回
     *            Page.returnClass队列，Page.returnClass再为空时返回hibernate默认转换的Map队列
     * @return
     */
    Page findPageBySql(Page page, String querysql, String countsql, LinkedHashMap<String, Object> params,
            Map<String, org.hibernate.type.Type> scalar);

    /**
     * sql非分页查询
     * 
     * @param sql
     *            查询语句
     * @param params
     *            Hibernate执行sql的参数值
     * @param scalar
     *            查询询结果按标量集合(列名->org.hibernate.type)返回Map队列，
     *            为空时返回hibernate默认转换的Map队列
     * @return
     */
    List<Map<String, Object>> queryBySql(String sql, LinkedHashMap<String, Object> params,
            Map<String, org.hibernate.type.Type> scalar);

	/**
	 * 执行sql，sql包含“：+ 命名”的占位符
	 * @param sql sql语句
	 * @param params 执行参数
	 * @param types 参数类型
	 * @return The number of entities updated or deleted.
	 */
	int executeBySql(String sql, Map<String, Object> params, Map<String, Type> types);

	/**
	 * 根据数据库主键存在与否进行更新或插入
	 * @param entity
	 * @param id
	 */
	void saveOrUpdate(T bean, PK id);

	/**
	 * Map对象转为类实例对象，注意：仅适用于JPA标注的类！
	 * @param map 列名->列值
	 * @param beanClass 目标类
	 * @return 目标类实例对象
	 */
	<E> E map2Bean(Map<String, Object> map, Class<E> beanClass);
}
