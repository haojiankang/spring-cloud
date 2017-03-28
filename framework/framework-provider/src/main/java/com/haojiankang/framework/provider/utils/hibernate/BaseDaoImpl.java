package com.haojiankang.framework.provider.utils.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.util.Assert;

import com.haojiankang.framework.commons.utils.Page;
import com.haojiankang.framework.commons.utils.bean.BeanUtils;
import com.haojiankang.framework.commons.utils.bean.ObjectUtils;
import com.haojiankang.framework.commons.utils.bean.Reflections;
import com.haojiankang.framework.commons.utils.lang.DateTimeUtil;
import com.haojiankang.framework.commons.utils.lang.StringUtil;

public class BaseDaoImpl<T, PK extends Serializable> implements BaseDao<T, PK> {

    protected final Log logger = LogFactory.getLog(getClass());
    private static final String ORDERLIST = "orderList";
    private static final String CREATEDATE = "createTime";
    private static final int ASC = 0;
    private static final int DESC = 1;
    private Class<T> entity;
    @Resource(name = "sessionFactory")
    protected SessionFactory sessionFactory;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public BaseDaoImpl() {
        Class<? extends BaseDaoImpl> class1 = getClass();
        Type type = class1.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type atype[] = ((ParameterizedType) type).getActualTypeArguments();
            entity = (Class<T>) atype[0];
        }
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public T get(PK id) {
        Assert.notNull(id, "id is required");
        return (T) getSession().get(entity, id);
    }

