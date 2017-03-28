/** 
 * Project Name:EHealthData 
 * File Name:HiBPN.java 
 * Package Name:com.ghit.wf.hibernate 
 * Date:2016年8月29日上午9:52:07  
*/

package com.haojiankang.framework.provider.sysmanager.api.model.po.wf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * ClassName:HiBPN <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月29日 上午9:52:07 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */

@Entity
@Table(name = "HJK_WF_BPN")
public class HiBPN {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CODE")
    private String code;
    @Column(name = "DEF_ACTION_CODE")
    private String defActionCode;
    @Column(name = "BPD_CODE")
    private String bpdCode;

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

    public String getDefActionCode() {
        return defActionCode;
    }

    public void setDefActionCode(String defActionCode) {
        this.defActionCode = defActionCode;
    }

    public String getBpdCode() {
        return bpdCode;
    }

    public void setBpdCode(String bpdCode) {
        this.bpdCode = bpdCode;
    }

}
