/** 
 * Project Name:basic-core 
 * File Name:FIreals.java 
 * Package Name:com.ghit.wf.model 
 * Date:2016年11月17日下午3:28:39  
*/

package com.github.haojiankang.framework.provider.sysmanager.api.supports.wf;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.haojiankang.framework.provider.sysmanager.api.model.vo.wf.Activity;
import com.github.haojiankang.framework.provider.sysmanager.api.service.wf.ActivitiesFilter;
import com.github.haojiankang.framework.provider.sysmanager.api.service.wf.ActivityFilter;

/**
 * ClassName:FIreals <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年11月17日 下午3:28:39 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class FIreals {
    public static ActivitiesFilter findActivitiesByBpa(String bpacode) {
        ActivitiesFilter filter = activities -> {
            return activities.stream()
                    .filter(a -> a.getBPNAction() != null && bpacode.equals(a.getBPNAction().getCode()))
                    .collect(Collectors.toList());
        };
        return filter;
    }

    public static ActivityFilter findActivityByBpa(String bpacode) {
        ActivityFilter filter = activities -> {
            Stream<Activity> stream = activities.stream()
                    .filter(a -> a.getBPNAction() != null && bpacode.equals(a.getBPNAction().getCode()));
            return stream.min((a1,a2)->a1.getExecuteTime().getTime()>a2.getExecuteTime().getTime()?1:-1).orElse(null);
        };
        return filter;
    }
}
