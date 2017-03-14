/** 
 * Project Name:EHealthData 
 * File Name:HiBPNAction.java 
 * Package Name:com.ghit.wf.hibernate 
 * Date:2016年8月29日上午9:52:43  
*/

package com.ghit.framework.provider.sysmanager.api.model.po.wf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * ClassName:HiBPNAction <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月29日 上午9:52:43 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@Entity
@Table(name = "HJK_WF_ACTION")
public class HiBPNAction {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "NAME")
    private String name;
    @Column(name = "OWN_BPD_CODE")
    private String bpdCode;
    @Column(name = "NEXT_BPN_CODE")
    private String nextBpnCode;
    @Column(name = "OWN_BPN_CODE")
    private String currentBpnCode;
    @Column(name = "CODE")
    private String code;
    @Column(name = "ACTION_TYPE")
    private String actionType;
    @Column(name = "AUTH_EXPR")
    private String authExpr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBpdCode() {
        return bpdCode;
    }

    public void setBpdCode(String bpdCode) {
        this.bpdCode = bpdCode;
    }

    public String getNextBpnCode() {
        return nextBpnCode;
    }

    public void setNextBpnCode(String nextBpnCode) {
        this.nextBpnCode = nextBpnCode;
    }

    public String getCurrentBpnCode() {
        return currentBpnCode;
    }

    public void setCurrentBpnCode(String currentBpnCode) {
        this.currentBpnCode = currentBpnCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getAuthExpr() {
        return authExpr;
    }

    public void setAuthExpr(String authExpr) {
        this.authExpr = authExpr;
    }

}
