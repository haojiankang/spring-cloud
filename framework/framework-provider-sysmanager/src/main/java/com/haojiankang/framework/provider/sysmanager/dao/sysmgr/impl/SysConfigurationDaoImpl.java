/** 
 * Project Name:EHealthData 
 * File Name:ConfigurationDaoImpl.java 
 * Package Name:com.ghit.ecg.sysmgr.dao.impl 
 * Date:2016年7月1日上午10:36:35  
*/  
  
package com.haojiankang.framework.provider.sysmanager.dao.sysmgr.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysConfiguration;
import com.haojiankang.framework.provider.sysmanager.dao.sysmgr.ConfigurationDao;
import com.haojiankang.framework.provider.utils.hibernate.BaseDaoImpl;

/** 
 * ClassName:ConfigurationDaoImpl <br/> 
 * Function: 系统配置信息数据访问层. <br/> 
 * Date:     2016年7月1日 上午10:36:35 <br/> 
 * @author   ren7wei 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
@Repository
public class SysConfigurationDaoImpl extends BaseDaoImpl<SysConfiguration, Serializable> implements ConfigurationDao {

}
  