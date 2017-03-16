/** 
 * Project Name:ghit-basic-provider 
 * File Name:ProviderContext.java 
 * Package Name:com.ghit.basic.common 
 * Date:2017年2月25日上午9:36:59  
*/

package com.github.haojiankang.framework.provider.sysmanager.supports;

import java.util.Hashtable;

import com.github.haojiankang.framework.commons.utils.lang.StringUtil;
import com.github.haojiankang.framework.commons.utils.security.context.Context;
import com.github.haojiankang.framework.commons.utils.security.context.HashTableContext;
import com.github.haojiankang.framework.commons.utils.security.model.IUser;
import com.github.haojiankang.framework.commons.utils.security.model.ResourceInformation;

/**
 * ClassName:ProviderContext <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年2月25日 上午9:36:59 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class ProviderContext {
    private static final Context context ;
    static{
        context = new HashTableContext();
        context.bind(ProviderConstant.CONTEXT_LOGIN_FAIL_INFO, new Hashtable<String,Integer>());
    }
    public static  <T> T lookup(Object name) {
        return context.lookup(name);
    }
    public static ResourceInformation getResource(IUser user) {
        // 获取全部资源信息
        ResourceInformation resource = (ResourceInformation) context.lookup(ProviderConstant.CONTEXT_RESOURCE_ROOT);
        resource = resource.clone();
        String jurisCode = "&" + user.JurisdictionCode() + "&";
        // 将未通过认证的资源信息删除
        authResource(resource, jurisCode);
        return resource;
    }
    /**
     * 递归认证资源，将code不在jurisCode中的资源排除
     */
    private static void authResource(ResourceInformation resource, String jurisCode) {
        if (resource.sons() != null) {
            for (int i = 0; i < resource.sons().size(); i++) {
                if (jurisCode.indexOf("&" + resource.sons().get(i).JurisdictionCode() + "&") == -1
                        && !StringUtil.isEmpty(resource.sons().get(i).JurisdictionCode())) {
                    resource.sons().remove(i);
                    i--;
                } else {
                    authResource(resource.sons().get(i), jurisCode);
                }
            }
        }
    }
    public static Context getContext(){
        return context;
    }
}
