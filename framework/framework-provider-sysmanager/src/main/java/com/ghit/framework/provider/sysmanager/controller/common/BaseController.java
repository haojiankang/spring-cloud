/** 
 * Project Name:EHealthData 
 * File Name:BaseController.java 
 * Package Name:com.ghit.common.mvc.controller 
 * Date:2016年7月4日下午2:48:58  
*/

package com.ghit.framework.provider.sysmanager.controller.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ghit.framework.commons.utils.Page;
import com.ghit.framework.commons.utils.i18n.I118nUtils;
import com.ghit.framework.provider.sysmanager.api.supports.service.BaseService;
import com.ghit.framework.provider.sysmanager.controller.sysmgr.UserController;
import com.ghit.framework.provider.utils.PS;
import com.ghit.framework.provider.utils.hibernate.AbstractPojo;

/**
 * ClassName:BaseController <br/>
 * Function: 公共控制层类. <br/>
 * Date: 2016年7月4日 下午2:48:58 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public abstract class BaseController<PO extends AbstractPojo<?>,VO extends AbstractPojo<?>> {
    protected Log log = LogFactory.getLog(UserController.class);

    public abstract BaseService<PO,VO> getBaseService();

    /**
     * 
     * i118nPrefix:返回i118n前缀
     *
     * @author ren7wei
     * @return 默认为controller的RequestMapping.name的值
     * @since JDK 1.8
     */
    public String i118nPrefix() {
        return StringUtils.substringAfter(this.getClass().getAnnotation(RequestMapping.class).value()[0], "/");
    }

    @RequestMapping("list")
    @ResponseBody
    public Object list(Page page) {
        Map<String, Object> maps = new HashMap<String, Object>();
        if (listBefore(maps, page)) {
            page=getBaseService().list(page);
            listAfter(maps, page);
        }
        return listReturn(maps, page);
    }

    @RequestMapping(value = "info")
    @ResponseBody
    public Object info(VO t) {
        if (infoBefore(t)) {
            t = getBaseService().findById(t);
            infoAfter(t);
        }
        return infoReturn(t);
    }

    @RequestMapping(value = "save")
    @ResponseBody
    public Object save(VO t) {
        boolean isAdd = t.getId()==null?true:StringUtils.isEmpty(t.getId().toString());
        boolean state = false;
        if (saveBefore(t)) {
            state = getBaseService().save(t);
            saveAfter(t, state, isAdd);
        }
        return saveReturn(t, state, isAdd);
    }

    @RequestMapping(value = "del")
    @ResponseBody
    public Object del(@RequestBody List<VO> t) {
        if(t!=null&&t.size()==1){
            boolean state = false;
            if (delBefore(t.get(0))) {
                state = getBaseService().del(t.get(0));
                delAfter(t.get(0), state);
            }
            return delReturn(t.get(0), state);
        }else{
            boolean state = false;
            if (delBefore(t)) {
                for(VO one:t){
                    state = getBaseService().del(one);
                    delAfter(one, state);
                }
            }
            return delReturn(t, state);
        }
    }

    protected Map<Object, Object> initMap(Object[] keys, Object[] values) {
        Map<Object, Object> map = new HashMap<>();
        if (values.length == keys.length) {
            for (int i = 0; i < keys.length; i++) {
                map.put(keys[i], values[i]);
            }
        }
        return map;
    }

    /**
     * 
     * 
     * saveBefore:save方法前执行 返回false是会阻止后续save和saveAfter执行
     * 
     * @author ren7wei
     * @param t
     * @return
     * @since JDK 1.8
     */
    protected boolean saveBefore(VO t) {
        return true;
    }

    /**
     * 
     * saveAfter:save方法执行后执行
     *
     * @author ren7wei
     * @param t
     * @param state
     * @since JDK 1.8
     */
    protected void saveAfter(VO t, boolean state, boolean isAdd) {

    }

    protected boolean delBefore(VO t) {
        return true;
    }
    protected boolean delBefore(List<VO> t) {
        return true;
    }

    protected void delAfter(VO t, boolean state) {

    }

    protected boolean listBefore(Map<String, Object> maps, Page page) {
        return true;
    }

    protected void listAfter(Map<String, Object> maps, Page page) {

    }

    protected boolean infoBefore(VO t) {
        return true;
    }

    protected void infoAfter(VO t) {
    }

    protected Object infoReturn(VO t) {
        return t;
    }

    /**
     * 
     * saveReturn:save方法返回值
     * 
     * @author ren7wei
     * @param t
     * @param state
     * @param isAdd
     * @return
     * @since JDK 1.8
     */
    protected Object saveReturn(VO t, boolean state, boolean isAdd) {
        return initMap(new Object[] { "state", "msg" },
                new Object[] { state,
                        I118nUtils.get(i118nPrefix() + (isAdd ? ".add" : ".edit") + (state ? ".success" : ".fail"))
                                // 失败的时候查看上下文中是否存放了提示信息，有则取出并返回给用户。
                                + (state ? "" : "\r\n" + PS.error()) });
    }

    protected Object delReturn(VO t, boolean state) {
        return initMap(new Object[] { "state", "msg" },
                new Object[] { state, I118nUtils.get(i118nPrefix() + ".del" + (state ? ".success" : ".fail")) });
    }
    protected Object delReturn(List<VO> t, boolean state) {
        return initMap(new Object[] { "state", "msg" },
                new Object[] { state, I118nUtils.get(i118nPrefix() + ".del" + (state ? ".success" : ".fail")) });
    }

    protected Object listReturn(Map<String, Object> maps, Page page) {
        maps.put("page", page);
        return maps;
    }
}
