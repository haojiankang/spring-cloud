/** 
 * Project Name:EHealthData 
 * File Name:SecurityPolicy.java 
 * Package Name:com.ghit.ecg.sysmgr.utils 
 * Date:2016年7月14日下午3:01:30  
*/

package com.github.haojiankang.framework.provider.sysmanager.supports.sysmgr;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.haojiankang.framework.commons.utils.security.model.IUser;
import com.github.haojiankang.framework.commons.utils.spring.SpringUtils;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.User;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOConfiguration;
import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOUser;
import com.github.haojiankang.framework.provider.sysmanager.api.service.sysmgr.UserMgr;
import com.github.haojiankang.framework.provider.sysmanager.supports.ProviderConstant;
import com.github.haojiankang.framework.provider.sysmanager.supports.ProviderContext;
import com.github.haojiankang.framework.provider.sysmanager.supports.conf.ProviderProperties;
import com.github.haojiankang.framework.provider.utils.PS;

/**
 * ClassName:SecurityPolicy <br>
 * Function: 安全策略工具类. <br>
 * Date: 2016年7月14日 下午3:01:30 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class SecurityPolicy {
    protected static final Log log = LogFactory.getLog(SecurityPolicy.class);
    // 用户连续登录失败最大次数
    private static int user_fail_login_count_max = 0;
    // 连续登录失败锁定时间
    private static long user_fail_login_lock_time = 30;
    // 锁定的用户信息
    public static Map<String, Long> lockInfo;
    static {
        ConfigurationUtils utils = ConfigurationUtils.initialization();
        VOConfiguration count = utils.getConfiguration(ProviderConstant.SYSMGR_CONFIG_FAILCOUNT);
        VOConfiguration time = utils.getConfiguration(ProviderConstant.SYSMGR_CONFIG_FAILTIME);
        lockInfo = new Hashtable<>();
        if (count != null)
            user_fail_login_count_max = Integer.parseInt(count.getConfigurationValue());
        if (time != null)
            user_fail_login_lock_time = Integer.parseInt(time.getConfigurationValue()) * 1000 * 30;
    }

    public static boolean checkUserStatus(String userName) {
        if (user_fail_login_count_max > 0) {
            Long time = lockInfo.get(userName);
            if (time != null) {
                if (System.currentTimeMillis() - time >= user_fail_login_lock_time) {
                    Map<String, Integer> infos = ProviderContext.lookup(ProviderConstant.CONTEXT_LOGIN_FAIL_INFO);
                    lockInfo.remove(userName);
                    infos.remove(userName);
                } else {
                    PS.error("该账户由于连续登录失败次数达到上限，请"
                            + (System.currentTimeMillis() - time - user_fail_login_lock_time) / 1000 / 60 + "分钟后再登录！");
                    return false;
                }
            } else {
                Map<String, Integer> infos = ProviderContext.lookup(ProviderConstant.CONTEXT_LOGIN_FAIL_INFO);
                Integer count = infos.get(userName);
                if (count != null && count >= user_fail_login_count_max) {
                    lockInfo.put(userName, System.currentTimeMillis());
                    PS.error("连续登录失败次数达到上限，请" + user_fail_login_lock_time / 1000 / 60 + "分钟后再登录！");
                    return false;
                }
            }
        }
        return true;
    }

    public static void changeUserStatus(String userName, boolean status) {
        Map<String, Integer> infos = ProviderContext.lookup(ProviderConstant.CONTEXT_LOGIN_FAIL_INFO);
        if (status) {
            infos.remove(userName);
        } else {
            if (infos.get(userName) != null)
                infos.put(userName, infos.get(userName) + 1);
            else
                infos.put(userName, 1);

        }
    }

    public static Map<String, UserMgr> beans;

    public static class UserInit implements Runnable {
        public Map<String, String> loginInfo;

        @Override
        public void run() {
            loginInfo = new HashMap<>();
            beans = new HashMap<>();
            ProviderProperties conf = SpringUtils.getBean("providerProperties");
            String[] types = conf.getLogin().getTypes();
            if (types == null) {
                log.info("no search login.policy config !");
                return;
            }
            for (String type : types) {
                beans.put(type.split("@")[0], SpringUtils.getBean(type.split("@")[1]));
            }
        }

    }

    public static IUser login(User user) {
        UserMgr loginSystem = beans.get(user.getUserType().toString());
        String name = user.getUserType() + "@" + user.getUserName();
        String realPwd = user.getPassword();
        if (SecurityPolicy.checkUserStatus(name)) {
            IUser currentUser = loginSystem.login(user);
            if (currentUser != null) {
                // currentUser.getData().put("loginName", user.getUserName());
                // Sharp.currentSession().token(Sharp.md5(Sharp.currentSession().sessionId()
                // +new Date().getTime()+ user.getUserName()));
                SecurityPolicy.changeUserStatus(name, true);
                // 获取密码验证规则，并对密码强度进行校验
                JsonNode json = (JsonNode) PS
                        .strToJsonObj("{" + SharpSysmgr.getConfigValueDef(ProviderConstant.SYSMGR_CONFIG_VALIDATE_PWD,
                                "regexp:\"\",message:\"\"") + "}");
                if (!realPwd.matches(json.findValue("regexp").asText())) {
                    PS.message("密码强度太低，请尽快修改密码！");
                }
                return currentUser;
            } else {
                SecurityPolicy.changeUserStatus(name, false);
            }
        }
        return null;
    }

    public static boolean resetPassowrd(VOUser user) {
        UserMgr loginSystem = beans.get(user.getUserType().toString());
        return loginSystem.resetPassword(user);
    }

    public static boolean modifyPassword(String userid, String oldpwd, String newpwd, String userType) {
        if (userType == null)
            userType = "0";
        UserMgr loginSystem = beans.get(userType);
        return loginSystem.modifyPassword(userid, oldpwd, newpwd);
    }

}
