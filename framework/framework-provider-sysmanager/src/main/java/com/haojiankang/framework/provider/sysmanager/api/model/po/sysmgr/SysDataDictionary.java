/** 
 * Project Name:EHealthData 
 * File Name:DataDictionary.java 
 * Package Name:com.ghit.ecg.sysmgr.entity 
 * Date:2016年7月25日下午1:10:25  
*/

package com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.haojiankang.framework.provider.sysmanager.api.supports.DbOnly;
import com.haojiankang.framework.provider.sysmanager.api.supports.po.BaseEntity;

/**
 * ClassName:DataDictionary <br>
 * Function: 数据字典实体. <br>
 * Date: 2016年7月25日 下午1:10:25 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */

@Entity
@Table(name = "HJK_SYS_DATA_DICTIONARY")
public class SysDataDictionary extends BaseEntity {

    private static final long serialVersionUID = 637694685275710531L;
    @DbOnly(name="字典名称")
    @Column(name = "NAME")
    private String name;
    @Column(name = "CONTENT")
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
