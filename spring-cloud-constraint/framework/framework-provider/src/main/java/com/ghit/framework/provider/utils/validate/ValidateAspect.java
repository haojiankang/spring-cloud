/** 
 * Project Name:EHealthData 
 * File Name:ValidateAspect.java 
 * Package Name:com.ghit.common.validate 
 * Date:2016年6月22日下午3:05:45  
*/

package com.ghit.framework.provider.utils.validate;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.xidea.el.Expression;
import org.xidea.el.ExpressionFactory;
import org.xidea.el.impl.ExpressionFactoryImpl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ghit.framework.commons.utils.FileUtils;
import com.ghit.framework.commons.utils.i18n.I118nUtils;
import com.ghit.framework.commons.utils.security.context.ContextContainer;
import com.ghit.framework.provider.utils.ProviderConstant;

/**
 * ClassName:ValidateAspect <br/>
 * Function: 验证切点类. <br/>
 * Date: 2016年6月22日 下午3:05:45 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.6
 * @see
 */
@Aspect
@Component
public class ValidateAspect {
    protected static final Log LOGGER = LogFactory.getLog(ValidateAspect.class);
    static ExpressionFactory FACTORY = ExpressionFactoryImpl.getInstance();
    private static Map<String, ValidateInfo> validateInfo;
    // 加载验证配置文件
    static {
        validateInfo = new HashMap<>();
        ObjectMapper ojbmapper = new ObjectMapper();
        ojbmapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        ojbmapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        File validateDir = FileUtils.getClassRoot("validate");
        if(validateDir!=null){
            File[] validateFiles = validateDir.listFiles(f -> f.getName().endsWith(".validate"));
            if (validateFiles != null)
                Arrays.asList(validateFiles).stream().forEach(f -> {
                    try {
                        ValidateInfos info = ojbmapper.readValue(f, ValidateInfos.class);
                        info.getValidates().forEach(t -> {
                            validateInfo.put(t.getName(), t);
                            PretreatmentInfo(null, t.getInfos());
                        });
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                });
        }
    }

    /**
     * 
     * PretreatmentInfo:预处理info信息.
     *
     * @author ren7wei
     * @param info
     * @param infos
     * @since JDK 1.8
     */
    private static void PretreatmentInfo(Info info, List<Info> infos) {
        infos.forEach(i -> {
            if (info != null) {
                if (StringUtils.isEmpty(i.getField())) {
                    i.setField(info.getField());
                }
            }
            if (StringUtils.isNotEmpty(i.getType())) {
                ValidateEnum en = ValidateEnum.valueOf(i.getType());
                i.setMessage(en.getMessage(i.getName()));
                i.setValidate(en.getValidate());
            }
            i.setValidate(i.getValidate().replace("$", "validateFun.").replace("#this", "this." + i.getField()));
            if (i.getInfos() != null)
                PretreatmentInfo(i, i.getInfos());
        });
    }

    @Pointcut("@annotation(com.ghit.framework.provider.utils.validate.Validate)")
    public void validateAspect() {
    }

    @Before("validateAspect()")
    public void before(JoinPoint joinPoint) throws ValidateFailException {
        Validate validate = getValidate(joinPoint);
        int[] indexs = validate.indexs();
        String[] mapping = validate.mapping();
        Map<String, Object> message = new HashMap<>();
        // 根据注解对值进行校验
        for (int i = 0; i < indexs.length; i++) {
            Object obj = joinPoint.getArgs()[indexs[i]];
            String name = mapping[i] == null || mapping[i].equals("") ? obj.getClass().getName() : mapping[i];
            validate(obj, name, message);
        }
        // 有验证消息的情况下根据注解上的处理模式进行相应的处理。
        if (message.size() > 0) {
            if (validate.processMode() == ProcessMode.Exception) {
                throw new ValidateFailException().setValidateFailMessage(message);
            } else if (validate.processMode() == ProcessMode.Context) {
                ContextContainer.getContainer().getContext().bind(ProviderConstant.SESSION_MESSAGE_VALIDATE, message);
            }
        }
    }

    /**
     * 
     * validate:对输入参数进行验证，设置jsel上下文.
     *
     * @author ren7wei
     * @param obj
     * @param mapping
     * @param message
     * @since JDK 1.8
     */
    private void validate(Object obj, String mapping, Map<String, Object> message) {
        ValidateInfo info = validateInfo.get(mapping);
        if (info == null)
            return;
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("this", obj);
        context.put("validateFun", new ValidateFunction());
        validateInfos(message, info.getInfos(), context);
    }

    /**
     * 
     * validateInfos:使用jsel对表达式进行验证
     * 
     * @author ren7wei
     * @param message
     * @param infos
     * @param context
     * @since JDK 1.8
     */
    private void validateInfos(Map<String, Object> message, List<Info> infos, Map<String, Object> context) {
        infos.forEach(t -> {
            Expression el = FACTORY.create(t.getValidate());
            Object evaluate = el.evaluate(context);
            if (evaluate.equals(t.getExpect())) {
                message.put("state", false);
                Object msg = message.get("message");
                message.put("message",
                        new StringBuilder(msg == null ? "" : msg.toString())
                                .append(t.getI118n() == null ? t.getMessage() : I118nUtils.get(t.getI118n()))
                                .append("\r\n").toString());
            } else {
                if (t.getInfos() != null)
                    validateInfos(message, t.getInfos(), context);
            }
        });
    }

    /**
     * 
     * getValidate:获取方法上的Validate注解.
     *
     * @author ren7wei
     * @param joinPoint
     * @return
     * @since JDK 1.8
     */
    public Validate getValidate(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        return Arrays.asList(methods).stream().filter(t -> t.getName().equals(methodName))
                .filter(t -> t.getAnnotation(Validate.class) != null).filter(t -> {
                    if (t.getParameterTypes().length == arguments.length) {
                        for (int i = 0; i < t.getParameterCount(); i++) {
                            if (arguments[0] == null
                                    || !t.getParameterTypes()[i].isAssignableFrom(arguments[0].getClass())) {
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                }).findFirst().get().getAnnotation(Validate.class);
    }

}
