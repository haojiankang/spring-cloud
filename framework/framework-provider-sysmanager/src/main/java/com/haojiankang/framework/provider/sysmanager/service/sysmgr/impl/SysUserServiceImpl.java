package com.haojiankang.framework.provider.sysmanager.service.sysmgr.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebMethod;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haojiankang.framework.commons.utils.Page;
import com.haojiankang.framework.commons.utils.bean.BeanUtils;
import com.haojiankang.framework.commons.utils.bean.ObjectUtils;
import com.haojiankang.framework.commons.utils.lang.StringUtil;
import com.haojiankang.framework.provider.sysmanager.api.model.po.sysmgr.SysUser;
import com.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr.VOUser;
import com.haojiankang.framework.provider.sysmanager.api.service.sysmgr.UserService;
import com.haojiankang.framework.provider.sysmanager.dao.sysmgr.UserDao;
import com.haojiankang.framework.provider.sysmanager.supports.ProviderConstant;
import com.haojiankang.framework.provider.sysmanager.supports.service.BaseServiceImpl;
import com.haojiankang.framework.provider.sysmanager.supports.sysmgr.SharpSysmgr;
import com.haojiankang.framework.provider.utils.PS;
import com.haojiankang.framework.provider.utils.hibernate.BaseDao;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser, VOUser> implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public VOUser findUsersByName(String username) {
        if (ObjectUtils.isEmpty(username))
            return null;
        SysUser usrIn = new SysUser();
        usrIn.setUserName(username);
        SysUser usrOut = userDao.findUsersByName(usrIn);
        if (usrOut == null)
            return null;
        usrOut.getRoles().size();
        if (usrOut.getOrganization() != null)
            usrOut.getOrganization().getId();
        return BeanUtils.poVo(usrOut, voClass, "roles,organization,jurisdictions");
    }

    @WebMethod(exclude = true)
    @Override
    public List<VOUser> findAllUser() {
        return BeanUtils.poVoList(userDao.getAllList(), voClass, "roles,organization");
    }

    @Override
    protected SysUser getPO(VOUser vo) {
        return BeanUtils.poVo(vo, poClass, "organization,jurisdictions,roles");
    }
    @Override
    protected VOUser getVO(SysUser po) {
        return BeanUtils.poVo(po, voClass, "organization,jurisdictions,roles");
    }

    @Override
    protected boolean saveBefore(VOUser t, boolean isAdd) {
        String password = null;
        boolean state = super.saveBefore(t, isAdd);
        if (state) {
            if (isAdd) {
                SysUser po = BeanUtils.poVo(t, poClass, "");
                // 添加时自动生成密码，密码默认为123456，可以在系统配置中修改 新增用户默认密码配置进行修改。
                password = SharpSysmgr.getConfigValueDef(ProviderConstant.SYSMGR_CONFIG_USER_INITPWD, "123456");
                // 对生成的密码进行加密处理
                po.setPassword(password);
                po.setCreateTime(new Date());
                SharpSysmgr.userPwdMd5(po);
            } else {
                // 编辑时，密码为空则获取数据库的密码
                if (StringUtil.isEmpty(t.getPassword())) {
                    SysUser user = userDao.get(t.getId());
                    t.setPassword(user.getPassword());
                    userDao.evict(user);
                }
            }
        }
        return state;

    }

    @Override
    protected boolean listBefore(Page page) {
        page.setOrders(new ArrayList<>());
        page.getOrders().add("+createTime");
        return super.listBefore(page);
    }

    @Override
    protected void findByIdAfter(SysUser user, boolean beforeState) {
        user.getRoles().size();
        if (user.getOrganization() != null)
            user.getOrganization().getId();
    }

    @WebMethod(exclude = true)
    @Override
    public BaseDao<SysUser, Serializable> getBaseDao() {
        return userDao;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean modifyPassword(String uiserid, String oldpwd, String newpwd) {
        SysUser user = userDao.get(uiserid);
        if (!StringUtil.eq(user.getPassword(),
                PS.UrlRSAToHash(oldpwd, String.valueOf(user.getCreateTime().getTime())))) {
            PS.error("原密码不正确，请检查你的输入，修改密码不成功!");
            return false;
        }
        user.setPassword(PS.UrlRSADecrypt(newpwd));
        SharpSysmgr.userPwdMd5(user);
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean reset(String id) {
        SysUser user = userDao.get(id);
        // 自动生成密码，密码默认为123456，可以在系统配置中修改 新增用户默认密码配置进行修改。
        String password = SharpSysmgr.getConfigValueDef(ProviderConstant.SYSMGR_CONFIG_USER_INITPWD, "123456");
        // 对生成的密码进行加密处理
        user.setPassword(password);
        SharpSysmgr.userPwdMd5(user);
        userDao.update(user);
        return true;
    }
}
