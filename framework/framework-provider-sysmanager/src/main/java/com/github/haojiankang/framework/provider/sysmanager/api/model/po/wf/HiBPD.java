/** 
 * Project Name:EHealthData 
 * File Name:HiBPD.java 
 * Package Name:com.ghit.wf.hibernate 
 * Date:2016年8月29日上午9:52:27  
*/

package com.github.haojiankang.framework.provider.sysmanager.api.model.po.wf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * ClassName:HiBPD <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月29日 上午9:52:27 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@Entity
@Table(name = "HJK_WF_BPD")
public class HiBPD {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CODE")
    private String code;
    @Column(name = "START_BPN_CODE")
    private String startBpnCode;
    @Column(name = "END_BPN_CODE")
    private String endBpnCode;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStartBpnCode() {
        return startBpnCode;
    }

    public void setStartBpnCode(String startBpnCode) {
        this.startBpnCode = startBpnCode;
    }

    public String getEndBpnCode() {
        return endBpnCode;
    }

    public void setEndBpnCode(String endBpnCode) {
        this.endBpnCode = endBpnCode;
    }

}
