package utils;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;

import java.io.IOException;

public class EmailTemplateRenderer {

    public String renderTemplateFromFile(String templateFileName, final Object sourceData) {
        byte[] bytes = new byte[0];
        try {
            String templateString = IOUtils.toString(
                    getClass().getClassLoader().getResourceAsStream(templateFileName)
            );
            return renderTemplate(templateString, sourceData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting resource file " + templateFileName, e);
        }
    }

    public String renderTemplate(String template, final Object sourceData) {
        return new StrSubstitutor(new ObjectBasedStrLookup(sourceData)).replace(template);
    }


}

class ObjectBasedStrLookup extends StrLookup {

    private Object bean;

    public ObjectBasedStrLookup(Object bean) {
        this.bean = bean;
    }

    @Override
    public String lookup(String key) {
        try {
            Object result = BeanUtils.getNestedProperty(bean, key);
            if(result == null)
                result = "-";
            else
                result = result.toString();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}


