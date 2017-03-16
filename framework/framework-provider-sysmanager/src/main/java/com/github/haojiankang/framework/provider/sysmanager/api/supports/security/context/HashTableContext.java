/** 
 * Project Name:EHealthData 
 * File Name:HashTableContext.java 
 * Package Name:com.ghit.common.mvc 
 * Date:2016年7月5日下午4:26:41  
*/

package com.ghit.framework.provider.sysmanager.api.supports.security.context;

import java.util.Hashtable;
import java.util.Set;

/**
 * ClassName:HashTableContext <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年7月5日 下午4:26:41 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class HashTableContext implements Context{
    protected Hashtable<Object, Object> context = null;

    public HashTableContext() {
        context = new Hashtable<>();
    }

    public void destroy() {
        context = null;
    }

    @SuppressWarnings("unchecked")
    public <T> T lookup(Object name) {
        Object object = context.get(name);
        if(object==null)
            return null;
        return (T) object;
    }

    public void appendBind(Object name, String obj) {
        Object object = context.get(name);
        if (object != null)
            context.put(name, new StringBuilder(object.toString()).append("\r\n").append(obj).toString());
        else
            context.put(name, obj==null?"null":obj);
    }

    public void bind(Object name, Object obj) {
        context.put(name, obj);
    }

    public void rebind(Object name, Object obj) {
        context.put(name, obj);
    }

    public void unbind(Object name) {
        context.remove(name);
    }

    public void rename(Object oldName, String newName) {
        context.put(newName, context.remove(oldName));
    }

    public Set<Object> names() {
        return context.keySet();
    }

}
