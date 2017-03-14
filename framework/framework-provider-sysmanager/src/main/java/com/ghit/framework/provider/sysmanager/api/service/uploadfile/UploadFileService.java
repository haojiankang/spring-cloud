/** 
 * Project Name:EHealthData 
 * File Name:UploadFileService.java 
 * Package Name:com.ghit.ecg.uploadfile.service 
 * Date:2016年8月2日上午10:31:37  
*/

package com.ghit.framework.provider.sysmanager.api.service.uploadfile;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.ghit.framework.provider.sysmanager.api.model.po.uploadfile.UploadFile;
import com.ghit.framework.provider.sysmanager.api.supports.security.model.IUser;
import com.ghit.framework.provider.sysmanager.api.supports.service.BaseService;

/**
 * ClassName:UploadFileService <br/>
 * Date: 2016年8月2日 上午10:31:37 <br/>
 * 
 * @author admin
 * @version
 * @since JDK 1.6
 * @see
 */
public interface UploadFileService extends BaseService<UploadFile,UploadFile> {
    /**
     * saveFile:上传文件保存.
     *
     * @author admin
     * @param t
     *            上传文件内容
     * @return 成功返回文件id，失败返回空
     * @since JDK 1.7
     */
    UploadFile saveFile(UploadFile t,IUser user);

    /**
     * 通过文件id获取文件信息
     * 
     * @param ids
     *            文件id集合
     * @return
     */
    UploadFile[] selectFilesByIds(String... ids);

    /**
     * 获取物理文件
     * 
     * @param t
     * @return
     */
    File getPhysicalFile(UploadFile t);

    /**
     * 
     * getFileByConditions:根据条件查询文件信息. 
     * @author ren7wei
     * @param conditions
     * @return
     * @since JDK 1.8
     */
    List<UploadFile> getFileByConditions(Map<String, Object> conditions);

    /**
     * 获取文件实际MD5值
     * @param fid 文件ID
     * @return 包含实际MD5值的上传文件对象
     */
    UploadFile getUploadFileActualMD5(String fid);

}