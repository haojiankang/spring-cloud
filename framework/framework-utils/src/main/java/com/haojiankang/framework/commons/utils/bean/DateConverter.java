package com.haojiankang.framework.commons.utils.bean;

import java.util.Date;
import java.util.List;

import org.springframework.core.convert.converter.Converter;

import com.haojiankang.framework.commons.utils.lang.DateTimeUtil;

/**
 * 表单输入日期文本转换成日期实例
 * 
 * <pre>
 * {@code
 * <!-- 类型转换及数据格式化 -->
 * <bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
 *   <property name="converters">
 *    <list>
 *      <!-- 定义表单日期的转换器 -->
 *      <bean class="com.ghit.common.mvc.DateConverter">
 *        <property name="patterns">
 *          <list>
 *            <value>yyyy-MM-dd</value>
 *            <value>yyyy-MM-dd HH:mm:ss</value>
 *          </list>
 *        </property>
 *      </bean>
 *    </list>
 *   </property>
 * </bean>
 * <!-- 拦截器设置 -->
 * <mvc:interceptors>
 *   <bean class="org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor">
 *     <constructor-arg ref="conversionService"/>
 *   </bean>
 * </mvc:interceptors>
 * }
 * </pre>
 * 
 * @version 1.0.0
 */
public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String src) {
        Date dtTmp = null;
        if (patterns != null && patterns.size() > 0) {
            dtTmp = DateTimeUtil.convertAsDate(src, patterns.toArray(new String[patterns.size()]));
        }
        return dtTmp != null ? dtTmp : DateTimeUtil.convertAsDate(src);
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }

    private List<String> patterns = null;
}
