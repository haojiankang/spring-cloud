/** 
 * Project Name:framework-utils 
 * File Name:FeignRemoteCallMessageConvert.java 
 * Package Name:com.ghit.framework.commons.utils.spring.convert 
 * Date:2017年3月15日下午8:28:49  
*/

package com.haojiankang.framework.commons.utils.spring.convert;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonValue;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haojiankang.framework.commons.utils.bean.BeanUtils;

/**
 * ClassName:FeignRemoteCallMessageConvert <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年3月15日 下午8:28:49 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.6
 * @see
 */
public class FeignRemoteCallMessageConvert extends AbstractGenericHttpMessageConverter<Object> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    protected ObjectMapper objectMapper;

    private List<MediaType> mediaTypes = new ArrayList<>();

    public FeignRemoteCallMessageConvert() {
        init();
    }

    protected void init() {
        setDefaultCharset(DEFAULT_CHARSET);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(new DefaultIndenter("  ", "\ndata:"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return !BeanUtils.isSimpleType(clazz);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        OutputStream body = outputMessage.getBody();
        body.write(BeanUtils.oToFromData(object).getBytes());
        body.flush();

    }

    @Override
    protected MediaType getDefaultContentType(Object object) throws IOException {
        if (object instanceof MappingJacksonValue) {
            object = ((MappingJacksonValue) object).getValue();
        }
        return super.getDefaultContentType(object);
    }

    @Override
    protected Long getContentLength(Object object, MediaType contentType) throws IOException {
        if (object instanceof MappingJacksonValue) {
            object = ((MappingJacksonValue) object).getValue();
        }
        return super.getContentLength(object, contentType);
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    public FeignRemoteCallMessageConvert addMediaType(String type, String subtype) {
        mediaTypes.add(new MediaType(type, subtype));
        return this;
    }

    public FeignRemoteCallMessageConvert addMediaType(MediaType mediaType) {
        mediaTypes.add(mediaType);
        return this;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return mediaTypes;
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return null;
    }
}
