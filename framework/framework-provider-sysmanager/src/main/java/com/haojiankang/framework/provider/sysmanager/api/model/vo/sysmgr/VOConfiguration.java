/** 
 * Project Name:EHealthData 
 * File Name:Configuration.java 
 * Package Name:com.ghit.ecg.sysmgr.entity 
 * Date:2016年6月30日上午10:46:40  
*/

package com.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr;

import com.haojiankang.framework.provider.sysmanager.api.supports.vo.BaseVO;

/**
 * ClassName:Configuration <br/>
 * Function: 系统配置信息. <br/>
 * Date: 2016年6月30日 上午10:46:40 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.6
 * @see
 */
public class VOConfiguration extends BaseVO {

    private static final long serialVersionUID = -8309373994879579820L;
    /**
     * 机构ID
     */
    private String organizationId;
    /**
     * 配置信息分组名称
     */
    private String configurationGroup;
    /**
     * 配置项名称
     */
    private String configurationName;
    /**
     * 配置项值
     */
    private String configurationValue;
    /**
     * 回调接口
     */
    private String callback;
    /**
     * 状态 0为不启用，1为启用
     */
    private Integer flag;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getConfigurationGroup() {
        return configurationGroup;
    }

    public void setConfigurationGroup(String configurationGroup) {
        this.configurationGroup = configurationGroup;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    public String getConfigurationValue() {
        return configurationValue;
    }

    public void setConfigurationValue(String configurationValue) {
        this.configurationValue = configurationValue;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

}