    public T load(PK id) {
        Assert.notNull(id, "id is required");
        return (T) getSession().load(entity, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> getAllList() {
        ClassMetadata classmetadata = sessionFactory.getClassMetadata(entity);
        if (ArrayUtils.contains(classmetadata.getPropertyNames(), ORDERLIST)) {
            String s = (new StringBuilder("from ")).append(entity.getName()).append(" as entity order by entity.")
                    .append(ORDERLIST).append(" asc, entity.").append(CREATEDATE).append(" desc").toString();
            return getSession().createQuery(s).list();
        } else {
            String s1 = (new StringBuilder("from ")).append(entity.getName()).append(" as entity order by entity.")
                    .append(CREATEDATE).append(" desc").toString();
            return getSession().createQuery(s1).list();
        }
    }

    /**
     * 查询总数据
     */
    public Long getTotalCount() {
        String s = (new StringBuilder("select count(*) from ")).append(entity.getName()).toString();
        return Long.valueOf(getSession().createQuery(s).uniqueResult().toString());
    }

    @SuppressWarnings("unchecked")
    public PK save(T entity) {
        return ((PK) getSession().save(entity));
    }

    public void update(T entity) {
        Assert.notNull(entity, "entity is required");
        try {
            getSession().update(entity);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
    }

    public void persist(T entity) {
        Assert.notNull(entity, "entity is required");
        try {
            getSession().persist(entity);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
    }

    @Override
    public void merge(T entity) {
        Assert.notNull(entity, "entity is required");
        try {
            getSession().merge(entity);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
    }

    public void delete(T entity) {
        Assert.notNull(entity, "entity is required");
        getSession().delete(entity);
    }

    public void delete(PK id) {
        Assert.notNull(id, "id is required");
        T entity = load(id);
        getSession().delete(entity);
    }

    public void delete(PK ids[]) {
        Assert.notEmpty(ids, "ids must not be empty");
        for (PK id : ids) {
            T entity = load(id);
            getSession().delete(entity);
        }
    }

    public void flush() {
        getSession().flush();
    }

    public void evict(Object obj) {
        Assert.notNull(obj, "object is required");
        getSession().evict(obj);
    }

    public void clear() {
        getSession().clear();
    }

    /**
     * 分页查询（无参、无序）
     * 
     * @param page
     * @param acriterion
     */
    public Page findPage(Page page) {
        Criteria criteria = getSession().createCriteria(entity);
        analyticalCondition(page, criteria);
        return findPage(page, criteria);
    }

    /**
     * 分页查询（有集合参数、有集合排序）
     * 
     * @param page
     * @param params
     * @param orders
     */
    public Page findPage(Page page, Map<String, ?> params, Map<String, Integer> orders) {
        Criteria criteria = getSession().createCriteria(entity);
        return findPage(page, criteria, params, orders);
    }

    /**
     * 分页查询（有结构化参数）
     * 
     * @param page
     * @param acriterion
     */
    public Page findPage(Page page, Criterion acriterion[]) {
        Criteria criteria = getSession().createCriteria(entity);
        Criterion acriterion1[];
        int j = (acriterion1 = acriterion).length;
        for (int i = 0; i < j; i++) {
            Criterion criterion = acriterion1[i];
            criteria.add(criterion);
        }

        return findPage(page, criteria);
    }

    /**
     * 分页查询（默认排序）
     * 
     * @param page
     * @param orders
     */
    public Page findPage(Page page, Order orders[]) {
        Criteria criteria = getSession().createCriteria(entity);
        org.hibernate.criterion.Order orders1[];
        int j = (orders1 = orders).length;
        for (int i = 0; i < j; i++) {
            org.hibernate.criterion.Order order = orders1[i];
            criteria.addOrder(order);
        }

        return findPage(page, criteria);
    }

    /**
     * 分页查询（无参、无序）
     * 
     * @param page
     * @param criteria
     */
    public Page findPage(Page page, Criteria criteria) {
        Assert.notNull(page, "Page is required");
        Assert.notNull(criteria, "criteria is required");

        page.setRecords(getTotalCount(criteria));

        criteria.setFirstResult(page.getRowStarted());
        criteria.setMaxResults(page.getRows());
        page.setResult(criteria.list());
        return page;
    }

    /**
     * 分页查询（有集合参数、有集合排序）
     * 
     * @param page
     * @param criteria
     * @param params
     * @param orders
     * @return
     */
    public Page findPage(Page page, Criteria criteria, Map<String, ?> params, Map<String, Integer> orders) {
        Assert.notNull(page, "Page is required");
        Assert.notNull(criteria, "criteria is required");

        if (params != null) {
            // 设置参数
            for (Iterator<String> keys = params.keySet().iterator(); keys.hasNext();) {
                String key = keys.next();
                Object value = params.get(key);
                if (value instanceof String) {
                    if (key.contains(".")) {
                        String tkey = StringUtils.substringBefore(key, ".");
                        criteria.createAlias(tkey, tkey);
                    }
                    if (key.endsWith("LIKE")) {
                        key = key.substring(0, key.indexOf("LIKE"));
                        criteria.add(
                                Restrictions.like(key, (new StringBuilder("%")).append(value).append("%").toString()));
                    } else if (key.endsWith("STARTWITH")) {
                        key = key.substring(0, key.indexOf("STARTWITH"));
                        criteria.add(
                                Restrictions.like(key, (new StringBuilder(value.toString())).append("%").toString()));
                    } else if (key.endsWith("ENDWITH")) {
                        key = key.substring(0, key.indexOf("ENDWITH"));
                        criteria.add(Restrictions.like(key, (new StringBuilder("%")).append(value).toString()));

                    } else {
                        criteria.add(Restrictions.eq(key, value));
                    }
                    // criteria.add(Restrictions.like(key, (new
                    // StringBuilder("%")).append(value).append("%").toString()));
                } else if (value instanceof Integer || value instanceof Long) {
                    criteria.add(Restrictions.eq(key, value));
                } else if (value instanceof Date || value instanceof Timestamp) {
                    if (key.endsWith("Start")) {
                        key = key.substring(0, key.indexOf("Start"));
                        criteria.add(Restrictions.ge(key, value));
                    }
                    if (key.endsWith("End")) {
                        key = key.substring(0, key.indexOf("End"));
                        criteria.add(
                                Restrictions.le(key, DateTimeUtil.addDate((Date) value, Calendar.DAY_OF_MONTH, 1)));
                    }
                }
            }
        }

        if (orders != null) {
            Order o = null;
            // 设置排序
            for (Iterator<String> keys = orders.keySet().iterator(); keys.hasNext();) {
                String key = keys.next();
                int order = orders.get(key);
                if (order == ASC) {
                    o = Order.asc(key);
                } else if (order == DESC) {
                    o = Order.desc(key);
                }
                criteria.addOrder(o);
            }
        }

        page.setRecords(getTotalCount(criteria));
        criteria.setFirstResult(page.getRowStarted());
        criteria.setMaxResults(page.getRows());
        page.setResult(criteria.list());
        return page;
    }

    /**
     * 查询总记录数（结构化）
     * 
     * @param criteria
     * @return records
     */
    private long getTotalCount(Criteria criteria) {
        Assert.notNull(criteria, "criteria is required");
        long i = 0;
        CriteriaImpl criteriaimpl = (CriteriaImpl) criteria;
        Projection projection = criteriaimpl.getProjection();
        ResultTransformer resulttransformer = criteriaimpl.getResultTransformer();
        Object uniqueResult = criteriaimpl.setProjection(Projections.rowCount()).uniqueResult();
        if (uniqueResult != null) {
            i = Long.valueOf(uniqueResult.toString()).longValue();
        }
        criteriaimpl.setProjection(projection);
        if (projection == null) {
            criteriaimpl.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (resulttransformer != null) {
            criteriaimpl.setResultTransformer(resulttransformer);
        }
        return i;
    }

    public void saveBatch(List<T> entities) {

        try {
            for (int i = 0, size = entities.size(); i < size; i++) {
                getSession().save(entities.get(i));
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
    }

    /**
     * 批量跟新
     */
    public void updateBatch(List<T> entities) {
        try {
            for (int i = 0, size = entities.size(); i < size; i++) {
                getSession().update(entities.get(i));
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }

    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<T> entities) {
        try {
            for (int i = 0, size = entities.size(); i < size; i++) {
                getSession().delete(entities.get(i));
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
    }

    /**
     * Hql分页查询list
     */
    @SuppressWarnings("unchecked")
    public List<T> findXlsListByHql(Class<T> className, String hql, Object... values) {
        Query query = getSession().createQuery(hql);
        query.setResultTransformer(Transformers.aliasToBean(className));
        this.setParameters(query, values);
        return query.list();
    }

    /**
     * Hql分页查询
     */
    public Page findPageByHql(Page page, String hql, Object... values) {

        if (page.getRecords() <= 0) {
            int count = this.findCountByHql(hql, values);
            page.setRecords(count);
        }
        Query query = getSession().createQuery(hql);
        query.setFirstResult(page.getRowStarted());
        query.setMaxResults(page.getRows());
        if (null != page.getReturnClassType()) {
            query.setResultTransformer(Transformers.aliasToBean(page.getReturnClassType()));
        }
        this.setParameters(query, values);
        page.setResult(query.list());
        return page;
    }

    /**
     * Hql查询总条数
     */
    public Integer findCountByHql(String hql, Object... values) {
        int beginPos = (" " + hql).toLowerCase().indexOf(" from ");
        hql = "select count(*) " + hql.substring(beginPos);
        Query query = getSession().createQuery(hql);
        this.setParameters(query, values);
        return Integer.valueOf(query.uniqueResult().toString());
    }

    /**
     * Hql查询设置参数
     */
    public void setParameters(Query query, Object... values) {
        if (values != null) {
            if (values.length == 1 && values[0] != null && values[0] instanceof Map) {
                @SuppressWarnings({ "unchecked", "rawtypes" })
                Map<String, ?> valueMap = (Map) values[0];
                String[] params = query.getNamedParameters();
                for (int i = 0, max = params.length; i < max; i++) {
                    query.setParameter(params[i], valueMap.get(params[i]));
                }
            } else {
                for (int i = 0; i < values.length; i++) {
                    if (values[0] != null) {
                        query.setParameter(i, values[i]);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> queryBySql(Class<T> className, String sql) {
        return getSession().createSQLQuery(sql).addEntity(className).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> queryByHSql(String hsql) {
        return getSession().createQuery(hsql).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> queryBySql(Class<T> className, String sql, List<Object> params) {
        SQLQuery query = getSession().createSQLQuery(sql);
        query.addEntity(className);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }

        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> queryByHSql(String hsql, List<Object> params) {
        Query query = getSession().createQuery(hsql);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + "", params.get(i));
        }
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> queryByHSql(String hsql, Map<String, ?> params) {
        Query query = getSession().createQuery(hsql);
        for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
            String key = iterator.next();
            Object value = params.get(key);
            query.setParameter(key, value);
        }
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> queryBySql(Class<T> className, String sql, Map<String, ?> params) {
        SQLQuery query = getSession().createSQLQuery(sql);
        query.addEntity(className);
        for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
            String key = iterator.next();
            Object value = params.get(key);
            query.setParameter(key, value);
        }
        return query.list();
    }

    @Override
    public Long getSeqByName(String seqName) {

        String sql = "select " + seqName + ".Nextval from dual";
        SQLQuery query = getSession().createSQLQuery(sql);
        return Long.valueOf(query.uniqueResult().toString());
    }

    @Override
    public boolean reCreateSequence(String seqName) {
        String sql1 = "drop sequence " + seqName;
        String sql2 = "create sequence " + seqName
                + " \n minvalue 0000000000 \n maxvalue 9999999999 \n start with 0000000000 \n increment by 1\n nocache";
        this.executeBySql(sql1, null);
        this.executeBySql(sql2, null);
        return true;
    }

    @Override
    public void executeBySql(String sql, Map<String, Object> params) {
        // 组合SQL条件
        sql = combineSqlContitons(sql, params, null);

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (params != null && params.size() > 0) {
            for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
                String key = iterator.next();
                Object value = params.get(key);
                query.setParameter(key.substring(key.indexOf(".") + 1, key.length()), value);
            }
        }
        query.executeUpdate();
    }

    /**
     * 组合Sql条件
     * 
     * @param hql
     * @param params
     * @param orders
     * @return
     */
    private String combineSqlContitons(String hql, final Map<String, Object> params, final Map<String, String> orders) {
        StringBuffer sql = new StringBuffer(hql);
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value instanceof Date || value instanceof Timestamp) {
                    if (key.endsWith("Start")) {
                        String temkey = key.substring(0, key.indexOf("Start"));
                        sql.append(" and ").append(temkey).append(" >= :")
                                .append(key.substring(key.indexOf(".") + 1, key.length()));
                    } else if (key.endsWith("End")) {
                        String temkey = key.substring(0, key.indexOf("End"));
                        sql.append(" and ").append(temkey).append(" <= :")
                                .append(key.substring(key.indexOf(".") + 1, key.length()));
                        params.put(key, DateTimeUtil.addDate((Date) value, Calendar.DAY_OF_MONTH, 1));
                    } else {
                        sql.append(" and ").append(key).append(" = :")
                                .append(key.substring(key.indexOf(".") + 1, key.length()));
                    }
                } else if (value instanceof String) {
                    // 是否模糊查询
                    if (key.endsWith("LIKE")) {
                        String temkey = key.substring(0, key.indexOf("LIKE"));
                        sql.append(" and ").append(temkey).append(" like :")
                                .append(key.substring(key.indexOf(".") + 1, key.length())).append("");
                        params.put(key, "%" + value.toString() + "%");
                    } else if (key.endsWith("STARTWITH")) {
                        String temkey = key.substring(0, key.indexOf("STARTWITH"));
                        sql.append(" and ").append(temkey).append(" like :")
                                .append(key.substring(key.indexOf(".") + 1, key.length())).append("");
                        params.put(key, value.toString() + "%");
                    } else if (key.endsWith("ENDWITH")) {
                        String temkey = key.substring(0, key.indexOf("ENDWITH"));
                        sql.append(" and ").append(temkey).append(" like :")
                                .append(key.substring(key.indexOf(".") + 1, key.length())).append("");
                        params.put(key, "%" + value.toString());
                    } else {
                        sql.append(" and ").append(key).append(" = :")
                                .append(key.substring(key.indexOf(".") + 1, key.length())).append("");
                        params.put(key, value.toString());
                    }
                } else if (value instanceof Integer || value instanceof Long) {
                    sql.append(" and " + key + " =:" + key.substring(key.indexOf(".") + 1, key.length()) + " ");
                } else if (value instanceof Object[]) {
                    sql.append(" and " + key + " in (:" + key.substring(key.indexOf(".") + 1, key.length()) + ") ");
                }
            }
        }
        if (orders != null && orders.size() > 0) {
            sql.append(" order by ");
            // 设置排序
            for (Iterator<String> keys = orders.keySet().iterator(); keys.hasNext();) {
                String key = keys.next();
                String order = orders.get(key);
                sql.append(key).append(" ").append(order).append(",");
            }
            sql.delete(sql.length() - 1, sql.length());
        }
        return sql.toString();
    }

    protected Criteria createCriteria() {
        return getSession().createCriteria(entity);
    }

    protected void analyticalCondition(Page page, Criteria criteria, String... alias) {
        if (page.getConditions() != null) {
            Set<String> aliasSet = analyticalCriterion(page.getConditions(), criteria);
            analyticalOrders(page, criteria, aliasSet);
            if (alias != null)
                aliasSet.addAll(Arrays.asList(alias));
            analyticalAlias(criteria, aliasSet);
        }
    }

    protected void analyticalCondition(Map<String, Object> conditions, Criteria criteria) {
        if (conditions != null) {
            Set<String> aliasSet = analyticalCriterion(conditions, criteria);
            analyticalAlias(criteria, aliasSet);
        }
    }

    private Set<String> analyticalCriterion(Map<String, Object> conditions, Criteria criteria) {
        Iterator<Entry<String, Object>> it_cond = conditions.entrySet().iterator();
        Set<String> aliasSet = new HashSet<String>();
        while (it_cond.hasNext()) {
            Entry<String, Object> next = it_cond.next();
            criteria.add(conversionCondition(next.getKey(), next.getValue(), criteria));
            analyticalAlias(next.getKey(), aliasSet);
        }
        return aliasSet;
    }

    private void analyticalAlias(Criteria criteria, Set<String> aliasSet) {
        Iterator<String> it_alias = aliasSet.iterator();
        while (it_alias.hasNext()) {
            String next = it_alias.next();
            criteria.createAlias(next, next);
        }
    }

    protected void analyticalOrders(Page page, Criteria criteria, Set<String> aliasSet) {
        if (page.getOrders() != null) {
            page.getOrders().forEach(o -> {
                if (o.startsWith("+")) {
                    criteria.addOrder(Order.asc(o.substring(1)));
                } else if (o.startsWith("-")) {
                    criteria.addOrder(Order.desc(o.substring(1)));
                }
                analyticalAlias(o.substring(1), aliasSet);
            });
        }
    }

    protected void analyticalAlias(String key, Set<String> alias) {
        if (key.contains(" or ")) {
            analyticalAlias(StringUtils.substringBefore(key, " or ").trim(), alias);
            analyticalAlias(StringUtils.substringAfter(key, " or ").trim(), alias);
        } else if (key.contains(" and ")) {
            analyticalAlias(StringUtils.substringBefore(key, " and ").trim(), alias);
            analyticalAlias(StringUtils.substringAfter(key, " and ").trim(), alias);
        } else if (key.contains(".")) {
            alias.add(StringUtils.substringBefore(key, "."));
        }
    }

    protected Criterion conversionCondition(String key, Object value, Criteria criteria) {
        Criterion criterion = null;
        if (value instanceof String) {
            if (key.contains(" or ")) {
                String valueBefore = StringUtils.substringBefore(value.toString(), " or ").trim();
                String valueAfter = StringUtils.substringAfter(value.toString(), " or ").trim();
                String keyBefore = StringUtils.substringBefore(key, " or ").trim();
                String keyAfter = StringUtils.substringAfter(key, " or ").trim();
                if (StringUtils.isEmpty(valueAfter))
                    valueAfter = value.toString();
                Disjunction disjunction = Restrictions.disjunction();
                disjunction.add(conversionCondition(keyBefore, valueBefore, criteria));
                disjunction.add(conversionCondition(keyAfter, valueAfter, criteria));
                criterion = disjunction;
            } else if (key.contains(" and ")) {
                Conjunction conjunction = Restrictions.conjunction();
                conjunction.add(conversionCondition(StringUtils.substringBefore(key, " and ").trim(),
                        StringUtils.substringBefore(value.toString(), " and ").trim(), criteria));
                conjunction.add(conversionCondition(StringUtils.substringAfter(key, " and ").trim(),
                        StringUtils.substringAfter(value.toString(), " and ").trim(), criteria));
                criterion = conjunction;
            } else {
                if (key.endsWith("LIKE")) {
                    criterion = Restrictions.like(StringUtils.substringBefore(key, "LIKE"),
                            (new StringBuilder("%")).append(value).append("%").toString());
                } else if (key.endsWith("STARTWITH")) {
                    criterion = Restrictions.like(StringUtils.substringBefore(key, "STARTWITH"),
                            (new StringBuilder(value.toString())).append("%").toString());
                } else if (key.endsWith("ENDWITH")) {
                    criterion = Restrictions.like(StringUtils.substringBefore(key, "ENDWITH"),
                            (new StringBuilder("%")).append(value).toString());
                } else if (key.endsWith("!=")) {
                    criterion = Restrictions.ne(StringUtils.substringBefore(key, "!="), value);
                } else {
                    criterion = Restrictions.eq(key, value);
                }
            }
        } else if (value instanceof Integer || value instanceof Long) {
            criterion = Restrictions.eq(key, value);
        } else if (value instanceof Date || value instanceof Timestamp) {
            if (key.endsWith("Start")) {
                criterion = Restrictions.ge(StringUtils.substringBefore(key, "Start"), value);
            }
            if (key.endsWith("End")) {
                criterion = Restrictions.le(StringUtils.substringBefore(key, "End"), value);
            }
        } else if (value instanceof Object[]) {
            criterion = Restrictions.in(key, (Object[]) value);
        }
        return criterion;
    }

    @Override
    public long findCount(Page page) {
        Criteria criteria = getSession().createCriteria(entity);
        analyticalCondition(page, criteria);
        long totalCount = getTotalCount(criteria);
        return totalCount;
    }

    public String assembleSQLConditions(Map<String, Object> args, LinkedHashMap<String, Object> params) {
        if (args == null || args.size() < 1)
            return null;
        StringBuffer sbf = new StringBuffer(2000);
        Field[] fields = getEntityFields(entity);
        for (Entry<String, Object> entry : args.entrySet()) {
            if (ObjectUtils.isEmpty(entry.getValue()))
                continue;// 忽略空参数
            if (sbf.length() > 0)
                sbf.append(" and ");
            sbf.append(" (").append(assembleSQL(entry.getKey(), entry.getValue(), params, fields)).append(")");
        }
        return sbf.toString();
    }

    public String assembleSQLOrderBy(List<String> orders) {
        if (orders == null || orders.size() < 1)
            return null;
        StringBuffer sbf = new StringBuffer(1000);
        sbf.append(" order by ");
        Field[] fields = getEntityFields(entity);
        orders.forEach(o -> {
            String str = (o.startsWith("+") || o.startsWith("-")) ? o.substring(1) : o;
            str = findColumnName(fields, str);
            sbf.append(str).append(o.startsWith("+") ? " asc," : (o.startsWith("-") ? " desc," : ","));
        });
        sbf.delete(sbf.length() - 1, sbf.length());
        return sbf.toString();
    }

    public Page findPageBySql(Page page, String querysql, String countsql, LinkedHashMap<String, Object> params,
            Map<String, org.hibernate.type.Type> scalar) {
        int count = findCountBySql(countsql, params);
        if (count < 1)
            return page;
        page.setRecords(count);

        Query query = null;
        if (null != scalar && !scalar.isEmpty()) {
            query = getScalarQuery(querysql, scalar);
        } else if (page.getReturnClassType() != null) {
            query = getSession().createSQLQuery(querysql)
                    .setResultTransformer(Transformers.aliasToBean(page.getReturnClassType()));
        } else {
            query = getDefaultQuery(querysql);
        }
        if (params != null && !params.isEmpty()) {
            query.setProperties(params);
        }
        query.setFirstResult(page.getRowStarted());
        query.setMaxResults(page.getRows());
        page.setResult(query.list());
        return page;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryBySql(String sql, LinkedHashMap<String, Object> params,
            Map<String, org.hibernate.type.Type> scalar) {
        Query query = (null != scalar && !scalar.isEmpty()) ? getScalarQuery(sql, scalar) : getDefaultQuery(sql);
        if (params != null && !params.isEmpty()) {
            query.setProperties(params);
        }
        return query.list();
    }

    /**
     * 装配每一个SQL条件子句和对应参数
     * 
     * @param key
     *            查询条件键
     * @param value
     *            查询条件值
     * @param params
     *            装配的参数
     * @param fields
     *            字典：类属性名-数据库列名
     * @return 查询条件子句
     */
    private String assembleSQL(String key, Object value, LinkedHashMap<String, Object> params, Field[] fields) {
        String[] ks, vs = null;
        String upperKey = key.toUpperCase();
        StringBuffer sbf = new StringBuffer(1000);
        if (value instanceof Date || value instanceof Timestamp) {
            if (upperKey.endsWith("START")) {// 起始时间
                params.put(key, value);
                return sbf.append(findColumnName(fields, key.substring(0, upperKey.indexOf("START")))).append(" >= :")
                        .append(key).toString();

            } else if (upperKey.endsWith("END")) {// 截止时间
                params.put(key, value);
                return sbf.append(findColumnName(fields, key.substring(0, upperKey.indexOf("END")))).append(" <= :")
                        .append(key).toString();

            } else {// 时间等于
                params.put(key, value);
                return sbf.append(findColumnName(fields, key)).append(" = :").append(key).toString();
            }

        } else if (value.getClass().isArray() || ClassUtils.isAssignable(value.getClass(), Collection.class)) {
            params.put(key, value);
            return sbf.append(findColumnName(fields, key)).append(" in (:").append(key).append(")").toString();

        } else {
            if (upperKey.contains(" OR ")) {// 或者条件
                ks = key.split("(?i) or ");
                vs = value.toString().split("(?i) or ");
                for (int i = 0; i < ks.length; i++) {
                    if (sbf.length() > 0)
                        sbf.append(" or ");
                    sbf.append(assembleSQL(ks[i].trim(), i < vs.length ? vs[i].trim() : vs[vs.length - 1].trim(),
                            params, fields));
                }
                return sbf.toString();

            } else if (upperKey.contains(" AND ")) {// 并且条件
                ks = key.split("(?i) and ");
                vs = value.toString().split("(?i) and ");
                for (int i = 0; i < Math.min(ks.length, vs.length); i++) {
                    if (sbf.length() > 0)
                        sbf.append(" and ");
                    sbf.append(assembleSQL(ks[i].trim(), vs[i].trim(), params, fields));
                }
                return sbf.toString();

            } else if (upperKey.endsWith("LIKE")) {// 模糊查询
                params.put(key, (new StringBuffer("%")).append(value).append("%").toString());
                return sbf.append(findColumnName(fields, key.substring(0, upperKey.indexOf("LIKE")))).append(" like :")
                        .append(key).toString();
            } else if (key.endsWith("STARTWITH")) {
                params.put(key, (new StringBuffer(value.toString())).append("%").toString());
                return sbf.append(findColumnName(fields, key.substring(0, upperKey.indexOf("STARTWITH"))))
                        .append(" like :").append(key).toString();
            } else if (key.endsWith("ENDWITH")) {
                params.put(key, (new StringBuffer("%")).append(value).toString());
                return sbf.append(findColumnName(fields, key.substring(0, upperKey.indexOf("ENDWITH"))))
                        .append(" like :").append(key).toString();

            } else if (upperKey.endsWith("!=")) {// 不等于
                params.put(key, value);
                return sbf.append(findColumnName(fields, key.substring(0, upperKey.indexOf("!=")))).append(" <> :")
                        .append(key).toString();

            } else {// 等于
                params.put(key, value);
                return sbf.append(findColumnName(fields, key)).append(" = :").append(key).toString();
            }
        }
    }

    /**
     * Sql查询总条数
     */
    protected Integer findCountBySql(String sql, LinkedHashMap<String, Object> params) {
        Query query = getSession().createSQLQuery(sql);
        if (params != null && params.size() > 0) {
            for (Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue().getClass().isArray()) {
                    query.setParameterList(entry.getKey(), (Object[]) entry.getValue());
                } else if (ClassUtils.isAssignable(entry.getValue().getClass(), Collection.class)) {
                    query.setParameterList(entry.getKey(), (Collection<?>) entry.getValue());
                } else {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        return Integer.valueOf(query.uniqueResult().toString());
    }

    /**
     * 通过传入的标量集合构造查询器
     * 
     * @param sql
     * @param scalar
     * @return
     */
    private SQLQuery getScalarQuery(String sql, Map<String, org.hibernate.type.Type> scalar) {
        SQLQuery query = (SQLQuery) getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        for (Entry<String, org.hibernate.type.Type> entry : scalar.entrySet())
            query.addScalar(entry.getKey(), entry.getValue());
        return query;
    }

    /**
     * 构造默认查询器
     * 
     * @param sql
     * @return
     */
    @SuppressWarnings("serial")
    private SQLQuery getDefaultQuery(String sql) {
        return (SQLQuery) getSession().createSQLQuery(sql).setResultTransformer(new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] values, String[] columns) {
                Map<String, Object> map = new LinkedHashMap<String, Object>(1);
                for (int i = 0; i < columns.length; i++)
                    map.put(columns[i].toLowerCase(), values[i]);
                return map;
            }

            @SuppressWarnings("rawtypes")
            @Override
            public List transformList(List lst) {
                return lst;
            }
        });
    }

    /**
     * 查找类字段的映射列名
     * 
     * @param fields
     * @param name
     * @return
     */
    private String findColumnName(Field[] fields, String name) {
        if (fields == null || fields.length < 1 || name == null || name.trim().length() < 1) {
            return name;
        }
        String strDBName = null;
        String strName = name.trim();
        for (Field f : fields) {
            if (f.getName().equals(strName)) {
                strDBName = getColumnName(f);
                break;
            }
        }
        if (strDBName == null || strDBName.length() < 1) {
            for (Field f : fields) {
                if (f.getName().equalsIgnoreCase(strName)) {
                    strDBName = getColumnName(f);
                    break;
                }
            }
        }
        return (strDBName == null || strDBName.length() < 1) ? name : strDBName;
    }

    /**
     * 获取指定字段的数据库列名
     * 
     * @param field
     * @return
     */
    private String getColumnName(Field field) {
        if (field == null)
            return null;
        String strDBName = null;
        Column annoCol = field.getAnnotation(Column.class);
        if (annoCol != null) {
            strDBName = StringUtil.trimToEmpty(annoCol.name());
        }
        if (strDBName == null || strDBName.length() < 1) {
            JoinColumn annoJoinCol = field.getAnnotation(JoinColumn.class);
            if (annoJoinCol != null)
                strDBName = StringUtil.trimToEmpty(annoJoinCol.name());
        }
        return (strDBName == null || strDBName.length() < 1) ? null : strDBName;
    }

    /**
     * 获取当前类和父类的所有字段，同名字段取当前类
     * 
     * @param clazz
     * @return
     */
    private Field[] getEntityFields(Class<?> clazz) {
        List<Field> lst = new ArrayList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field f : fields) {
                if (!f.isAnnotationPresent(Transient.class))
                    lst.add(f);
            }
        }
        try {
            Class<?> superClass = Reflections.getClassGenricType(clazz);
            if ((superClass != null) && !Object.class.equals(superClass)) {
                Field[] superFields = superClass.getDeclaredFields();
                if (superFields != null && superFields.length > 0) {
                    for (Field f : superFields) {
                        if (f.isAnnotationPresent(Transient.class))
                            continue;
                        // 去重处理
                        boolean blExists = false;
                        for (Field s : lst) {
                            if (s.getName().equals(f.getName())) {
                                blExists = true;
                                break;
                            }
                        }
                        if (blExists != true)
                            lst.add(f);
                    }
                }
            }
        } catch (Exception e) {
        }
        return lst.toArray(new Field[lst.size()]);
    }

    @Override
    public int modifyNotNullById(Object t) {
        Assert.notNull(t, "entity is required");
        try {
            Map<String, Object> values = new HashMap<>();
            StringBuilder sql = new StringBuilder();
            Field id = BeanUtils.getField(t.getClass(), f -> {
                return f.getAnnotation(Id.class) != null;
            });
            buildModifySql(t, values, sql, id);
            Query query = getSession().createQuery(sql.toString());
            values.forEach((k, v) -> {
                query.setParameter(k, v);
            });
            query.setParameter(id.getName(), id.get(t));
            return query.executeUpdate();
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
        return -1;
    }

    private void buildModifySql(Object t, Map<String, Object> values, StringBuilder sql, Field id) {
        sql.append("update ");
        sql.append(entity.getSimpleName());
        sql.append(" set ");
        List<Field> fields = BeanUtils.getFields(t.getClass(), f -> {
            return f.getAnnotation(Id.class) == null && f.getAnnotation(Transient.class) == null
                    && !Modifier.isStatic(f.getModifiers());
        });
        id.setAccessible(true);
        fields.forEach(f -> {
            f.setAccessible(true);
            try {
                Object value = f.get(t);
                if (value == null)
                    return;
                sql.append(f.getName());
                sql.append("=:");
                sql.append(f.getName());
                sql.append(",");
                values.put(f.getName(), value);
            } catch (Exception e) {
                logger.error(e);
            }
        });
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where ");
        sql.append(id.getName());
        sql.append("=:");
        sql.append(id.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public T find(Map<String, Object> conditions) {
        Criteria criteria = getSession().createCriteria(entity);
        analyticalCondition(conditions, criteria);
        return (T) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findList(Map<String, Object> conditions) {
        Criteria criteria = getSession().createCriteria(entity);
        criteria.addOrder(Order.asc("createTime"));
        analyticalCondition(conditions, criteria);
        return criteria.list();
    }

    @Override
    public int executeBySql(String sql, Map<String, Object> params, Map<String, org.hibernate.type.Type> types) {
        SQLQuery query = getSession().createSQLQuery(sql);
        String[] names = query.getNamedParameters();
        if (null != names && names.length > 0 && null != params && params.size() > 0) {
            boolean bl = null != types && types.size() >= params.size();
            for (String name : names) {
                if (true == bl) {
                    query.setParameter(name, params.get(name), types.get(name));
                } else {
                    query.setParameter(name, params.get(name));
                }
            }
        }
        return query.executeUpdate();
    }

    @Override
    public void saveOrUpdate(T bean, PK id) {
        Assert.notNull(entity, "entity is required");
        T e = (T) getSession().get(entity, id);
        if (null != e) {
            getSession().update(bean);
        } else {
            getSession().save(bean);
        }
    }

    /**
     * Map对象转为类实例对象，注意：仅适用于JPA标注的类！
     * 
     * @param map
     *            列名->列值
     * @param beanClass
     *            目标类
     * @return 目标类对象
     */
    public <E> E map2Bean(Map<String, Object> map, Class<E> beanClass) {
        if (null == map)
            return (E) null;

        String strColumnName = null;
        Field[] fields = getEntityFields(beanClass);
        Map<String, Field> mapColumnField = new HashMap<String, Field>();
        for (Field f : fields) {
            strColumnName = getColumnName(f);
            if (null != strColumnName && strColumnName.trim().length() > 0)
                mapColumnField.put(strColumnName.trim().toLowerCase(), f);
            else
                mapColumnField.put(f.getName().toLowerCase(), f);
        }

        E bean;
        try {
            bean = beanClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e1) {
            return null;
        }

        map.forEach((k, v) -> {
            if (mapColumnField.containsKey(k.toLowerCase()))
                setFieldValue(bean, mapColumnField.get(k.toLowerCase()), v);
        });
        return bean;
    }

    private boolean setFieldValue(Object bean, Field field, Object fieldvalue) {
        Object value = (field.getType().equals(fieldvalue.getClass())
                || ClassUtils.isAssignable(fieldvalue.getClass(), field.getType())) ? fieldvalue
                        : ObjectUtils.convert(fieldvalue, field.getType());

        boolean success = false;
        try {
            FieldUtils.writeField(field, bean, value, true);
            success = true;
        } catch (Exception e) {
        }
        if (success)
            return true;

        try {
            BeanUtils.setProperty(bean, field.getName(), value);
            success = true;
        } catch (Exception e) {
        }
        if (success)
            return true;

        try {
            BeanUtils.setBeanProperty(bean, field.getName(), value);
            success = true;
        } catch (Exception e) {
        }
        return success;
    }
}
