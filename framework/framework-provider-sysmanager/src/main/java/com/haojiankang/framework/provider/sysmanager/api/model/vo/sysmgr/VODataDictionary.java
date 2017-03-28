/** 
 * Project Name:EHealthData 
 * File Name:DataDictionary.java 
 * Package Name:com.ghit.ecg.sysmgr.entity 
 * Date:2016年7月25日下午1:10:25  
*/

package com.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr;

import com.haojiankang.framework.provider.sysmanager.api.supports.vo.BaseVO;

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

public class VODataDictionary  extends BaseVO {
    private static final long serialVersionUID = 637694685275710531L;
    private String name;
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
