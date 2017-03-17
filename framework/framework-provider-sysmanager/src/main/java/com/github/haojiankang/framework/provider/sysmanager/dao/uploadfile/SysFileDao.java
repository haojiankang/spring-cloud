/** 
 * Project Name:EHealthData 
 * File Name:FileDao.java 
 * Package Name:com.ghit.common.fs 
 * Date:2016年8月2日上午9:37:39  
*/  
  
package com.github.haojiankang.framework.provider.sysmanager.dao.uploadfile;

import java.io.Serializable;

import com.github.haojiankang.framework.provider.sysmanager.api.model.po.uploadfile.UploadFile;
import com.github.haojiankang.framework.provider.utils.hibernate.BaseDao;

/** 
 * ClassName:FileDao <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2016年8月2日 上午9:37:39 <br/> 
 * @author   admin 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public interface SysFileDao extends BaseDao<UploadFile, Serializable> {
    /**
     * findFileByMD5:查询同一份文件是否已上传. 
     *
     * @author admin
     * @param md5
     * @return
     * @since JDK 1.7
     */
    UploadFile findFileByMD5(UploadFile md5);
}
  