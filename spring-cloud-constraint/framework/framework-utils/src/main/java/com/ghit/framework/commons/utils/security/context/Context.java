/** 
 * Project Name:EHealthData 
 * File Name:Context.java 
 * Package Name:com.ghit.common.mvc 
 * Date:2016年6月30日下午4:47:33  
*/

package com.ghit.framework.commons.utils.security.context;

import java.util.Set;

/**
 * ClassName:Context <br/>
 * Function: mvc上下文 <br/>
 * Date: 2016年6月30日 下午4:47:33 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public interface Context {
    /**
     * 
     * destroy:销毁资源释放内存.
     *
     * @author ren7wei
     * @since JDK 1.8
     */
    void destroy();

    /**
     * 
     * lookup:在上下文中查找name对应的值
     *
     * @author ren7wei
     * @param name
     * @return
     * @since JDK 1.8
     */
    <T extends Object> T lookup(Object name);

    /**
     * 
     * appendBind:追加绑定，只针对字符串能进行该操作，默认自动把多次的值用\r\n分隔开。
     * 
     * @author ren7wei
     * @param name
     * @param obj
     * @since JDK 1.8
     */
    void appendBind(Object name, String obj);

    /**
     * 
     * bind:为name绑定值.
     *
     * @author ren7wei
     * @param name
     * @param obj
     * @since JDK 1.8
     */
    void bind(Object name, Object obj);

    /**
     * 
     * rebind:为name重新绑定值.
     *
     * @author ren7wei
     * @param name
     * @param obj
     * @since JDK 1.8
     */
    void rebind(Object name, Object obj);

    /**
     * 
     * unbind:解除name绑定的值。.
     *
     * @author ren7wei
     * @param name
     * @since JDK 1.8
     */
    void unbind(Object name);

    /**
     * 
     * rename:重命名name.
     *
     * @author ren7wei
     * @param oldName
     * @param newName
     * @since JDK 1.8
     */
    void rename(Object oldName, String newName);

    /**
     * 
     * names:返回所有name的值.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    Set<Object> names();
}
