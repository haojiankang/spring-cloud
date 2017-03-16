package com.ghit.framework.provider.sysmanager.api.service.sysmgr;

import java.util.Map;

import com.ghit.framework.commons.utils.security.model.IUser;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOUser;

public interface IndexService {
    /**
     * 用户登录验证， 并将返回用户信息
     * 
     * @return
     */
    IUser userLogin(VOUser user);

    /**
     * 
     * resetPassword:重置用户密码
     * 
     * @author ren7wei
     * @param user
     * @return
     * @throws Exception
     * @since JDK 1.8
     */
    boolean resetPassword(VOUser user) throws Exception;

    /**
     * 
     * modifyPassword:修改指定用户的密码.
     *
     * @author ren7wei
     * @param userid
     * @param oldpwd
     * @param newpwd
     * @param userType
     * @return
     * @since JDK 1.8
     */
    boolean modifyPassword(String userid, String oldpwd, String newpwd, String userType);

    /**
     * 
     * getWorkFlowNodeActionMapping:返回流程节点和流程动作的映射关系
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    Map<String, String> getWorkFlowNodeActionMapping();

    /**
     * 
     * loadResource:加载菜单资源信息.
     *
     * @author ren7wei
     * @param resourceName
     * @param user
     * @return
     * @since JDK 1.8
     */
    Map<String, Object> loadResource(String resourceName, IUser user);

    /**
     * 
     * loadConfig:加载配置信息.
     *
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    Map<String, String> loadConfig();

}
