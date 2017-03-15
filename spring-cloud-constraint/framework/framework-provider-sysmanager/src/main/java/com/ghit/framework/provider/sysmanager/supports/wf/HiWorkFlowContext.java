/** 
 * Project Name:EHealthData 
 * File Name:WorkFlowContext.java 
 * Package Name:com.ghit.wf 
 * Date:2016年8月29日上午11:02:08  
*/

package com.ghit.framework.provider.sysmanager.supports.wf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ghit.framework.commons.utils.spring.SpringUtils;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.HiActivityBean;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.HiBPDBean;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.HiBPNActionBean;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.HiBPNBean;
import com.ghit.framework.provider.sysmanager.api.model.vo.wf.HiProcessBean;
import com.ghit.framework.provider.sysmanager.api.service.wf.WFService;

/**
 * ClassName:WorkFlowContext <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年8月29日 上午11:02:08 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class HiWorkFlowContext {

    protected final static Log LOGGER = LogFactory.getLog(HiWorkFlowContext.class);
    static Map<String, HiBPDBean> bpds = new HashMap<>();
    static Map<String, HiBPNBean> bpns = new HashMap<>();
    static Map<String, HiBPNActionBean> actions = new HashMap<>();
    static Map<String, String> actionNodeMapping = new HashMap<>();

    public static class BpdInit implements Runnable {

        @SuppressWarnings("unchecked")
        private WFService<HiBPDBean, HiBPNBean, HiProcessBean, HiActivityBean, HiBPNActionBean> wfService = SpringUtils
                .getBean(WFService.class);

        @Override
        public void run() {
            Map<String, HiBPDBean> t_bpds = loadBpd();
            Map<String, HiBPNBean> t_bpns = loadBpn();
            Map<String, HiBPNActionBean> t_actions = loadAction();
            // 加载流程定义信息
            // 加载流程节点定义信息
            // 加载流程节点动作信息
            cascade(t_bpds, t_bpns, t_actions);
            bpds.clear();
            bpns.clear();
            actions.clear();
            bpds.putAll(t_bpds);
            bpns.putAll(t_bpns);
            actions.putAll(t_actions);
            actionNodeMapping.putAll(mapping());
        }

        private Map<String, String> mapping() {
            Map<String, String> mapping = actions.entrySet().stream().collect(Collectors.toMap((t -> t.getKey()),
                    t -> t.getValue().getOwnBPN().stream().map(HiBPNBean::getCode).collect(Collectors.joining(","))));
            return mapping;
        }

        private Map<String, HiBPNBean> loadBpn() {
            List<HiBPNBean> list = wfService.allBPNs();
            return list.stream().collect(Collectors.toMap(HiBPNBean::getCode, t -> t));
        }

        private Map<String, HiBPDBean> loadBpd() {
            List<HiBPDBean> list = wfService.allBPDs();
            return list.stream().collect(Collectors.toMap(HiBPDBean::getCode, t -> t));
        }

        private Map<String, HiBPNActionBean> loadAction() {
            List<HiBPNActionBean> list = wfService.allActions();
            return list.stream().collect(Collectors.toMap(HiBPNActionBean::getCode, t -> t));
        }

        private void cascade(Map<String, HiBPDBean> bpds, Map<String, HiBPNBean> bpns,
                Map<String, HiBPNActionBean> actions) {
            bpds.forEach((bpdCode, bpd) -> {
                bpd.setAllBPN(new ArrayList<>());
                bpns.forEach((bpnCode, bpn) -> {
                    if (bpdCode.equals(bpn.getBPD().getCode())) {
                        bpd.getAllBPN().add(bpn);
                        bpn.setBpd(bpd);
                    }
                    if (bpnCode.equals(bpd.getStartBPN().getCode())) {
                        bpd.setStartBPN(bpn);
                    }
                    if (bpnCode.equals(bpd.getEndBPN().getCode())) {
                        bpd.setEndBPN(bpn);
                    }
                    if (bpn.getActions() == null)
                        bpn.setActions(new ArrayList<>());
                    actions.forEach((actionCode, action) -> {
                        if (actionCode.equals(bpn.getDefAction().getCode())) {
                            bpn.setDefAction(action);
                        }
                        if (action.getOwnBPN().stream().filter(n -> {
                            return n.getCode().equals(bpnCode);
                        }).findFirst().orElse(null) != null) {
                            action.setBpd(bpd);
                            if (action.getOwnBPN() == null) {
                                action.setOwnBPN(new HashSet<>());
                            }
                            action.getOwnBPN().add(bpn);
                            bpn.getActions().add(action);
                        }
                        if (bpnCode.equals(action.getNextBPN().getCode())) {
                            action.setNextBPN(bpn);
                        }
                    });
                });
            });
        }
    }

    public static HiBPDBean getBPD(String bpdCode, boolean deep) {
        return deep ? deepClone(bpds.get(bpdCode)) : bpds.get(bpdCode);
    }

    public static HiBPNBean getBPN(String bpdCode, boolean deep) {
        return deep ? deepClone(bpns.get(bpdCode)) : bpns.get(bpdCode);
    }

    public static HiBPNActionBean getAction(String bpdCode, boolean deep) {
        return deep ? deepClone(actions.get(bpdCode)) : actions.get(bpdCode);
    }

    public static Map<String, String> getActionNodeMapping(boolean deep) {
        return deep ? deepClone(actionNodeMapping) : actionNodeMapping;
    }

    @SuppressWarnings("unchecked")
    private static <T> T deepClone(T t) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(t);
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            return (T) (oi.readObject());
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return null;
    }
}
