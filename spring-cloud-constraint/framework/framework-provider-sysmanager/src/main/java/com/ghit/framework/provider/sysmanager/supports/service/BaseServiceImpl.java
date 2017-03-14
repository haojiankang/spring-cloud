/** 
 * Project Name:EHealthData 
 * File Name:BaseServiceImpl.java 
 * Package Name:com.ghit.common.mvc.service 
 * Date:2016年7月4日下午2:13:31  
*/

package com.ghit.framework.provider.sysmanager.supports.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ghit.framework.commons.utils.Page;
import com.ghit.framework.commons.utils.bean.BeanUtils;
import com.ghit.framework.commons.utils.lang.StringUtil;
import com.ghit.framework.provider.sysmanager.api.supports.DbOnly;
import com.ghit.framework.provider.sysmanager.api.supports.service.BaseService;
import com.ghit.framework.provider.sysmanager.supports.PS;
import com.ghit.framework.provider.utils.hibernate.AbstractPojo;
import com.ghit.framework.provider.utils.hibernate.BaseDao;
import com.ghit.framework.provider.utils.validate.Validate;

/**
 * ClassName:BaseServiceImpl <br/>
 * Function: 公共业务处理接口实现. <br/>
 * Date: 2016年7月4日 下午2:13:31 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@Transactional(propagation = Propagation.SUPPORTS)
public abstract class BaseServiceImpl<PO extends AbstractPojo<?>, VO extends AbstractPojo<?>>
        implements BaseService<PO, VO> {
    protected final Log logger = LogFactory.getLog(getClass());
    protected Class<VO> voClass;
    protected Class<PO> poClass;

    @SuppressWarnings("unchecked")
    public BaseServiceImpl() {
        Class<?> class1 = getClass();
        Type type = class1.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type atype[] = ((ParameterizedType) type).getActualTypeArguments();
            poClass = (Class<PO>) atype[0];
            voClass = (Class<VO>) atype[1];
        }
    }

    public abstract BaseDao<PO, Serializable> getBaseDao();

    @Override
    public Page list(Page page) {
        boolean beforeState = listBefore(page);
        if (beforeState) {
            getBaseDao().findPage(page);
        }
        listAfter(page, beforeState);
        return page;
    }

    @Override
    public VO findById(VO t) {
        boolean beforeState = findByIdBefore(t);
        PO po = null;
        if (beforeState) {
            po = getBaseDao().get(t.getId());
        }
        findByIdAfter(po, beforeState);
        return getVO(po);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean del(VO t) {
        try {
            boolean beforeState = delBefore(t);
            if (beforeState)
                getBaseDao().delete(getPO(t));
            return delAfter(t, beforeState);
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }

    protected PO getPO(VO vo) {
        return BeanUtils.poVo(vo, poClass, "");
    }
    protected VO getVO(PO po) {
        return BeanUtils.poVo(po, voClass, "");
    }

    @Override
    @Validate
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean save(VO t) {
        try {
            PO po = getPO(t);
            boolean isAdd = StringUtils.isEmpty(t.getId());
            boolean beforeState = saveBefore(t, isAdd);
            if (beforeState) {
                if (isAdd) {
                    getBaseDao().save(po);
                } else {
                    PO oldt = getBaseDao().get(t.getId());
                    po.setCreateTime(oldt.getCreateTime());
                    po.setCreateUser(oldt.getCreateUser());
                    if (StringUtil.isEmpty(po.getRemark())) {
                        po.setRemark(oldt.getRemark());
                    }
                    getBaseDao().evict(oldt);
                    getBaseDao().update(po);
                }
            }
            return saveAfter(po, beforeState, isAdd);
        } catch (Exception e) {
            logger.error(e);
            PS.error(e.getMessage());
        }
        return false;
    }

    @Override
    @Validate
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean insert(VO t) {
        try {
            PO po = getPO(t);
            boolean beforeState = saveBefore(t, true);
            if (beforeState) {
                getBaseDao().save(po);
            }
            return saveAfter(po, beforeState, true);
        } catch (Exception e) {
            logger.error(e);
            PS.error(e.getMessage());
        }
        return false;
    }

    @Override
    @Validate
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean update(VO t) {
        try {
            PO po = getPO(t);
            boolean beforeState = saveBefore(t, false);
            if (beforeState) {
                PO oldt = getBaseDao().get(t.getId());
                po.setCreateTime(oldt.getCreateTime());
                po.setCreateUser(oldt.getCreateUser());
                if (StringUtil.isEmpty(po.getRemark())) {
                    po.setRemark(oldt.getRemark());
                }
                getBaseDao().evict(oldt);
                getBaseDao().update(po);
            }
            return saveAfter(po, beforeState, false);
        } catch (Exception e) {
            logger.error(e);
            PS.error(e.getMessage());
        }
        return false;
    }

    @Override
    public int modifyNotNullById(VO t) {
        PO po = getPO(t);
        return getBaseDao().modifyNotNullById(po);
    }

    /**
     * 
     * saveBefore:save前执行的方法。
     * <p>
     * 此处默认对DbOnly标记的属性实现了数据库唯一性检查，此时的返回值表示是否通过唯一性检查
     * </p>
     * 
     * @author ren7wei
     * @param t
     * @param isAdd
     * @return
     * 
     *         <pre>
     * true 通过唯一性检查，后续入库操作正常执行
     * fasle 未通过唯一性检查，后续入库操作将不会执行
     * 但是不会影响saveAfter的执行
     *         </pre>
     * 
     * @since JDK 1.8
     */
    protected boolean saveBefore(VO t, boolean isAdd) {
        Page page = new Page();
        Map<String, List<Field>> groups = new HashMap<>();
        BeanUtils.getFieldHaveAnnotation(t.getClass(), DbOnly.class).forEach(f -> {
            try {
                DbOnly only = f.getAnnotation(DbOnly.class);
                if (only.group().length() > 0) {
                    List<Field> list = groups.get(only.group());
                    if (list == null) {
                        list = new ArrayList<>();
                        groups.put(only.group(), list);
                    }
                    list.add(f);
                    return;
                }
                Object value = BeanUtils.getValueByField(t, f);
                page.setConditions(new HashMap<>());
                page.getConditions().put(f.getName(), value);
                if (!isAdd)
                    page.getConditions().put("id!=", t.getId());
                long findCount = getBaseDao().findCount(page);
                if (findCount > 0) {
                    PS.error(only.name() + "为：" + value + "\t的记录已存在!");
                }
            } catch (Exception e) {
                logger.error(e);
            }
        });
        if (groups.size() > 0) {
            groups.forEach((k, v) -> {
                page.setConditions(new HashMap<>());
                v.stream().forEach(f -> {
                    page.getConditions().put(f.getName(), BeanUtils.getValueByField(t, f));
                });
                if (!isAdd)
                    page.getConditions().put("id!=", t.getId());
                long findCount = getBaseDao().findCount(page);
                if (findCount > 0) {
                    PS.error(k);
                }
            });
        }
        return PS.error() == null;
    }

    protected boolean saveAfter(PO t, boolean beforeState, boolean isAdd) {
        return beforeState;
    }

    protected boolean delAfter(VO t, boolean beforeState) {
        return true;
    }

    protected boolean delBefore(VO t) {
        return true;
    }

    protected void findByIdAfter(PO t, boolean beforeState) {
    }

    protected boolean findByIdBefore(VO t) {
        return true;
    }

    protected void listAfter(Page page, boolean beforeState) {
        if (page.getResult() != null)
            page.setResult(BeanUtils.poVoList(page.getResult(), voClass, ""));
    }

    protected boolean listBefore(Page page) {
        return true;
    }
}
