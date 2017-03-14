package com.ghit.framework.provider.sysmanager.supports;

/**
 * 常量信息类
 * 
 * @author ren7wei
 *
 */
public class ProviderConstant {
    /**
     * 上下文中资源信息根节点的key
     */
    public static final String CONTEXT_RESOURCE_ROOT = "security.resource.root";
    /**
     * 上下文中机构信息根节点的key
     */
    public static final String CONTEXT_TREENODE_ROOT = "security.treenode.root";
    /**
     * 会话消息中的validate校验信息的key
     */
    public static final String SESSION_MESSAGE_VALIDATE = "session.message.validate";
    /**
     * 上下文中系统参数配置缓存的key
     */
    public static final String SYSMGR_CONFIGURATION_CACHE = "sysmgr.configuration.cache";
    /**
     * 上下文中数据字典缓存的key
     */
    public static final String SYSMGR_DATA_DICTIONARY_CACHE = "sysmgr.dataDictionary.cache";
    /**
     * 线程上下文中存放错误消息的key
     */
    public static final String TCONTEXT_MESSAGE_ERROR = "tcontext.message.error";
    /**
     * 线程上下文中存放提示消息的key
     */
    public static final String TCONTEXT_MESSAGE_INFO = "tcontext.message.info";
    /**
     * 用于存放用户登录失败的信息
     */
    public static final String CONTEXT_LOGIN_FAIL_INFO = "index.login.fail.info";

    /**
     * 用于存放系统配置中的配置项名称的值
     */
    public static final String SYSMGR_CONFIG_FAILCOUNT = "锁定所需登录次数";
    /**
     * 用于存放系统配置中的配置项名称的值
     */
    public static final String SYSMGR_CONFIG_FAILTIME = "用户锁定时间";
    /**
     * 用于存放系统配置中的系统邮箱设置的配置项名称
     */
    public static final String SYSMGR_CONFIG_MAIL = "系统邮箱设置";
    /**
     * 用于存放系统配置中的密码验证规则的配置项名称
     */
    public static final String SYSMGR_CONFIG_VALIDATE_PWD = "密码验证规则";
    /**
     * 用于存放系统配置中的新增用户初始化密码的配置项名称
     */
    public static final String SYSMGR_CONFIG_USER_INITPWD = "新增用户初始密码";
    /**
     * 用于存放系统配置中的机构树顶级节点编码
     */
    public static final String SYSMGR_CONFIG_TREE_ROOT = "organization.tree.root";
    /**
     * 用于存放系统配置中的机构树顶级节点编码
     */
    public static final String SYSMGR_CONFIG_AUTH_INFO = "Authentication.info";

}
