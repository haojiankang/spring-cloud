/** 
 * Project Name:EHealthData 
 * File Name:SystemTypeLogin.java 
 * Package Name:com.ghit.ecg.sysmgr.service.impl 
 * Date:2016年8月16日下午1:10:31  
*/

package com.ghit.framework.provider.sysmanager.service.sysmgr.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ghit.framework.commons.utils.bean.BeanUtils;
import com.ghit.framework.commons.utils.lang.StringUtil;
import com.ghit.framework.commons.utils.security.model.IUser;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.User;
import com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr.VOUser;
import com.ghit.framework.provider.sysmanager.api.service.sysmgr.UserMgr;
import com.ghit.framework.provider.sysmanager.dao.sysmgr.UserDao;
import com.ghit.framework.provider.sysmanager.supports.sysmgr.SharpSysmgr;
import com.ghit.framework.provider.utils.PS;

/**
 * ClassName:SystemTypeLogin <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月16日 下午1:10:31 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@Service("systemUserMgr")
public class SystemUserMgr implements UserMgr {
    @Resource
    private UserDao dao;

    @Override
    public IUser login(User user) {
        IUser currentUser = null;
        User findUser = dao.findUsersByName(user);
        if(findUser==null)
            return null;
        user.setCreateTime(findUser.getCreateTime());
        SharpSysmgr.userPwdMd5(user);
        if (findUser != null && StringUtil.eq(findUser.getPassword(), user.getPassword())) {
            currentUser = SharpSysmgr.convertUserToIUser(findUser);
        }
        return currentUser;
    }

    @Override
    public boolean modifyPassword(String userid,String oldPwd, String newPwd) {
        User user = dao.get(userid);
        if (!StringUtil.eq(user.getPassword(),
                PS.UrlRSAToHash(oldPwd, String.valueOf(user.getCreateTime().getTime())))) {
            PS.error("原密码不正确，请检查你的输入，修改密码不成功!");
            return false;
        }
        user.setPassword(PS.UrlRSADecrypt(newPwd));
        SharpSysmgr.userPwdMd5(user);
        return true;
    }

    @Override
    public boolean resetPassword(VOUser user) {
        User po = BeanUtils.poVo(user, User.class, "");
        User findUser = dao.findUsersByName(po);
        boolean status = false;
        if (StringUtil.eq(user.getMail(), findUser.getMail())) {
            String newPassword = randPassword();
            findUser.setPassword(newPassword);
            user.setPassword(newPassword);
            SharpSysmgr.userPwdMd5(findUser);
            status = true;
        } else {
            PS.error("输入的邮箱信息和注册的邮箱信息不符，不能重置密码。\r\n如有疑问请联系系统管理员。");
        }
        return status;
    }

    private String randPassword() {
        return Long.toString(System.currentTimeMillis() / 8 * 9, 16);
    }
}
