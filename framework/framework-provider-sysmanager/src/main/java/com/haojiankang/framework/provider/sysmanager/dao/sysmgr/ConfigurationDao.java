/** 
 * Project Name:EHealthData 
 * File Name:ConfigurationDao.java 
 * Package Name:com.ghit.ecg.sysmgr.dao 
 * Date:2016年6月30日下午3:12:20  
*/  
  
package com.haojiankang.framework.provider.sysmanager.dao.sysmgr;

import java.io.Serializable;

import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysConfiguration;
import com.haojiankang.framework.provider.utils.hibernate.BaseDao;

/** 
 * ClassName:ConfigurationDao <br/> 
 * Function: 系统配置信息数据访问层. <br/> 
 * Date:     2016年6月30日 下午3:12:20 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
public interface ConfigurationDao extends BaseDao<SysConfiguration, Serializable> {

}
  