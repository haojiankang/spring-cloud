/** 
 * Project Name:EHealthData 
 * File Name:BaseService.java 
 * Package Name:com.ghit.common.mvc.service 
 * Date:2016年7月4日下午2:11:08  
*/

package com.github.haojiankang.framework.provider.sysmanager.api.supports.service;

import com.github.haojiankang.framework.commons.utils.Page;
import com.github.haojiankang.framework.provider.utils.hibernate.AbstractPojo;

/**
 * ClassName:BaseService <br/>
 * Function: 公用基础服务接口. <br/>
 * Date: 2016年7月4日 下午2:11:08 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public interface BaseService<PO extends AbstractPojo<?>,VO extends AbstractPojo<?>> {
    /**
     * 
     * list:标准实体查询通用业务处理方法，查询结果会存放在page.result中.
     *
     * @author ren7wei
     * @param page
     * @since JDK 1.8
     */
    Page list(Page page); 
    /**
     * 
     * findById:根据Id查询实体通用业务处理方法.
     *
     * @author ren7wei
     * @param t
     * @return
     * @since JDK 1.8
     */
    VO findById(VO t);
    /**
     * 
     * del:标准删除统一业务处理方法.
     *
     * @author ren7wei
     * @param t
     * @return
     * @since JDK 1.8
     */
    boolean del(VO t);
    /**
     * 
     * save:标准保存、新增统一业务处理方法。.
     *
     * @author ren7wei
     * @param t
     * @return
     * @since JDK 1.8
     */
    boolean save(VO t);
    /**
     * 新增保存
     * @param t
     * @return
     */
    boolean insert(VO t);
    /**
     * 修改保存
     * @param t
     * @return
     */
    boolean update(VO t);
    /**
     * 通过主键修改非空字段
     * @param t
     * @return
     */
    int modifyNotNullById(VO t);
}
