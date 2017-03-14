package com.ghit.framework.provider.sysmanager.api.supports.security.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ghit.framework.commons.utils.i18n.LanguageType;

/**
 * 用户信息接口
 * 
 * @author ren7wei
 *
 */
public interface IUser extends Serializable {
    /**
     * 
     * getId:用于返回用户的唯一标识.
     *
     * @author ren7wei
     * @return 用户ID
     * @since JDK 1.8
     */
    String getId();
    /**
     * 返回用户名(用于显示的名称，而不是登录帐号)
     * 
     * @return
     */
    String getUserName();

    /**
     * 返回角色信息列表
     * 
     * @return
     */
    List<? extends IRole> getRoles();

    /**
     * 返回所有路径认证类型的认证类容 返回格式通过&对url进行拼接 示例：url1&url2&url3
     * 
     * @return
     */
    String authenticationURL();

    /**
     * 返回用户具有的所有权限code 返回格式通过&对code进行拼接 示例：01001&01002&02005
     * 
     * @return
     */
    String JurisdictionCode();
    /**
     * 
     * getLanguageType:获得语言类型.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    LanguageType getLanguageType();
    /**
     * 
     * getDepartment:获取用户所属部门信息.
     *
     * @author ren7wei
     * @return 部门信息
     * @since JDK 1.8
     */
    IDepartment getDepartment();
    /**
     * 
     * getUserType:返回用户类型.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    Integer getUserType();
    /**
     * 
     * getData:用户其他绑定数据(登录的时候设置内容)
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    Map<String,Object> getData();
}
