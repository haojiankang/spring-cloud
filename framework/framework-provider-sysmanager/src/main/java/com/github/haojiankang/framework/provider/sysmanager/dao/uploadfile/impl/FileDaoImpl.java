/** 
 * Project Name:EHealthData 
 * File Name:FileDaoImpl.java 
 * Package Name:com.ghit.ecg.uploadfile.dao.impl 
 * Date:2016年8月5日上午10:20:06  
*/  
  
package com.github.haojiankang.framework.provider.sysmanager.dao.uploadfile.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.haojiankang.framework.commons.utils.bean.ObjectUtils;
import com.github.haojiankang.framework.provider.sysmanager.api.model.po.uploadfile.UploadFile;
import com.github.haojiankang.framework.provider.sysmanager.dao.uploadfile.FileDao;
import com.github.haojiankang.framework.provider.utils.hibernate.BaseDaoImpl;

/** 
 * ClassName:FileDaoImpl <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Date:     2016年8月5日 上午10:20:06 <br/> 
 * @author   admin 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
@Repository
public class FileDaoImpl extends BaseDaoImpl<UploadFile, Serializable> implements FileDao {

    @Override
    public UploadFile findFileByMD5(UploadFile md5) {
        if(ObjectUtils.isEmpty(md5.getMd5())) return null;
        String hql = " from UploadFile where md5=?0";
        List<UploadFile> queryByHSql = this.queryByHSql(hql, new ArrayList<Object>(Arrays.asList(md5.getMd5())));
        return queryByHSql.size() > 0 ? queryByHSql.get(0) : null;
    }
}
  