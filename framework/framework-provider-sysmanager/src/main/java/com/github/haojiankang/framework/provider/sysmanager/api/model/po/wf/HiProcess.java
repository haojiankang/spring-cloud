/** 
 * Project Name:EHealthData 
 * File Name:HiFlowState.java 
 * Package Name:com.ghit.wf.hibernate 
 * Date:2016年8月29日上午9:52:58  
*/

package com.github.haojiankang.framework.provider.sysmanager.api.model.po.wf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * ClassName:HiFlowState <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月29日 上午9:52:58 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */

@Entity
@Table(name = "HJK_WF_PROCESS")
public class HiProcess {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "BPD_CODE")
    private String bpdCode;
    @Column(name = "BPN_CODE")
    private String currentBpnCode;
    @Column(name = "ACTION_CODE")
    private String actionCode;
    @Column(name = "EXECUTOR")
    private String executor;
    @Column(name = "EXECUTOR_TYPE")
    private String executorType;
    @Column(name = "EXECUTOR_NAME")
    private String executorName;
    @Column(name = "MESSAGE")
    private String message;
    @Column(name = "EXECUTE_TIME")
    private Date executeTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBpdCode() {
        return bpdCode;
    }

    public void setBpdCode(String bpdCode) {
        this.bpdCode = bpdCode;
    }

    public String getCurrentBpnCode() {
        return currentBpnCode;
    }

    public void setCurrentBpnCode(String currentBpnCode) {
        this.currentBpnCode = currentBpnCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executro) {
        this.executor = executro;
    }

    public String getExecutorType() {
        return executorType;
    }

    public void setExecutorType(String executorType) {
        this.executorType = executorType;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

}
