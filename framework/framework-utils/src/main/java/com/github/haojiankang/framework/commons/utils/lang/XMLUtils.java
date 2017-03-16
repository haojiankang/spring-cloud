package com.github.haojiankang.framework.commons.utils.lang;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * xml工具类
 */
public class XMLUtils {

    @SuppressWarnings("unchecked")
    public static <T> T xmlToBean(String xml, Class<T> cls) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(cls);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xml));
    }

    public static <T> String beanToXml(T t) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(t.getClass());
        Marshaller marshaller = context.createMarshaller();
        StringWriter sw = new StringWriter();
        marshaller.marshal(t, sw);
        return sw.toString().replaceFirst("standalone=\"yes\"", "");
    }

    public static String xmlFormat(String xml) {
        return xml.replace("><", ">\r\n<");
    }

    public static String createElement(String name, String value) {
        int indexOf = name.indexOf(" ");
        if (value == null || value.equals("null"))
            value = "";
        if (indexOf == -1) {
            return "<" + name + ">" + value + "</" + name + ">";
        } else {
            return "<" + name + ">" + value + "</" + name.substring(0, indexOf) + ">";
        }
    }
}