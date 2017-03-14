package com.ghit.framework.provider.sysmanager.api.service.sysmgr;

import java.util.List;

import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.User;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOUser;
import com.ghit.framework.provider.sysmanager.api.supports.service.BaseService;

public interface UserService extends BaseService<User,VOUser>{
    public VOUser findUsersByName(String username);


    public List<VOUser> findAllUser();
    /**
     * 
     * modifyPassword:修改密码功能
     *
     * @author ren7wei
     * @param oldpwd 原密码
     * @param newpwd 新密码
     * @return 是否修改成功
     * @since JDK 1.8
     */
     boolean modifyPassword(String uiserid,String oldpwd,String newpwd);

     /**
      * 
      * reset:重置用户的密码信息
      *
      * @author ren7wei
      * @param id
      * @return
      * @since JDK 1.8
      */
     
     boolean reset(String id);

}
