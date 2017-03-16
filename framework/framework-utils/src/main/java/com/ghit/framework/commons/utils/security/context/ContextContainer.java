/** 
 * Project Name:EHealthData 
 * File Name:ContextContainer.java 
 * Package Name:com.ghit.common.mvc 
 * Date:2016年6月30日下午4:50:03  
*/

package com.ghit.framework.commons.utils.security.context;

/**
 * ClassName:ContextContainer <br/>
 * Function: 上下文容器 <br/>
 * Date: 2016年6月30日 下午4:50:03 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class ContextContainer {
    ThreadLocal<Context> local = new ThreadLocal<>();

    private ContextContainer() {

    }

    private static ContextContainer container;

    /**
     * 
     * getContainer:返回一个single的Container实例。
     *
     * @author ren7wei
     * @return
     * @since JDK 1.7
     */
    public static ContextContainer getContainer() {
        if (container == null)
            container = new ContextContainer();
        return container;
    }
    /**
     * 
     * getContext:获取当前会话所在的上下文.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    public Context getContext() {
        if (local.get() == null)
            local.set(new HashTableContext());
        return local.get();
    }
    /**
     * 
     * destroyContext:对象回收).
     *
     * @author ren7wei
     * @since JDK 1.8
     */
    public void destroyContext() {
        Context context = local.get();
        if (context != null) {
            context.destroy();
            local.remove();
        }
    }
}
